package demo.springbatch.importCSVJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import demo.springbatch.entity.Customer;
import demo.springbatch.listener.ImportJobListener;
import lombok.AllArgsConstructor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchImportConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    private CustomerCsvReader csvReader;
    private FilterProcessor filterProcessor;
    private CustomerDbWriter dbWriter;
    

    @Bean("importCSV")
    public Job importCSVJob(ImportJobListener importJobListener) {

        return jobBuilderFactory.get("importCustomers")
            .listener(importJobListener)
            .flow(csvToDatabase())
            .end()
            .build();

    }

    public Step csvToDatabase() {

        return stepBuilderFactory.get("csv-step").<Customer, Customer>chunk(10)
            .reader(csvReader)
            .processor(filterProcessor)
            .writer(dbWriter)
            .taskExecutor(asyncTaskExecutor(10))
            .build();

    }

    public TaskExecutor asyncTaskExecutor(int limit) {

        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(limit);

        return asyncTaskExecutor;
        
    }

}