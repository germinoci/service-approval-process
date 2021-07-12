vytvorenie namespace
--------------------
kubectl create -f operator/create-namespace.yaml

prepnutie sa na namespace
-------------------------
kubectl config set-context --current --namespace=infinispan-namespace


instalacia infinispan operatora
-------------------------------
zdroj yaml suboru: https://raw.githubusercontent.com/infinispan/infinispan-operator/2.1.x/deploy/operator-install.yaml
kubectl apply -f operator/infinispan-operator-install.yaml


spustenia infinispan clustra
-------------------------------------
zdroj yaml suboru: https://raw.githubusercontent.com/infinispan/infinispan-operator/2.1.x/deploy/cr/minimal/cr_minimal.yaml
kubectl apply -f operator/create-infinispan-cluster.yaml


vytvorenie kogito infra pre infinispan
--------------------------------------
kubectl apply -f operator/kogito-infinispan-infra.yaml

build + push docker image
-------------------------
mvn clean package
docker build -f src/main/docker/quarkus-jvm.Dockerfile -t docker.demor.sk/kogito/service-approval-process:test .
docker push docker.demor.sk/kogito/service-approval-process:test

instalacia kafka operatora
--------------------------
zdroj yaml suboru: https://strimzi.io/install/latest?namespace=infinispan-namespace s parametrizovanym namespacom
kubectl apply -f operator/kafka-operator-install.yaml 

spustenie clustra kafka + zookeeper
-----------------------------------
zdroj yaml suboru: https://strimzi.io/examples/latest/kafka/kafka-persistent-single.yaml
kubectl apply -f operator/create-kafka-cluster.yaml

vytvorenie kogito infra pre kafku
---------------------------------
kubectl apply -f operator/kogito-kafka-infra.yaml

instalacia data index
---------------------
kubectl apply -f operator/kogito-data-index.yaml

instalacia kogito task console
------------------------------
v yamli treba nastavit spravnu URL data indexu
kubectl apply -f operator/kogito-task-console.yaml

spustenie kogito runtime s infinispanom
---------------------------------------
v yamli treba nastavit spravne URL pre kafku a data-index
kubectl apply -f operator/kogito-runtime-process.yaml

pridanie usera do task console
------------------------------
otvorit task console v browsri
kliknut vpravo hore na sipku, vybrat z menu Add new user, pridat usera userId: officer groups: officer


spustenie procesu
-----------------
POST http://<service_approval_process_ip_address>/approvals?businessKey=123
Request body:
{
  "isApproved": false,
  "isAssigned": false,
  "isPublished": false
}

posunutie procesu do dalsej fazy
--------------------------------
otvorit v browsri graphql konzolu data indexu
spustit query, ktorou zistime id user tasku:
{
  UserTaskInstances {
    id
  }
}

GET http://<service_approval_process_ip_address>/approvals - ziskanie id instancii procesu

POST http://<service_approval_process_ip_address>/approvals/<approval_id>/serviceSelectionAndAssessment/<task_id>/phases/complete?group=officer&user=officer
Request body:
{
  "isFromUserApproved": true,
  "service": {
    "serviceId": "123",
    "status": "APPROVED"
  }
}