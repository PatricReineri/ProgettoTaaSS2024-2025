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
   And after:
   ```shell 
   minikube -p minikube docker-env --shell powershell | Invoke-Expression
   ```
3) After running minikube you do the build of all Dockerfile in services, for insert the image locally into minikube.

   Go to in the services folder and run:
   ```bash
   docker build -t magicevents/name-service:latest .
   ```
   Note: you have to do these operations in same terminal where you start minukube.
   
   In folder Backend (run ``` cd ..```) is present a script for build all Dockerfile services.

   Check that the images have been created correctly:
   ```bash
   docker image ls
   ```
4) Now go in k8s folder (run ``` cd k8s```) and run: 
    ```bash 
    kubectl apply -f magicevents-namespace.yaml
   ```
   For create the namespace

5) Always in k8s folder run:
    ```bash
    kubectl apply -f .
    ```
6) Always in k8s folder run:
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
7) Go to ingress folder (run ``` cd ingress```)  and run:
    ```bash
    kubectl apply -f .
    ```
   ```bash
   minikube addons enable ingress
   ```
8) Open the dashboard with this command:
   ```bash
   minikube dashboard
   ```

For stopping minikube:
```bash
  minikube stop
```