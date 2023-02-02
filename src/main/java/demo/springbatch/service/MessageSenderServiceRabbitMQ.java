package demo.springbatch.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class MessageSenderServiceRabbitMQ implements MessageSenderServiceInterface {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    private final String exchange = "batch-exchange.topic";

    @Scheduled
    public void sendMessage(String routingkey, long orderId) {

        rabbitTemplate.convertAndSend(exchange, routingkey, orderId);
    }

}