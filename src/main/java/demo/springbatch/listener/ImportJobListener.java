package demo.springbatch.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import demo.springbatch.enums.OrderStatus;
import demo.springbatch.service.BatchOrderServiceInterface;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImportJobListener implements JobExecutionListener {

	@Autowired 
	private BatchOrderServiceInterface batchOrderService;

	@Override
	public void beforeJob(JobExecution jobExecution) {

		long batchOrderId = getOrderId(jobExecution);

		log.info("Start processing batch order " + batchOrderId);
		log.info("[BATCH SERVICE] " + batchOrderService);
		batchOrderService.updateStatus(
			batchOrderId, 
			OrderStatus.PROCESSING
		);
	}

	@Override
	public void afterJob(JobExecution jobExecution) {

		long batchOrderId = getOrderId(jobExecution);

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

			log.info("Batch order " + batchOrderId + " complete");
			batchOrderService.updateStatus(
				batchOrderId, 
				OrderStatus.COMPLETED
			);

		} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
		
			log.error("Batch order " + batchOrderId + " failed");
			batchOrderService.updateStatus(
				batchOrderId,
				OrderStatus.ERROR
			);
		}
	}

	private Long getOrderId(JobExecution jobExecution) {
		JobParameters jobParameters = jobExecution.getJobParameters();
    	return jobParameters.getLong("order_id");
	}
}
