package myapp.service.common;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

public class CommonPostProcessService {
    public void postProcess(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();
    }
}
