package demo.springbatch.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue}")
    String queueName;

    @Value("${spring.rabbitmq.exchange}")
    String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    @Bean
    public Declarables topicBindings() {
        Queue importJobQueue = new Queue("import-job-queue", true);
        Queue successQueue = new Queue("success-queue", true);
        Queue errorQueue = new Queue("error-queue", true);

        TopicExchange topicExchange = new TopicExchange("batch.exchange");

        return new Declarables(
            importJobQueue,
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
                .bind(importJobQueue)
                .to(topicExchange).with("job.import")
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