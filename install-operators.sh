echo "----------Creating and switching namespace-----------"
kubectl create -f operator/create-namespace.yaml
kubectl config set-context --current --namespace=infinispan-namespace

echo "----------Installing Infinispan operator-----------"
kubectl apply -f operator/infinispan-operator-install.yaml

echo "----------Installing Kafka operator-----------"
kubectl apply -f operator/kafka-operator-install.yaml 