package myapp.service.common;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ProcessErrorService {
    void processError(ConsumerRecord<String,String> record, Throwable e);
    void markAsProcessed(ConsumerRecord<String,String> record);
}
