package com.sci;

import com.sci.dao.DBCustomers;
import com.sci.dao.DBOrders;
import com.sci.models.Customers;
import com.sci.models.Orders;

import java.time.LocalDate;
import java.util.Date;

public class Test {

    public static void main(String[] args) {

        DBCustomers dbCustomers = new DBCustomers();

        Customers customer = new Customers(
                199, "salah", "salah@mail", "0123456789", null
        );
//        dbCustomers.insert(customer);


        DBOrders dbOrders = new DBOrders();

//        dbOrders.insert(Orders.builder()
//                .order_date(LocalDate.of(2001, 1, 1))
//                .order_id(201)
//                .customer(customer)
//                .order_total(2000)
//                .build()
//        );


        Orders orders = dbOrders.get(201);
        System.out.println(orders);

    }
}
