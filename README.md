# Jenkins Shared Library for k3s CI/CD

This library gives you reusable steps to:
- Run jobs on a Kubernetes **pod agent** with multiple containers (python, kaniko, kubectl)
- Install Python deps + run tests
- Build & push images to **GHCR**
- Deploy to your k3s cluster with `kubectl`
- **Auto-tag images** using branch + commit (and `latest` for `main`)
  
## Requirements
The below is the config used by my setup, adjust as necessary.

- Jenkins with **Kubernetes** plugin, controller in `infra` namespace
- A Kubernetes cloud configured with:
- Namespace: `apps`
- Service Account: `jenkins-deployer`
- Jenkins URL: `http://jenkins.infra.svc.cluster.local:8080`
- **Use WebSocket** enabled
- In `apps` namespace: a docker-registry secret for GHCR named `reg-creds` (used by Deployment pulls). Kaniko will push using its own credentials if provided via environment or mounted docker config; many setups just push anonymously to public GHCR repos.

> Tip: keep the Pod Template **in the form UI**, not the raw YAML box, so the plugin injects JNLP args correctly.

## Registering the library in Jenkins
**Manage Jenkins → System → Global Pipeline Libraries**
- Name: `lib` (you can pick another)
- Default version: `main` (or a tag)
- Retrieval method: **Modern SCM → Git** → point to your GitHub repo
- Load implicitly: (optional) checked
