Instalacia operatorov pre infinispan a kafka
--------------------------------------------
```./install-operators.sh```

Kogito Runtime Service
--------------------------------
Pre nasadenie Kogito Runtime Service je potrebne urobit build projektu, ktory chceme nasadit,
vytvorit docker image a pushnut ho do registry, napr. v pripade service-approval-service:

```
mvn clean package
docker build -f src/main/docker/quarkus-jvm.Dockerfile -t docker.demor.sk/kogito/service-approval-process:test .
docker push docker.demor.sk/kogito/service-approval-process:test
```

Nasadenie
---------
```./deploy-all.sh```

Pridanie ingress host do /etc/hosts
-----------------------------------
- prikazom ```minikube ip``` zistit IP adresu minikube
- pridat do /etc/hosts zaznam - hostname pouzijeme taky, aky sme si zvolili v ingress.yaml



Pridanie usera do task console
------------------------------
- otvorit task console v browsri
- kliknut vpravo hore na sipku, vybrat z menu Add new user, pridat usera userId: officer groups: officer
 a usera userId: manager groups: manager


Spustenie procesu
-----------------
```POST http://<service_approval_process_host>/approvals?businessKey=789```

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
```