package demo.springbatch.writer;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import demo.springbatch.entity.Customer;

@Component
public class CustomerCsvWriter extends FlatFileItemWriter<Customer> {
    
    private final String[] columnNames = {"id", "firstName", "lastName", "email"};
    private final String output = "src/main/resources/output/customers.csv";
    
    public CustomerCsvWriter() {

        BeanWrapperFieldExtractor<Customer> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(columnNames);
        
        DelimitedLineAggregator<Customer> aggregator = new DelimitedLineAggregator<>();
        aggregator.setFieldExtractor(fieldExtractor);

        this.setResource(new FileSystemResource(output));
        this.setLineAggregator(aggregator);

    }
    
}
