package com.dailycodework.parcelservice.dto;

import com.dailycodework.parcelservice.entities.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateParcelStatusRequest {

    private Status status;
    private Long changedBy;
    private String comment;
}