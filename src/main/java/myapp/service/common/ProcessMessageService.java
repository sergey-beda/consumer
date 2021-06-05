package myapp.service.common;

@FunctionalInterface
public interface ProcessMessageService<T> {
    void process(T dto);
}
