package demo.springbatch.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import demo.springbatch.enums.OrderStatus;
import demo.springbatch.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class BatchOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private OrderType type = OrderType.EXPORT;
    private OrderStatus status = OrderStatus.PENDINNG;
    private final LocalDateTime dateTime = LocalDateTime.now();
}
