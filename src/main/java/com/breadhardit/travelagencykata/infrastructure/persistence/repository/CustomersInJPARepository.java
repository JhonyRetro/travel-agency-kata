package com.breadhardit.travelagencykata.infrastructure.persistence.repository;

import com.breadhardit.travelagencykata.application.port.CustomersRepository;
import com.breadhardit.travelagencykata.domain.Customer;
import com.breadhardit.travelagencykata.infrastructure.persistence.entity.CustomerEntity;
import com.breadhardit.travelagencykata.infrastructure.persistence.entity.CustomerEntityAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Scope("singleton")
@Primary
@RequiredArgsConstructor
public class CustomersInJPARepository implements CustomersRepository {
    private final CustomersJPARepository customersJPARepository;

    private CustomerEntity buildEntityFromCustomer(Customer customer) {
        if (customer != null) {
            return CustomerEntityAdapter.from(customer);
        }  else {
            return null;
        }
    }

    private Customer getCustomerFromEntity(CustomerEntity entity) {
        if (entity != null) {
            return CustomerEntityAdapter.to(entity);
        }  else {
            return null;
        }
    }

    public void saveCustomer(Customer customer) {
        CustomerEntity customerEntity = buildEntityFromCustomer(customer);
        this.customersJPARepository.save(customerEntity);
    }

    @Override
    public Optional<Customer> getCustomerById(String id) {
        Customer customer = getCustomerFromEntity(this.customersJPARepository.findById(id).orElse(null));
        if (customer == null) {
            return Optional.empty();
        } else  {
            return Optional.of(customer);
        }
    }

    @Override
    public Optional<Customer> getCustomerByPassport(String passport) {
        Customer customer = getCustomerFromEntity(this.customersJPARepository.findById(passport).orElse(null));
        if (customer == null) {
            return Optional.empty();
        } else  {
            return Optional.of(customer);
        }
    }
}
