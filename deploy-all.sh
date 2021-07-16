echo "----------Creating Infinispan cluster and Infinispan Kogito Infra------------"
kubectl apply -f operator/create-infinispan-cluster.yaml
kubectl apply -f operator/kogito-infinispan-infra.yaml

echo "----------Creating Kafka cluster and Kafka Kogito Infra-----------"
kubectl apply -f operator/create-kafka-cluster.yaml
kubectl apply -f operator/kogito-kafka-infra.yaml

echo "----------Installing Data Index-----------"
kubectl apply -f operator/kogito-data-index.yaml

echo "--------Installing Kogito Task Console---------"
kubectl apply -f operator/kogito-task-console.yaml

echo "--------Installing Kogito Management Console---------"
kubectl apply -f operator/kogito-management-console.yaml

echo "--------Installing Kogito Jobs Service---------"
kubectl apply -f operator/kogito-job-service.yaml

echo "--------Installing Kogito Runtime Service---------"
kubectl apply -f operator/kogito-runtime-process.yaml

echo "--------Make Data Index and Service Approval Process accessible outside from cluster---------"
kubectl apply -f operator/ingress.yaml