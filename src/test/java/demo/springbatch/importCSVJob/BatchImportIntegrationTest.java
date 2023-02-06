package demo.springbatch.importCSVJob;

import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@SpringBatchTest
@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@ContextConfiguration(
    classes = { BatchImportConfig.class }
)
@TestExecutionListeners({ 
    DependencyInjectionTestExecutionListener.class, 
    DirtiesContextTestExecutionListener.class
})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class BatchImportIntegrationTest {

    private final String TEST_INPUT = "customers.json";

    private final String TEST_OUTPUT = "customers.csv";
    private final String EXPECTED_OUTPUT = "expected_customers.csv";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
  
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
  
    @After
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    private JobParameters importJobParameters() {
        JobParametersBuilder paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addString("file.input", TEST_INPUT);
        paramsBuilder.addString("file.output", TEST_OUTPUT);
        return paramsBuilder.toJobParameters();
   }

   @Test
    public void givenReferenceOutput_whenJobExecuted_thenSuccess() throws Exception {

        // given
        FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
        FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(importJobParameters());
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
    
        // then
        Assertions.assertEquals(actualJobInstance.getJobName(), "importCustomers");
        Assertions.assertEquals(actualJobExitStatus.getExitCode(), "COMPLETED");
        AssertFile.assertFileEquals(expectedResult, actualResult);
    }
    
}
