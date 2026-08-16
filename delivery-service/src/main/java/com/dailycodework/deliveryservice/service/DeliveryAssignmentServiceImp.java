package com.dailycodework.deliveryservice.service;

import com.dailycodework.deliveryservice.dto.AssignDeliveryRequest;
import com.dailycodework.deliveryservice.dto.Parcel;
import com.dailycodework.deliveryservice.entities.DeliveryAgentProfile;
import com.dailycodework.deliveryservice.entities.DeliveryAssignment;
import com.dailycodework.deliveryservice.entities.DeliveryStatus;
import com.dailycodework.deliveryservice.events.DeliveryStatusEvent;
import com.dailycodework.deliveryservice.feign.ParcelRestClient;
import com.dailycodework.deliveryservice.kafka.DeliveryEventProducer;
import com.dailycodework.deliveryservice.repository.DeliveryAgentProfileRepository;
import com.dailycodework.deliveryservice.repository.DeliveryAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryAssignmentServiceImp implements DeliveryAssignmentService {

    private final DeliveryAssignmentRepository assignmentRepository;
    private final DeliveryAgentProfileRepository agentRepository;
    private  final ParcelRestClient parcelRestClient;
    private final DeliveryEventProducer deliveryEventProducer;
    @Override
    public DeliveryAssignment assignParcelToAgent(AssignDeliveryRequest request) {

        Parcel parcel = parcelRestClient.getParcelById(request.getParcelId());
        if ("DELIVERED".equals(parcel.getStatus()) || "CANCELLED".equals(parcel.getStatus())) {
            throw new RuntimeException("Cannot assign delivered or cancelled parcel");
        }

        if (parcel == null || parcel.getId() == null) {
            throw new RuntimeException("Parcel not found with id: " + request.getParcelId());
        }

        DeliveryAgentProfile agent = agentRepository.findById(request.getAgentId())
                .orElseThrow(() -> new RuntimeException("Delivery agent not found with id: " + request.getAgentId()));

        if (Boolean.FALSE.equals(agent.getAvailable())) {
            throw new RuntimeException("Delivery agent is not available");
        }

        DeliveryAssignment assignment = DeliveryAssignment.builder()
                .parcelId(parcel.getId())
                .agent(agent)
                .status(DeliveryStatus.ASSIGNED)
                .assignedAt(LocalDateTime.now())
                .notes(request.getNotes())
                .build();

        agent.setAvailable(false);
        agent.setUpdatedAt(LocalDateTime.now());

        DeliveryAssignment savedAssignment = assignmentRepository.save(assignment);
        savedAssignment.setParcel(parcel);

        return savedAssignment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryAssignment> getAllAssignments() {
        List<DeliveryAssignment> assignments = assignmentRepository.findAll();
        assignments.forEach(this::attachParcel);
        return assignments;
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryAssignment getAssignmentById(Long id) {
        DeliveryAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery assignment not found with id: " + id));

        attachParcel(assignment);

        return assignment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryAssignment> getAssignmentsByAgentId(Long agentId) {
        List<DeliveryAssignment> assignments = assignmentRepository.findByAgentId(agentId);
        assignments.forEach(this::attachParcel);
        return assignments;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryAssignment> getAssignmentsByParcelId(Long parcelId) {
        List<DeliveryAssignment> assignments = assignmentRepository.findByParcelId(parcelId);
        assignments.forEach(this::attachParcel);
        return assignments;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryAssignment> getAssignmentsByStatus(DeliveryStatus status) {
        List<DeliveryAssignment> assignments = assignmentRepository.findByStatus(status);
        assignments.forEach(this::attachParcel);
        return assignments;
    }

    @Override
    public DeliveryAssignment startDelivery(Long id) {
        DeliveryAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery assignment not found with id: " + id));

        assignment.setStatus(DeliveryStatus.IN_PROGRESS);
        assignment.setStartedAt(LocalDateTime.now());
        DeliveryAssignment savedAssignment = assignmentRepository.save(assignment);

        DeliveryStatusEvent event = DeliveryStatusEvent.builder()
                .deliveryAssignmentId(savedAssignment.getId())
                .parcelId(savedAssignment.getParcelId())
                .agentId(savedAssignment.getAgent().getId())
                .agentUserId(savedAssignment.getAgent().getUserId())
                .eventType("DELIVERY_STARTED")
                .occurredAt(LocalDateTime.now())
                .comment("Delivery started")
                .build();

        deliveryEventProducer.publishDeliveryStarted(event);
        attachParcel(savedAssignment);

        return savedAssignment;
    }

    @Override
    public DeliveryAssignment completeDelivery(Long id) {
        DeliveryAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery assignment not found with id: " + id));

        assignment.setStatus(DeliveryStatus.DELIVERED);
        assignment.setCompletedAt(LocalDateTime.now());

        DeliveryAgentProfile agent = assignment.getAgent();
        agent.setAvailable(true);
        agent.setUpdatedAt(LocalDateTime.now());

        DeliveryAssignment savedAssignment = assignmentRepository.save(assignment);

        DeliveryStatusEvent event = DeliveryStatusEvent.builder()
                .deliveryAssignmentId(savedAssignment.getId())
                .parcelId(savedAssignment.getParcelId())
                .agentId(agent.getId())
                .agentUserId(agent.getUserId())
                .eventType("DELIVERY_COMPLETED")
                .occurredAt(LocalDateTime.now())
                .comment("Delivery completed")
                .build();

        deliveryEventProducer.publishDeliveryCompleted(event);

        attachParcel(savedAssignment);

        return savedAssignment;
    }

    @Override
    public DeliveryAssignment failDelivery(Long id, String notes) {
        DeliveryAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery assignment not found with id: " + id));

        assignment.setStatus(DeliveryStatus.FAILED);
        assignment.setCompletedAt(LocalDateTime.now());
        assignment.setNotes(notes);

        DeliveryAgentProfile agent = assignment.getAgent();
        agent.setAvailable(true);
        agent.setUpdatedAt(LocalDateTime.now());

        DeliveryAssignment savedAssignment = assignmentRepository.save(assignment);
        attachParcel(savedAssignment);

        return savedAssignment;
    }


    @Override
    public DeliveryAssignment cancelDelivery(Long id) {
        DeliveryAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery assignment not found with id: " + id));

        assignment.setStatus(DeliveryStatus.CANCELLED);
        assignment.setCompletedAt(LocalDateTime.now());

        DeliveryAgentProfile agent = assignment.getAgent();
        agent.setAvailable(true);
        agent.setUpdatedAt(LocalDateTime.now());

        DeliveryAssignment savedAssignment = assignmentRepository.save(assignment);
        attachParcel(savedAssignment);

        return savedAssignment;
    }

    @Override
    public void deleteAssignment(Long id) {
        DeliveryAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery assignment not found with id: " + id));

        assignmentRepository.delete(assignment);
    }

    @Override
    public Long countByInProgress() {
        return assignmentRepository.countByStatus(DeliveryStatus.IN_PROGRESS);
    }
    @Override
    public Long countByDelivered() {
        return assignmentRepository.countByStatus(DeliveryStatus.DELIVERED);
    }

    private void attachParcel(DeliveryAssignment assignment) {
        try {
            Parcel parcel = parcelRestClient.getParcelById(assignment.getParcelId());
            assignment.setParcel(parcel);
        } catch (Exception e) {
            assignment.setParcel(null);
        }
    }
}