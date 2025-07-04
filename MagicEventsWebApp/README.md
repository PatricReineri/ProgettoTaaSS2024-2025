# Step for running kubernetes cluster

The preliminary step is to install [mkcert](https://github.com/FiloSottile/mkcert) and run:
```bash 
mkcert -install
```

Follow and run this command in order:

1) ```bash 
   minikube start 
   ```
   ```bash
   minikube addons enable ingress
   ```

Be sure to edit the hosts file mapping localhost as follows.

In Windows:
```
  127.0.0.1 magicevents.com
```

In MacOS or Linux:
```
  magicevents.[minikube ip].nip.io magicevents.com
```

For modify hosts file in MacOS or Linux run:
```bash
  sudo nano /etc/hosts
```
In Windows open file in path: `C:\Windows\System32\drivers\etc\hosts`.

2) ```bash 
   eval $(minikube docker-env) 
   ```
   To redirect docker commands to minikube docker.

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
   
   In folder MagicEventsWebApp is present a script for build all Dockerfile services.

   Check that the images have been created correctly:
   ```bash
   docker image ls
   ```
4) Now go in k8s folder (run ``` cd Backend/k8s```) and run: 
    ```bash 
    kubectl apply -f magicevents-namespace.yaml
   ```
   For create the namespace.

5) Always in k8s folder run:
    ```bash
    kubectl apply -f .
    ```
6) Go in tls folder (run ``` cd tls```) and run:
    ```bash
   kubectl -n magicevents create secret tls magicevents-tls \
   --cert=magicevents.local.pem \
   --key=magicevents.local-key.pem
    ```
   For create certificate secret in kubernetes.

   Windows version:
   ```shell
   kubectl -n magicevents create secret tls magicevents-tls ` --cert=magicevents.com.pem ` --key=magicevents.com-key.pem
   ```
7) Go to ingress folder (run ``` cd ../ingress```)  and run:
    ```bash
    kubectl apply -f .
    ```

Open the dashboard with this command:
```bash
minikube dashboard
```

In windows make sure you do in a terminal running as administrator:
```shell
minikube tunnel
```
And leave the terminal open. Test connection with this command:
```shell
ping magicevents.com
```

For stopping minikube:
```bash
  minikube stop
```