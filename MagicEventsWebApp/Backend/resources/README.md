# How to run minikube:

Ufficial documentation you follow: [Getting Started](https://minikube.sigs.k8s.io/docs/start/?arch=%2Fwindows%2Fx86-64%2Fstable%2F.exe+download);

Now you can enable ingress with this command:
```bash
  minikube addons enable ingress
```
Go in the minikube dashboard with this command:
```bash
  minikube dashboard
```
Be sure to edit the hosts file if your application does not have a domain, mapping localhost as follows:
```
  127.0.0.1 magicevents.com
  127.0.0.1 api.magicevents.com
```
For modify hosts file in MacOS or Linux run:
```bash
  sudo nano /etc/hosts
```
In Windows open file in path: `C:\Windows\System32\drivers\etc\hosts`.

Now you can insert in the `Service>Ingresses` section (in minikube dashboard) the configuration in file ingress.yaml (`Resources\service\networking\ingress.yaml`).