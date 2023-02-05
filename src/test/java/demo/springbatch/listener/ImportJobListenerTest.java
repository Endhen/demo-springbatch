package demo.springbatch.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobExecution;

@ExtendWith(MockitoExtension.class)
public class ImportJobListenerTest {

    @Mock
    private JobExecution jobExecution;

    @Test
    void testAfterJob() {
    }

    @Test
    void testBeforeJob() {

    }

    @Test
    void testGetOrderId() {

    }

}
