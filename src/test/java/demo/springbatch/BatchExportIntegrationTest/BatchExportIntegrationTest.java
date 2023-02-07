package demo.springbatch.BatchExportIntegrationTest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import demo.springbatch.exportCSVJob.BatchExportConfig;
import demo.springbatch.exportCSVJob.CustomerCsvWriter;
import demo.springbatch.exportCSVJob.CustomerDbReader;
import demo.springbatch.listener.ImportJobListener;

@SpringBatchTest
@EnableAutoConfiguration
@SpringJUnitConfig(
    classes= {
        BatchExportConfig.class,
        CustomerCsvWriter.class,
        CustomerDbReader.class
    }
)
@ActiveProfiles("test")
@ConfigurationProperties(prefix = "env")
public class BatchExportIntegrationTest {
    
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
  
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @MockBean
    private ImportJobListener importJobListener;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
  
    @After
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    public void testJob(@Autowired Job exportCSVJob) throws Exception {
        // Given
        jobLauncherTestUtils.setJob(exportCSVJob);
        populateCustomerTable();

        // When 
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Then
        Assertions.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }

    private void populateCustomerTable() throws IOException { 

        ClassPathResource sqlFile = new ClassPathResource(
            "/src/test/java/demo/springbatch/BatchExportIntegrationTest/customer_input.sql"
        );
        String sqlQuery = Files.readString(
            Paths.get(sqlFile.getPath()), 
            Charset.defaultCharset()
        );

        jdbcTemplate.execute(sqlQuery);

        for (int i = 1; i <= 10; i++) 
            jdbcTemplate.update(
                "INSERT INTO customer VALUES (?, ?, ?, ?, ?, ?, ?, ?)", 
                i, 
                "first_name" + i,
                "last_name" + i,
                "birthday" + i,
                "email" + i,
                "contact_no" + i,
                "country" + i,
                "gender" + i
            );
        
    }
}
