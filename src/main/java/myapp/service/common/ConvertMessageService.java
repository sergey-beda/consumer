package myapp.service.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import myapp.service.dto.AbstractSystemDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConvertMessageService {
    private final ObjectMapper objectMapper;

    public <T extends AbstractSystemDto> Optional<T> extractValue(ConsumerRecord<String, String> record, Class<T> dtoClass) {
        return extractValue(record.value(), dtoClass);
    }

    public <T extends AbstractSystemDto> Optional<T> extractValue(String record, Class<T> dtoClass) {
        try {
            return Optional.ofNullable(objectMapper.readValue(record, dtoClass));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
