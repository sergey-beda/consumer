package myapp.service.common;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

public interface ConsumeService {
    String CONSUMER_SINGLE_FACTORY = "singleFactory";
    void consume(ConsumerRecord<String,String> record, Acknowledgment acknowledgment);
}
