vytvorenie namespace
--------------------
kubectl create -f create-namespace.yaml

prepnutie sa na namespace
-------------------------
kubectl config set-context --current --namespace=infinispan-namespace


instalacia infinispan operatora
-------------------------------
zdroj yaml suboru: https://raw.githubusercontent.com/infinispan/infinispan-operator/2.1.x/deploy/operator-install.yaml
kubectl apply -f infinispan-operator-install.yaml


spustenia clustra s dvomi instanciami
-------------------------------------
zdroj yaml suboru: https://raw.githubusercontent.com/infinispan/infinispan-operator/2.1.x/deploy/cr/minimal/cr_minimal.yaml
kubectl apply -f create-infinispan-cluster.yaml


nastavenie kogito infra s infinispanom
--------------------------------------
kubectl apply -f operator/kogito-infinispan-infra.yaml

build + push docker image
-------------------------
mvn clean package
docker build -f src/main/docker/quarkus-jvm.Dockerfile -t docker.demor.sk/kogito/service-approval-process:latest .
docker push docker.demor.sk/kogito/service-approval-process:latest

spustenie kogito runtime s infinispanom
---------------------------------------
kubectl apply -f kogito-runtime-process.yaml
kubectl expose deployment service-approval-process --type=LoadBalancer --name=service-approval-process-exposed




instalacia kafka operatora
--------------------------
zdroj yaml suboru: https://strimzi.io/install/latest?namespace=infinispan-namespace s parametrizovanym namespacom
kubectl apply -f kafka-operator-install.yaml 

spustenie clustra kafka + zookeeper
-----------------------------------
zdroj yaml suboru: https://strimzi.io/examples/latest/kafka/kafka-persistent-single.yaml
kubectl apply -f create-kafka-cluster.yaml

nastavenie kogito infra s kafkom
--------------------------------
kubectl apply -f kogito-kafka-infra.yaml

instalacia data index
---------------------
kubectl apply -f operator/kogito-data-index.yaml

vytvorenie kogito infra pre data index
--------------------------------------
kubectl apply -f operator/kogito-data-index-infra.yaml


instalacia kogito task console
------------------------------
kubectl apply -f operator/kogito-task-console.yaml
kubectl expose deployment task-console --type=LoadBalancer --name=task-console-exposed




