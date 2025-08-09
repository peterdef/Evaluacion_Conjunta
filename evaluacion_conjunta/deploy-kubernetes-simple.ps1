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

# Paso 1: Crear namespace
Write-Host "`n📁 Paso 1: Creando namespace..." -ForegroundColor Cyan
Write-Host "📦 Aplicando namespace..." -ForegroundColor Yellow
kubectl apply -f "kubernetes/sistema-agricola/1-namespace.yml"
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ namespace aplicado exitosamente" -ForegroundColor Green
} else {
    Write-Host "❌ Error aplicando namespace" -ForegroundColor Red
}

# Paso 2: Desplegar bases de datos
Write-Host "`n🗄️  Paso 2: Desplegando bases de datos..." -ForegroundColor Cyan
Write-Host "📦 Aplicando bases de datos..." -ForegroundColor Yellow
kubectl apply -f "kubernetes/sistema-agricola/2-databases.yml"
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ bases de datos aplicadas exitosamente" -ForegroundColor Green
} else {
    Write-Host "❌ Error aplicando bases de datos" -ForegroundColor Red
}

# Esperar a que las bases de datos estén listas
Write-Host "⏳ Esperando a que las bases de datos estén listas..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Paso 3: Desplegar microservicios
Write-Host "`n🔧 Paso 3: Desplegando microservicios..." -ForegroundColor Cyan
Write-Host "📦 Aplicando microservicios..." -ForegroundColor Yellow
kubectl apply -f "kubernetes/sistema-agricola/3-microservicios.yml"
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ microservicios aplicados exitosamente" -ForegroundColor Green
} else {
    Write-Host "❌ Error aplicando microservicios" -ForegroundColor Red
}

# Paso 4: Configurar Ingress
Write-Host "`n🌐 Paso 4: Configurando Ingress..." -ForegroundColor Cyan
Write-Host "📦 Aplicando Ingress..." -ForegroundColor Yellow
kubectl apply -f "kubernetes/sistema-agricola/4-ingress.yml"
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Ingress aplicado exitosamente" -ForegroundColor Green
} else {
    Write-Host "❌ Error aplicando Ingress" -ForegroundColor Red
}

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

Write-Host "`n🎉 ¡Despliegue completado exitosamente!" -ForegroundColor Green
Write-Host "   El sistema agrícola está ahora ejecutándose en Kubernetes" -ForegroundColor Cyan
