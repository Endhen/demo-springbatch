package demo.springbatch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import demo.springbatch.entity.Customer;

@Component
public class FilterProcessor implements ItemProcessor<Customer,Customer> {

    @Override
    public Customer process(@NonNull Customer customer) throws Exception {

        return customer.getCountry().equals("United States") ? customer : null;
        
    }
}
