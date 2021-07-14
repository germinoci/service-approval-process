echo "--------Installing Kogito Task Console---------"
kubectl apply -f operator/kogito-task-console.yaml

echo "--------Installing Kogito Management Console---------"
kubectl apply -f operator/kogito-management-console.yaml

echo "--------Installing Kogito Jobs Service---------"
kubectl apply -f operator/kogito-job-service.yaml