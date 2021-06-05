package myapp.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myapp.service.Sleep;
import myapp.service.dto.AbstractSystemDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@Slf4j
@RequiredArgsConstructor
public class RouteMessageService {
    private final ConvertMessageService convertMessageService;
    private final ApplicationContext applicationContext;
    private final AnnotationService annotationService;
    private final CommonPostProcessService commonPostProcessService;
    private final ProcessErrorService processErrorService;

    public RouteMessageService() {
    }

    public void route(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        route(record);
        commonPostProcessService.postProcess(record, acknowledgment);
    }

    void route(ConsumerRecord<String,String> record) {
        try {
            Class<? extends AbstractSystemDto> dtoClass = annotationService.getDtoClass(record.topic());
            if (dtoClass == null) {
                throw new IllegalArgumentException("Не сопоставлено ни одного DTO класса для топика " + record.topic())
            }
            ProcessMessageService processMessageService = getProcessMessageService(dtoClass);
            if (processMessageService == null) {
                throw new IllegalArgumentException("Для класса " + dtoClass.getName() + " не определен сервис обработки" );
            }
            sleepIfNeed(dtoClass);
            convertMessageService.extractValue(record, dtoClass)
                .ifPresent(dto-> {
                    processMessageService.process(dto);
                });
            processErrorService.markAsProcessed(record);
        } catch (Exception e) {
            processErrorService.processError(record, e);
        }
    }

    public <T> ProcessMessageService getProcessMessageService(Class<T> dtoClass) {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(ProcessMessageService.class, dtoClass);
        String[] namesForType = applicationContext.getBeanNamesForType(resolvableType);
        if (isEmpty(namesForType)) {
            Class<?>[] interfaces = dtoClass.getInterfaces();
            if (!isEmpty(interfaces)) {
                return getProcessMessageService(interfaces[0]);
            }
            Class<?>[] superClass = dtoClass.getSuperclass();
            return  superClass != null ? getProcessMessageService(superClass) : null;
        }
    }

    public <T> void sleepIfNeed(Class<T> dtoClass) {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(ProcessMessageService.class, dtoClass);
        //для теста не нужно
        Optional.ofNullable(dtoClass.getAnnotation(Sleep.class))
            .map(Sleep::value)
            .ifPresent(sleep -> {
                try {
                    SECONDS.sleep(sleep);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
    }
}
