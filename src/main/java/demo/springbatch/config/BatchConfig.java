package demo.springbatch.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import demo.springbatch.entity.Customer;
import demo.springbatch.repository.CustomerRepository;
import lombok.AllArgsConstructor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private CustomerRepository customerRepository;

    @Autowired
    private DataSource dataSource;


    @Bean
    public FlatFileItemReader<Customer> reader() {
    
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        itemReader.setLineMapper(lineMapper());
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);

        return itemReader;
    }

    private LineMapper<Customer> lineMapper() {

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "birthday");

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public CustomerProcessor processor() {
        return new CustomerProcessor();
    }

    @Bean
    public RepositoryItemWriter<Customer> writer() {

        RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
        writer.setRepository(customerRepository);
        writer.setMethodName("save");
        
        return writer;
    }

    @Bean
    public Step step1() {

        return stepBuilderFactory.get("csv-step").<Customer, Customer>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();

    }

    @Bean("importCSV")
    public Job runJob() {

        return jobBuilderFactory.get("importCustomers")
                .flow(step1())
                .end()
                .build();

    }

    @Bean
    public TaskExecutor taskExecutor() {

        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);

        return asyncTaskExecutor;
    }

    @Bean
    public JdbcCursorItemReader<Customer> dbReader() {

        JdbcCursorItemReader<Customer> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT id,first_name, last_name, email FROM customer");
        reader.setRowMapper(sqlRowMapper());

        return reader;
    }

    @Bean
    public RowMapper<Customer> sqlRowMapper() {

        return new RowMapper<Customer>() {
            
            @Override
            @Nullable
            public Customer mapRow(ResultSet resultSet, int nowNumber) throws SQLException {
                
                return new Customer(
                    resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    "", "", "", ""
                    // resultSet.getString("gender"),
                    // resultSet.getString("contact_no"),
                    // resultSet.getString("country"),
                    // resultSet.getString("birthday")
                );
            }
        };
    }

    @Bean
    public FlatFileItemWriter<Customer> csvWriter() {

        BeanWrapperFieldExtractor<Customer> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"id", "firstName", "lastName", "email"});
        
        DelimitedLineAggregator<Customer> aggregator = new DelimitedLineAggregator<>();
        aggregator.setFieldExtractor(fieldExtractor);

        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("src/main/resources/output/customers.csv"));
        writer.setLineAggregator(aggregator);

        return writer;

    }

    @Bean
    public Step databaseToCSV() {
        
        return stepBuilderFactory.get("database-to-csv").<Customer, Customer>chunk(10)
            .reader(dbReader())
            .writer(csvWriter())
            .build();

    }

    @Bean("exportCSV")
    public Job exportCSVJob() {

        return jobBuilderFactory.get("export-CSV")
            .incrementer(new RunIdIncrementer())
            .flow(databaseToCSV())
            .end()
            .build();

    }

}