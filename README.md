# Прототип консьюмера для обработки сообщений, поступающих из кафки
##Постановка задачи: 
В production имеется работающая мастер система АС1, база данной которой постоянно изменяется в режиме реального времени. На замену мастер симтемы производится разработка другой АС2, которая также имеет свою базу данных(отличается логичческая/физическая модель БД).
Требуется произвести плавный переход от АС1 к АС2 в production. Для этих целей требуется реплицировать данные из БД устаревающей мастер системы в БД для новой АС в режиме реального времени.
##Архитектура:
На БД АС1 устаревающей мастер системы настраиваем логирование (suplemental логи) для каждой таблицы, требующейся для репликации.
Подключаем к устаревающей БД АС1 Oracle Golden Gate, который будет вычитывать логи из БД АС1 и продьюсить в Oracle Golden Gate for Big Data.
Подключаем Oracle Golden Gate for Big Data  и консьюмим данные из Oracle Golden Gate и после их фильтрации продьюсим в брокер кафки.
Настраиваем брокер кафки. Создаем топики,в которые будет продьюсить OGG for BD. Натсраиваем retry policy.
Подключаем наш консьюмер к брокеру кафки и к БД АС2. При обработке данных консьюмером производим сохранение информации в БД АС2.

##Пример настройки логов на БД АС1:
смотри по ссылке http://www.juliandyke.com/Research/GoldenGate/GoldenGateSupplementalLogging.php
##Пример конфигурации OGG:
смотри файл SETTINGS_OGG.md
##Пример конфигурации OGGforBD:
смотри файл SETTINGS_OGGforBD.md
##Пример создания топиков в брокере кафки:
смотри файл CREATE_TOPICS.md

#Далее изложена документация jhipster:

# consumer

This application was generated using JHipster 7.0.1, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v7.0.1](https://www.jhipster.tech/documentation-archive/v7.0.1).

This is a "microservice" application intended to be part of a microservice architecture, please refer to the [Doing microservices with JHipster][] page of the documentation for more information.
This application is configured for Service Discovery and Configuration with the JHipster-Registry. On launch, it will refuse to start if it is not able to connect to the JHipster-Registry at [http://localhost:8761](http://localhost:8761). For more information, read our documentation on [Service Discovery and Configuration with the JHipster-Registry][].

## Development

To start your application in the dev profile, run:

```
./mvnw
```

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Doing API-First development using openapi-generator

[OpenAPI-Generator]() is configured for this application. You can generate API code from the `src/main/resources/swagger/api.yml` definition file by running:

```bash
./mvnw generate-sources
```

Then implements the generated delegate classes with `@Service` classes.

To edit the `api.yml` definition file, you can use a tool such as [Swagger-Editor](). Start a local instance of the swagger-editor using docker by running: `docker-compose -f src/main/docker/swagger-editor.yml up -d`. The editor will then be reachable at [http://localhost:7742](http://localhost:7742).

Refer to [Doing API-First development][] for more details.

## Building for production

### Packaging as jar

To build the final jar and optimize the consumer application for production, run:

```
./mvnw -Pprod clean verify
```

To ensure everything worked, run:

```
java -jar target/*.jar
```

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Other tests

Performance tests are run by [Gatling][] and written in Scala. They're located in [src/test/gatling](src/test/gatling).

To use those tests, you must install Gatling from [https://gatling.io/](https://gatling.io/).

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a postgresql database in a docker container, run:

```
docker-compose -f src/main/docker/postgresql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/postgresql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./mvnw -Pprod verify jib:dockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 7.0.1 archive]: https://www.jhipster.tech/documentation-archive/v7.0.1
[doing microservices with jhipster]: https://www.jhipster.tech/documentation-archive/v7.0.1/microservices-architecture/
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v7.0.1/development/
[service discovery and configuration with the jhipster-registry]: https://www.jhipster.tech/documentation-archive/v7.0.1/microservices-architecture/#jhipster-registry
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v7.0.1/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v7.0.1/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v7.0.1/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v7.0.1/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v7.0.1/setting-up-ci/
[gatling]: https://gatling.io/
[openapi-generator]: https://openapi-generator.tech
[swagger-editor]: https://editor.swagger.io
[doing api-first development]: https://www.jhipster.tech/documentation-archive/v7.0.1/doing-api-first-development/
