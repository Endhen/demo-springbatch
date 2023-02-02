package demo.springbatch.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;

import demo.springbatch.service.MessageSenderServiceInterface;

public class ImportJobListener implements JobExecutionListener {

	@Autowired
	private MessageSenderServiceInterface messageService;

	@Override
	public void beforeJob(JobExecution jobExecution) {

		System.out.println("BEFORE_JOB_EXECUTION : " + getOrderId(jobExecution));

		// messageBrokerService.sendMessage(
		// 	getOrderId(jobExecution), "job.processing");

	}

	@Override
	public void afterJob(JobExecution jobExecution) {

		System.out.println("STATUS GGG: " + getOrderId(jobExecution));

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) 
			System.out.println("STATUS : " + BatchStatus.COMPLETED);
			//     messageBrokerService.sendMessage(
				// 		getOrderId(jobExecution), "job.success");
				
		else if (jobExecution.getStatus() == BatchStatus.FAILED) 
			System.out.println("STATUS : " + BatchStatus.FAILED);
	    //     messageBrokerService.sendMessage(
		// 		getOrderId(jobExecution), "job.error");
	    
	}

	private Long getOrderId(JobExecution jobExecution) {
		JobParameters jobParameters = jobExecution.getJobParameters();
    	return jobParameters.getLong("order_id");
	}
}
