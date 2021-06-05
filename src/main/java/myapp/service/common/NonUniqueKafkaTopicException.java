package myapp.service.common;

public class NonUniqueKafkaTopicException extends RuntimeException {
    NonUniqueKafkaTopicException() {
        super();
    }

    NonUniqueKafkaTopicException(String s) {
        super(s);
    }
}
