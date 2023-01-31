package demo.springbatch.writer;

import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.stereotype.Component;

import demo.springbatch.entity.Customer;
import demo.springbatch.repository.CustomerRepository;

@Component
public class CustomerDbWriter extends RepositoryItemWriter<Customer> {

    public CustomerDbWriter(CustomerRepository customerRepository) {
        this.setRepository(customerRepository);
        this.setMethodName("save");
    }

}
