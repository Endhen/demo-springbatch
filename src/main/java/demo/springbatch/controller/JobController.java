package demo.springbatch.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("importCSV")
    private Job importCSVJob;

    @Autowired
    @Qualifier("exportCSV")
    private Job exportCSVJob;

    @GetMapping("/importCustomers")
    public void importCsvToDBJob() {
    
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("Started at : ", System.currentTimeMillis()).toJobParameters();

        try {
            jobLauncher.run(importCSVJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/exportCustomers")
    public void exportCsvToDBJob() {
    
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("Started at : ", System.currentTimeMillis()).toJobParameters();

        try {
            jobLauncher.run(exportCSVJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }


    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void recievedMessage(String incomingMessage) {
        System.out.println("Recieved Message From RabbitMQ: " + incomingMessage);
    }
}
