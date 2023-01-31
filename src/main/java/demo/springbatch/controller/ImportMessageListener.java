package demo.springbatch.controller;

import org.springframework.stereotype.Component;

@Component
// @RabbitListener(queues = "${spring.rabbitmq.queue}")
public class ImportMessageListener {

    // @Value("${spring.rabbitmq.exchange}")
    // private String exchange;

    // @Value("${spring.rabbitmq.routingkey}")
    // private String routingkey;

    // @Autowired
    // private RabbitTemplate rabbitTemplate;

    // @RabbitHandler
    // public void process(Message message) {

    //     // Extract the message content
    //     String messageContent = new String(message.getBody());

    //     System.out.println("LISTENER : " +  messageContent);        

    //     // Process the message
    //     // ...

    //     // Create the response message
    //     Message responseMessage = MessageBuilder
    //             .withBody("Batch response".getBytes())
    //             .build();

    //     // Send the response message
    //     rabbitTemplate.send(exchange, routingkey, responseMessage);
    // }

}
