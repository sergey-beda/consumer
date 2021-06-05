package myapp.service.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myapp.service.common.ConsumeService;
import myapp.service.common.RouteMessageService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientConsumeService implements ConsumeService {
    private final RouteMessageService routeMessageService;

    @Override
    @KafkaListener(
        id = "{$kafka.consumer.id}",
        topics = {
            "CLIENT_TOPIC"
        },
        containerFactory = CONSUMER_SINGLE_FACTORY
    )
    public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        routeMessageService.route(record, acknowledgment);
    }
}
