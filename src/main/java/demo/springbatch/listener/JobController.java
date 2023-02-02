package demo.springbatch.listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("importCSV")
    private Job importCSVJob;

    @Autowired
    @Qualifier("exportCSV")
    private Job exportCSVJob;

    // @RabbitListener(queues = "${spring.rabbitmq.queue}")
    // public void importCsvToDBJob(String incomingMessage) {

    //     if (incomingMessage.equals("import")) {
        
    //         JobParameters jobParameters = new JobParametersBuilder()
    //                 .addLong("Started at : ", System.currentTimeMillis()).toJobParameters();

    //         try {
    //             jobLauncher.run(importCSVJob, jobParameters);
    //         } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }

    // @RabbitListener(queues = "${spring.rabbitmq.queue}")
    // public void exportCsvToDBJob(String incomingMessage) {

    //     if (incomingMessage.equals("export")) {

            // JobParameters jobParameters = new JobParametersBuilder()
            //         .addLong("Started at : ", System.currentTimeMillis()).toJobParameters();

            // try {
            //     jobLauncher.run(exportCSVJob, jobParameters);
            // } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            //     e.printStackTrace();
            // }
    //     }
    // }
}
