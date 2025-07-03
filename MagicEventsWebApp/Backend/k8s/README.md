# Step for running kubernetes cluster

Follow and run this command in order:

1) ```bash 
   minikube start 
   ```
   ```bash
   minikube addons enable ingress
   ```

Be sure to edit the hosts file mapping localhost as follows

In Windows:
```
  127.0.0.1 magicevents.local
```

In MacOS or Linux:
```
  magicevents.192.168.49.2.nip.io magicevents.local
```

For modify hosts file in MacOS or Linux run:
```bash
  sudo nano /etc/hosts
```
In Windows open file in path: `C:\Windows\System32\drivers\etc\hosts`.

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

8) Open the dashboard with this command:
   ```bash
   minikube dashboard
   ```

In windows make sure you do in a terminal running as administrator:
```shell
minikube tunnel
```
And leave the terminal open. Test connection with this command:
```shell
ping magicevents.local
```

For stopping minikube:
```bash
  minikube stop
```