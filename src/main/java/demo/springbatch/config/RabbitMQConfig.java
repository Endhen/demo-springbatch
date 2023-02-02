package demo.springbatch.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange topicExchange() {
      return new TopicExchange("batch-exchange.topic");
    }

    @Bean
    public DirectExchange directExchange() {
      return new DirectExchange("batch-exchange.direct");
    }

    @Bean(name = "success-queue")
    public Queue successQueue() {
      return new Queue("success-queue");
    }

    @Bean(name = "error-queue")
    public Queue errorQueue() {
      return new Queue("error-queue");
    }

    @Bean(name = "processing-queue")
    public Queue processingQueue() {
      return new Queue("processing-queue");
    }

    @Bean(name = "start-queue")
    public Queue startQueue() {
      return new Queue("start-queue");
    }

    
    @Bean
    public Binding directcBinding(
            @Qualifier("start-queue") Queue startQueue, 
            DirectExchange directExchange
        ) {

        return BindingBuilder
            .bind(startQueue)
            .to(directExchange)
            .with("job.start");
    }

    @Bean
    public Declarables topicBindings(
            @Qualifier("processing-queue") Queue processingQueue,
            @Qualifier("success-queue") Queue successQueue,
            @Qualifier("error-queue") Queue errorQueue,
            TopicExchange topicExchange
        ) { 

        return new Declarables(
            processingQueue,
            successQueue,
            errorQueue,
            topicExchange,
            BindingBuilder
                .bind(successQueue)
                .to(topicExchange).with("job.success"),
            BindingBuilder
                .bind(errorQueue)
                .to(topicExchange).with("job.error"),
            BindingBuilder
                .bind(processingQueue)
                .to(topicExchange).with("job.processing")
        );
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}