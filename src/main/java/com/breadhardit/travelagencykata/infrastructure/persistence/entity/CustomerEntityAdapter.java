package com.breadhardit.travelagencykata.infrastructure.persistence.entity;

import com.breadhardit.travelagencykata.domain.Customer;

import java.time.LocalDate;

public class CustomerEntityAdapter {
    public static CustomerEntity from(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surnames(customer.getSurnames())
                .birthDate(customer.getBirthDate())
                .passportNumber(customer.getPassportNumber())
                .enrollmentDate(LocalDate.now())
                .active(true)
                .build();
    }
    public static Customer to(CustomerEntity customerEntity) {
        return Customer.builder()
                .id(customerEntity.getId())
                .name(customerEntity.getName())
                .surnames(customerEntity.getSurnames())
                .birthDate(customerEntity.getBirthDate())
                .passportNumber(customerEntity.getPassportNumber())
                .enrollmentDate(customerEntity.getEnrollmentDate())
                .active(customerEntity.getActive())
                .build();
    }
}
