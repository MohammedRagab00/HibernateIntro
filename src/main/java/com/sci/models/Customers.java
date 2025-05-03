package com.sci.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"orders"})
public class Customers {

    @Id
    private Integer customer_id;

    private String last_name;

    private String email;

    private String phone_number;

    @OneToMany(mappedBy = "customer")
    private List<Orders> orders;

}
