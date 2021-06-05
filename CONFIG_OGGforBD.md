#Конфигурация OGG for BD

#Конфиг репликата
```
REPLICAT rconf
TARGETDB LIBFILE libggjava.so SET property=dirprm/kafkaconnect.props
REPORTCOUNT EVERY 1 MINUTES, RATE
REPLACEBADCHAR SUBSTITUTE CC
GROUPTRANSOPS 100

INCLUDE ./dirprm/define_schema.mac
MAP #schem().CLIENT_GG, TARGET #schem.CLIENT_GG, KEYCOLS(ID);
#Также можно писать фильтры: например FILTER(@COLTEST(@GETVAL(reftable.client_id), MISSING, INVALID, NULL))   
```

#Для работы с репликатом имеются команды:
```
stop replicat ext1
unregister replicat ext1 database
regiter replicat ext1 database
start replicat ext1
```

#Для подключения репликата к брокеру кафки имеются kafkaconnect.properties
