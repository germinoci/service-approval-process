echo "----------Creating Infinispan cluster and Infinispan Kogito Infra------------"
kubectl apply -f operator/create-infinispan-cluster.yaml
kubectl apply -f operator/kogito-infinispan-infra.yaml

echo "----------Creating Kafka cluster and Kafka Kogito Infra-----------"
kubectl apply -f operator/create-kafka-cluster.yaml
kubectl apply -f operator/kogito-kafka-infra.yaml

echo "----------Installing Data Index-----------"
kubectl apply -f operator/kogito-data-index.yaml