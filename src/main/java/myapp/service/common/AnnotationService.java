package myapp.service.common;

import myapp.service.dto.AbstractSystemDto;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class AnnotationService {
    private static final String PACKAGES = "myapp";
    private static final String MULTIPLE_TOPICS_MATCH = "Топику %s соответствует более одного класса";
    private static final String TOPIC_SHOULD_BE_NOT_NULL = "Топик не должен быть null";
    private static final String WRONG_ANNOTATION_USAGE = "Аннотация @KafkaTopic в классе %s использована не по назначению";

    private Map<String, Class<? extends AbstractSystemDto>> topicsMap;

    @PostConstruct
    private void init() {
        topicsMap = new HashMap<>();
        Reflections reflections = new Reflections(PACKAGES);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(KafkaTopic.class);
        classes.forEach(clazz -> {
            KafkaTopic annotation = clazz.getAnnotation(KafkaTopic.class);
            if (annotation == null) {
                return;
            }
            if (!AbstractSystemDto.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException(String.format(WRONG_ANNOTATION_USAGE, clazz));
            }
            String topic = annotation.value();
            if (topicsMap.containsKey(topic)) {
                throw new NonUniqueKafkaTopicException(String.format(MULTIPLE_TOPICS_MATCH, topic));
            }
            topicsMap.put(topic, clazz.asSubclass(AbstractSystemDto.class));
        });
    }

    public Class<? extends AbstractSystemDto> getDtoClass(String topic) {
        if (topic == null) {
            throw new IllegalArgumentException(TOPIC_SHOULD_BE_NOT_NULL);
        }
        return  topicsMap.get(topic);
    }

}
