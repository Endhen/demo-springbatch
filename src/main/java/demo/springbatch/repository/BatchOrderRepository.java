package demo.springbatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.springbatch.entity.BatchOrder;

public interface BatchOrderRepository extends JpaRepository<BatchOrder, Long> {}
