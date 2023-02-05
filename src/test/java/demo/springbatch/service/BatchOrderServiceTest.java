package demo.springbatch.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import demo.springbatch.entity.BatchOrder;
import demo.springbatch.enums.OrderStatus;
import demo.springbatch.repository.BatchOrderRepository;

@ExtendWith(MockitoExtension.class)
public class BatchOrderServiceTest {

    @Mock
    private BatchOrderRepository repository;

    @InjectMocks
    private BatchOrderService service;

    @Test
    void updateStatus_Save_IfOrderIsRetrived() {

        // Given 
        BatchOrder expectedBatchOrder= new BatchOrder().setId(1);

        // When
        Mockito.when(repository.findById(anyLong())).thenReturn(Optional.of(expectedBatchOrder));

        //Then
        service.updateStatus(1l, OrderStatus.COMPLETED);
        Mockito.verify(repository, times(1)).save(any());

    }

    @Test
    void updateStatus_DoNotSave_IfOrderIsNotRetrived() {

        // Given 
        Optional<BatchOrder> notFoundBatchOrder = Optional.empty();
        long batchOrderId = 1l;

        // When
        Mockito.when(repository.findById(anyLong())).thenReturn(notFoundBatchOrder);

        //Then
        service.updateStatus(batchOrderId, OrderStatus.COMPLETED);
        Mockito.verify(repository, times(0)).save(any());

    }
}
