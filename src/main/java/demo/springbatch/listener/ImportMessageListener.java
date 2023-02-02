package demo.springbatch.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
@RabbitListener(queues = "start-queue")
public class ImportMessageListener {

    @Autowired
    @Qualifier("exportCSV")
    private Job exportCSVJob;

    @Autowired
    private JobLauncher jobLauncher;

    @RabbitHandler
    public void process(Long orderId) {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("order_id", orderId)
                .addLong("Started at : ", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(
                    exportCSVJob,
                    jobParameters);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

}
