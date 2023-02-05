package demo.springbatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.springbatch.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {}
