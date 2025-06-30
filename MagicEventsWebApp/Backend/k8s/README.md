# Step for running kubernetes cluster

Before running minikube make sure you do the build of all Dockerfile in services, for insert the image locally into minikube.

Follow and run this command in order:

1) ```bash 
   minikube start 
   ```

2) ```bash 
   eval $(minikube docker-env) 
   ```
    To redirect docker commands to minikube docker

In Windows for step 1-2 run:
```bash 
  minikube start --driver=docker
```
After is started:
```bash 
  minikube docker-env
```

4) Open /k8s and run: 
    ```bash 
    kubectl apply -f magicevents-namespace.yaml
   ```
   Create the namespace

5) Always in /k8s run:
    ```bash
    kubectl apply -f .
    ```

6) Always in /k8s run:
    ```bash
    kubectl create secret tls magicevents-tls \
    --cert=tls/tls.crt \
    --key=tls/tls.key \
    -n magicevents
    secret/magicevents-tls created
    ```
    For create certificate secret in kubernetes

Windows version:
```bash
kubectl create secret tls magicevents-tls `--cert=tls\tls.crt `--key=tls\tls.key `-n magicevents
```

7) Open ingress folder and run:
    ```bash
    kubectl apply -f .
    ```
8) Open the dashboard with this command:
```bash
  minikube dashboard
```