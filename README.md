# service-approval-process project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .


### Use the application

Examine OpenAPI via swagger UI at [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/service-approval-process-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## Provided examples

### RESTEasy JAX-RS example

REST is easy peasy with this Hello World RESTEasy resource.

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)


### Run complete stack with keycloak auth in docker containers - docker-compose

`mvn clean compile quarkus:dev`

`cd docker-compose `

`./startServices.sh`

### Keycloak

It will install the *Kogito Realm* that comes with a predefined set of users:
| Login            | Password   | Roles                         |
| ---------------- | ---------- | ----------------------------- |
|    admin         |   admin    | *admin*, *manager*, *officer* |
|    referent      |   referent | *officer*                     |
|    manazer       |   manazer  | *manager*                     |

### Management console / task console / graphql - docker compose
# Link Server Managenemt console
http://172.17.0.1:11222/console/

# Link graphiql
http://localhost:8180/graphiql/

# Link Task console
http://localhost:8380/TaskInbox/

# Link Managenemt console
http://localhost:8280/ProcessInstances/

# Link Swagger/ Open api
http://localhost:8080/q/swagger-ui/


### Management console - local
https://docs.jboss.org/kogito/release/latest/html_single/#proc-management-console-using_kogito-developing-process-services
`java -Dquarkus.http.port=8280 -jar management-console-1.7.0.Final-runner.jar`
http://localhost:8280/

### Task console - local
https://docs.jboss.org/kogito/release/latest/html_single/#proc-task-console-using_kogito-developing-process-services
`java -Dquarkus.http.port=8380 -jar task-console-1.7.0.Final-runner.jar`

### Infinispan server - local
https://infinispan.org/get-started/
`docker run -it -p 11222:11222 -e USER="admin" -e PASS="password" quay.io/infinispan/server:12.1`
http://localhost:11222/

### Infinispan client - local
https://docs.jboss.org/kogito/release/latest/html_single/#proc-data-index-service-using_kogito-configuring
`java -Dquarkus.infinispan-client.auth-username=admin -Dquarkus.infinispan-client.auth-password=password -jar data-index-service-infinispan-1.7.0.Final-runner.jar`

### Job service client - local
https://docs.jboss.org/kogito/release/latest/html_single/#proc-jobs-service-using_kogito-configuring
`java -jar jobs-service-common-1.7.0.Final-runner.jar`
