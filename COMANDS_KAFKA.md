# Команды для брокера кафки
## Создание топика:
```
/opt/Apache/kafka/bin/kafka-topics --create --zookeeper=10.53.37.84:2181 --replication-factor 2 --partitions 36 --topic CLIENT_TOPIC --cleanup.policy=delete;
```
## Удаление топика:
```
/opt/Apache/kafka/bin/kafka-topics --zookeeper=10.53.37.84:2181 --delete --topic CLIENT_TOPIC;
```
## Для визуальной работы с очередями брокера кафки имеется утилита kafkatool
