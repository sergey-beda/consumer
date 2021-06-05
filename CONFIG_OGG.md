#Конфигурация OGG
#Для работы OGG требуется создание файковых таблиц В БД АС1 для экстракта:
```
CREATE TABLE CLIENT_GG
(
    ID          NUMBER(19,0) primary key,
    NAME        VARCHAR2(255)
)
```

#Для работы экстракта с фейковыми таблицами требуется произвести генерацию через defgen. Для этого нужен конфиг defgen.prm
```
defsfile ./dirdef/src.def, purge

userid ggate_user, password ggate_user

table SCHEMA.CLIENT_GG;
```

Для выполнения генерации требуется в директории gg/dirprm внести изменения в файл defgen.prm, очистить дирректорию gg/dirsql, в директории gg выполнить команду ./defgen paramfile dirprm/defgen.prm, сформированный src.def в каталоге gg/dirsql перенести в gg/dirdef

#Для уменьшения размера конфигов и улучшения читабельности сделаем макрос для схемы
это файл define_schema.mac
```
MACRO #schem
BEGIN
    NAME_SCHEMA
END;
```

#Конфиг экстракта
```
EXTRACT EXT1
userid ggate_user, password ggate_user
EXTTRAIL ./dirdat/lt
TRANLOGOPTIONS EXCLUDETAG 61
TRANLOGOPTIONS EXCLUDEUSER USERAS1
TARGETDEFS ./direct/src.def

INCLUDE ./dirprm/define_schema.mac
MACRO #map_col
PARAMS (#colname)
BEGIN
    #colname = @IF ( @COLTEST(#colname, MISSING, INVALID), @BEFORE(#colname), #colname)
END;
SOURCECATALOG #schem
TABLE #schem.CLIENT,
TARGET #schem.CLIENT_GG,
TARGETDEF #schem.CLIENT_GG,
KEYCOLS(ID),
COLMAP()
id = id,
\#mapcol(name),
\inn = @COLSTAT(NULL)
);
#Также можно писать фильтры, sqlexec: например FILTER(@COLTEST(@GETVAL(reftable.client_id), MISSING, INVALID, NULL))  и SQLEXEC (id reftable, QUERY "select ref_id from #schem.REFTABLE where ref_id = :refid", PARAMS (refid=ID), BEFOREFILTER) 

#Конфиг pump
EXTRACT EXTDP1
RMHOST 10.53.25.30, MGRPORT 7813
RMTRAIL .dirdat/rt
REPLACEBADCHAR SUBSTITUTE BB
INCLUDE ./dirprm/define_schema.mac

TABLE #schem.CLIENT_GG, KEYCOLS(ID);
#Также можно писать фильтры, sqlexec: например FILTER(@COLTEST(@GETVAL(reftable.client_id), MISSING, INVALID, NULL))  и SQLEXEC (id reftable, QUERY "select ref_id from #schem.REFTABLE where ref_id = :refid", PARAMS (refid=ID), BEFOREFILTER) 
```

#Для работы с экстрактом/пампом имеются команды:
```
stop extract ext1
unregister extract ext1 database
regiter extract ext1 database
start extract ext1
```
