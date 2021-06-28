vytvorenie namespace
--------------------
kubectl create -f create-namespace.yaml

prepnutie sa na namespace
-------------------------
kubectl config set-context --current --namespace=infinispan-namespace


instalacia infinispan operatora
-------------------------------
kubectl apply -f infinispan-operator-install.yaml


spustenia clustra s dvomi instanciami
-------------------------------------
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


