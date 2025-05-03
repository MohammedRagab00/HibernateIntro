package com.sci.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
//@Setter
@Builder
@Getter
@ToString(exclude = {"customer"})
public class Orders {

    @Id
    private Integer order_id;

    private LocalDate order_date;

    private int order_total;

    // many orders for one customer
    @ManyToOne
    private Customers customer;

}
