package demo.springbatch.importCSVJob;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import demo.springbatch.entity.Customer;

@Component
public class CustomerCsvReader extends FlatFileItemReader<Customer> {

    private final String readerName = "customer-csv-reader";
    private final String csvSoruce = "src/main/resources/customers.csv";
    private final String[] columnNames = { "id", "firstName", "lastName", "email", "gender", "contactNo", "country", "birthday" };
    
    public CustomerCsvReader() {

        this.setResource(new FileSystemResource(csvSoruce));
        this.setLineMapper(lineMapper(columnNames));
        this.setName(readerName);
        this.setLinesToSkip(1);
    }

    private LineMapper<Customer> lineMapper(String... columnNames) {

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(columnNames);

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

}
