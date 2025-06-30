# Step for running kubernetes cluster

Follow and run this command in order:

1) ```bash 
   minikube start 
   ```

2) ```bash 
   eval $(minikube docker-env) 
   ```
   To redirect docker commands to minikube docker

   Windows version:
   ```shell 
   minikube docker-env
   ```
3) After running minikube you do the build of all Dockerfile in services, for insert the image locally into minikube.

   Go to in the services folder and run:
   ```bash
   docker build -t magicevents/name-service:latest .
   ```
   Or build docker-compose (in same terminal where you start minukube)
4) Open /k8s and run: 
    ```bash 
    kubectl apply -f magicevents-namespace.yaml
   ```
   For create the namespace

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
   ```shell
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

For stopping minikube:
```bash
  minikube stop
```