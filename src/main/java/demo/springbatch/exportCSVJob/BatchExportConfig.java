package demo.springbatch.exportCSVJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo.springbatch.entity.Customer;
import demo.springbatch.listener.ImportJobListener;
import lombok.AllArgsConstructor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchExportConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private CustomerDbReader dbReader;
    private CustomerCsvWriter csvWriter;

    @Bean("exportCSV")
    public Job exportCSVJob(ImportJobListener importJobListener) {

        return jobBuilderFactory.get("export-CSV")
            .listener(importJobListener)
            .flow(databaseToCSV())
            .end()
            .build();

    }
    
    public Step databaseToCSV() {
        
        return stepBuilderFactory.get("database-to-csv").<Customer, Customer>chunk(10)
            .reader(dbReader)
            .writer(csvWriter)
            .build();

    }

}