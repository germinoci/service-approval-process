Instalacia operatorov pre infinispan a kafka
--------------------------------------------
```./install-operators.sh```

Nasadenie infinispan + kafka + data-index
-----------------------------------------
```./deploy-kogito-infra.sh```

Dalej je potrebne upravit URL adresy pre kafku, data-index a job service v kogito-runtime-process.yaml,
kogito-job-service.yaml,kogito-management-console.yaml a kogito-task-console.yaml podla IP adries, ktore zistime
prikazom kubectl get services.

Nasadenie kogito runtime service + management + task console
------------------------------------------------------------
Pre nasadenie Kogito Runtime Service je potrebne urobit build projektu, ktory chceme nasadit,
vytvorit docker image a pushnut ho do registry, napr. v pripade service-approval-service:

```
mvn clean package
docker build -f src/main/docker/quarkus-jvm.Dockerfile -t docker.demor.sk/kogito/service-approval-process:test .
docker push docker.demor.sk/kogito/service-approval-process:test
```

```./deploy-consoles.sh```

Po nasadeni je nutne upravit environment premennu pre ip adresu kogito servisu na aktualnu (pouziva ju task console)
v subore kogito-runtime-process.yaml:

```
apiVersion: app.kiegroup.org/v1beta1
kind: KogitoRuntime
metadata:
  name: service-approval-process
spec:
  replicas: 1
  image: docker.demor.sk/kogito/service-approval-process:test
  infra:
    - infinispan-infra
    - kafka-infra
  env:
    - name: QUARKUS_INFINISPAN_CLIENT_SASL_MECHANISM
      value: SCRAM-SHA-512
    - name: kogito.dataindex.http.url
      value: http://10.101.34.42:80
    - name: kogito.dataindex.ws.url 
      value: ws://10.101.34.42:80
    - name: kogito.service.url
      value: http://10.105.93.216:80   #tuto adresu treba upravit
    - name: kogito.jobs-service.url
      value: http://10.99.98.40:80
    - name: kafka.bootstrap.servers
      value: http://10.97.137.0:9092
```

Nasadenie Kogito Runtime Service
--------------------------------
kubectl apply -f operator/kogito-runtime-process.yaml

Pridanie usera do task console
------------------------------
- otvorit task console v browsri
- kliknut vpravo hore na sipku, vybrat z menu Add new user, pridat usera userId: officer groups: officer


Spustenie procesu
-----------------
```POST http://<service_approval_process_ip_address>/approvals?businessKey=789```

Request body:

```
{
    "isApproved": false,
    "isAssigned": false,
    "isPublished": false,
    "serviceData": {
        "serviceId": "789",
        "status": "STARTED"
    }
}