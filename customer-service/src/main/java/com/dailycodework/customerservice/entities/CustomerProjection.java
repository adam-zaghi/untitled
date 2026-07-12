package com.dailycodework.customerservice.entities;

import org.springframework.data.rest.core.config.Projection;

@Projection(name="no",types=Customer.class)
public interface CustomerProjection {
    String getFirstName();
    String getLastName();

}
