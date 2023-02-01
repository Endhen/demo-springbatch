package demo.springbatch.reader;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import demo.springbatch.entity.Customer;

@Component
public class CustomerDbReader extends JdbcCursorItemReader<Customer> {
    
    private final String sqlSelector = "SELECT id,first_name, last_name, email FROM customer";

    public CustomerDbReader(DataSource dataSource) {
        this.setDataSource(dataSource);
        this.setSql(sqlSelector);
        this.setRowMapper(sqlRowMapper());
    }

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
                    "", 
                    "", 
                    "", 
                    ""
                );
            }
        };
    }

    
}
