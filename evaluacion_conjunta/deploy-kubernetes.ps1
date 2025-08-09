# Script para desplegar Sistema Agrícola en Kubernetes
Write-Host "🚀 Iniciando despliegue en Kubernetes..." -ForegroundColor Green

# Verificar que kubectl esté disponible
try {
    $kubectlVersion = kubectl version --client --short
    Write-Host "✅ kubectl encontrado: $kubectlVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ kubectl no está disponible. Por favor instálalo primero." -ForegroundColor Red
    exit 1
}

# Verificar que el cluster esté funcionando
Write-Host "🔍 Verificando cluster de Kubernetes..." -ForegroundColor Yellow
$clusterStatus = kubectl cluster-info 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ No se puede conectar al cluster de Kubernetes" -ForegroundColor Red
    Write-Host "   Asegúrate de que minikube esté ejecutándose o tengas acceso al cluster" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ Cluster de Kubernetes disponible" -ForegroundColor Green

# Función para aplicar archivos de Kubernetes
function Apply-KubernetesResources {
    param(
        [string]$FilePath,
        [string]$Description
    )
    
    Write-Host "📦 Aplicando $Description..." -ForegroundColor Yellow
    kubectl apply -f $FilePath
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Error aplicando $Description" -ForegroundColor Red
        return $false
    }
    
    Write-Host "✅ $Description aplicado exitosamente" -ForegroundColor Green
    return $true
}

# Función para esperar a que los pods estén listos
function Wait-ForPods {
    param(
        [string]$Namespace,
        [string]$LabelSelector
    )
    
    Write-Host "⏳ Esperando a que los pods estén listos..." -ForegroundColor Yellow
    kubectl wait --for=condition=ready pod -l $LabelSelector -n $Namespace --timeout=300s
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "⚠️  Algunos pods no están listos después de 5 minutos" -ForegroundColor Yellow
        return $false
    }
    
    Write-Host "✅ Todos los pods están listos" -ForegroundColor Green
    return $true
}

# Paso 1: Crear namespace
Write-Host "`n📁 Paso 1: Creando namespace..." -ForegroundColor Cyan
Apply-KubernetesResources -FilePath "kubernetes/sistema-agricola/1-namespace.yml" -Description "namespace"

# Paso 2: Desplegar bases de datos
Write-Host "`n🗄️  Paso 2: Desplegando bases de datos..." -ForegroundColor Cyan
Apply-KubernetesResources -FilePath "kubernetes/sistema-agricola/2-databases.yml" -Description "bases de datos"

# Esperar a que las bases de datos estén listas
Write-Host "⏳ Esperando a que las bases de datos estén listas..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Paso 3: Desplegar microservicios
Write-Host "`n🔧 Paso 3: Desplegando microservicios..." -ForegroundColor Cyan
Apply-KubernetesResources -FilePath "kubernetes/sistema-agricola/3-microservicios.yml" -Description "microservicios"

# Paso 4: Configurar Ingress
Write-Host "`n🌐 Paso 4: Configurando Ingress..." -ForegroundColor Cyan
Apply-KubernetesResources -FilePath "kubernetes/sistema-agricola/4-ingress.yml" -Description "Ingress"

# Esperar a que todos los servicios estén listos
Write-Host "`n⏳ Esperando a que todos los servicios estén listos..." -ForegroundColor Yellow
Start-Sleep -Seconds 60

# Mostrar estado de los pods
Write-Host "`n📊 Estado de los pods:" -ForegroundColor Green
kubectl get pods -n sistema-agricola

# Mostrar servicios
Write-Host "`n🌐 Servicios expuestos:" -ForegroundColor Green
kubectl get services -n sistema-agricola

# Mostrar información de acceso
Write-Host "`n🔗 Información de acceso:" -ForegroundColor Cyan
Write-Host "   Eureka Dashboard: http://localhost:30761" -ForegroundColor Yellow
Write-Host "   API Gateway: http://localhost:30084" -ForegroundColor Yellow
Write-Host "   RabbitMQ Management: http://localhost:15672 (guest/guest)" -ForegroundColor Yellow

# Agregar entradas al archivo hosts (opcional)
Write-Host "`n📝 Para acceder por nombres de dominio, agrega estas líneas a tu archivo hosts:" -ForegroundColor Cyan
Write-Host "   $(minikube ip) eureka.sistema-agricola.local" -ForegroundColor Yellow
Write-Host "   $(minikube ip) api.sistema-agricola.local" -ForegroundColor Yellow
Write-Host "   $(minikube ip) agricultura.sistema-agricola.local" -ForegroundColor Yellow
Write-Host "   $(minikube ip) inventario.sistema-agricola.local" -ForegroundColor Yellow
Write-Host "   $(minikube ip) facturacion.sistema-agricola.local" -ForegroundColor Yellow
Write-Host "   $(minikube ip) notificaciones.sistema-agricola.local" -ForegroundColor Yellow
Write-Host "   $(minikube ip) rabbitmq.sistema-agricola.local" -ForegroundColor Yellow

Write-Host "`n🎉 ¡Despliegue completado exitosamente!" -ForegroundColor Green
Write-Host "   El sistema agrícola está ahora ejecutándose en Kubernetes" -ForegroundColor Cyan

