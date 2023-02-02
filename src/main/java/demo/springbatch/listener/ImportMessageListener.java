package demo.springbatch.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "import-job-queue")
public class ImportMessageListener {

    @Autowired
    @Qualifier("exportCSV")
    private Job exportCSVJob;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private JobLauncher jobLauncher;

    private final String exchange = "batch.exchange";
    private final String routingkey = "job.import";

    @RabbitHandler
    public void process(Integer orderId) {

        System.out.println("LISTENER : " + orderId);
        startImportJob(orderId);

        // TODO Change request status

        // TODO Create the response message
        // Message responseMessage = MessageBuilder
        //         .withBody("Batch response".getBytes())
        //         .build();

        // Send the response message
        // rabbitTemplate.convertSendAndReceive(exchange, routingkey, "responseMessage");
    }

    private void startImportJob(int orderId) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("Started at : ", System.currentTimeMillis()).toJobParameters();

        try {
            jobLauncher.run(
                exportCSVJob,
                jobParameters
            );
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
