package demo.springbatch.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.springbatch.entity.BatchOrder;
import demo.springbatch.enums.OrderStatus;
import demo.springbatch.repository.BatchOrderRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BatchOrderService implements BatchOrderServiceInterface {

    @Autowired
	private BatchOrderRepository repository;

    @Override
    public void updateStatus(long id, OrderStatus status) {

        Optional<BatchOrder> batchOrder = repository.findById(id);
        
        if (batchOrder.isPresent()) {
			batchOrder.get().setStatus(status);
			repository.save(batchOrder.get());
		} else {
			log.error("BatchOrder id " + id + "has been lost");
		}
    }
}
