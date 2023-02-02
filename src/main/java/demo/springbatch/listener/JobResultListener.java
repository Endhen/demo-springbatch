package demo.springbatch.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobResultListener implements JobExecutionListener {

    private int jobId;

    public JobResultListener(int jobId) {
        this.jobId = jobId;
    }

	public void beforeJob(JobExecution jobExecution) {
        log.info("Before job : " + jobId);
	}

	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED ) {
	        //job success
            log.info("Job success : " + jobId);
	    }
	    else if (jobExecution.getStatus() == BatchStatus.FAILED) {
	        //job failure
            log.info("Job fail : " + jobId);
	    }
	}
}
