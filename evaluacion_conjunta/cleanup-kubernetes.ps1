# Script para limpiar el Sistema Agrícola de Kubernetes
Write-Host "🧹 Iniciando limpieza del sistema agrícola..." -ForegroundColor Green

# Función para confirmar acción
function Confirm-Action {
    param([string]$Message)
    
    $response = Read-Host "$Message (S/N)"
    return $response -eq "S" -or $response -eq "s" -or $response -eq "Y" -or $response -eq "y"
}

# Verificar que kubectl esté disponible
try {
    $kubectlVersion = kubectl version --client --short
    Write-Host "✅ kubectl encontrado: $kubectlVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ kubectl no está disponible." -ForegroundColor Red
    exit 1
}

# Paso 1: Eliminar namespace completo
Write-Host "`n📁 Paso 1: Eliminando namespace sistema-agricola..." -ForegroundColor Cyan
if (Confirm-Action "¿Deseas eliminar el namespace sistema-agricola?") {
    kubectl delete namespace sistema-agricola
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Namespace eliminado exitosamente" -ForegroundColor Green
    } else {
        Write-Host "⚠️  El namespace ya no existe o hubo un error" -ForegroundColor Yellow
    }
} else {
    Write-Host "⏭️  Saltando eliminación del namespace" -ForegroundColor Yellow
}

# Paso 2: Eliminar imágenes Docker
Write-Host "`n🐳 Paso 2: Eliminando imágenes Docker..." -ForegroundColor Cyan
if (Confirm-Action "¿Deseas eliminar las imágenes Docker del sistema agrícola?") {
    $images = @(
        "sistema-agricola/eureka-server:latest",
        "sistema-agricola/api-gateway:latest",
        "sistema-agricola/agricultura:latest",
        "sistema-agricola/inventario:latest",
        "sistema-agricola/facturacion:latest",
        "sistema-agricola/notificaciones:latest"
    )
    
    foreach ($image in $images) {
        Write-Host "   Eliminando $image..." -ForegroundColor Gray
        docker rmi $image 2>$null
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "   ✅ $image eliminada" -ForegroundColor Green
        } else {
            Write-Host "   ⚠️  $image no existe o ya fue eliminada" -ForegroundColor Yellow
        }
    }
} else {
    Write-Host "⏭️  Saltando eliminación de imágenes Docker" -ForegroundColor Yellow
}

# Paso 3: Limpiar contenedores huérfanos
Write-Host "`n🧹 Paso 3: Limpiando contenedores huérfanos..." -ForegroundColor Cyan
if (Confirm-Action "¿Deseas eliminar contenedores huérfanos?") {
    docker container prune -f
    Write-Host "✅ Contenedores huérfanos eliminados" -ForegroundColor Green
} else {
    Write-Host "⏭️  Saltando limpieza de contenedores" -ForegroundColor Yellow
}

# Paso 4: Limpiar volúmenes no utilizados
Write-Host "`n💾 Paso 4: Limpiando volúmenes no utilizados..." -ForegroundColor Cyan
if (Confirm-Action "¿Deseas eliminar volúmenes no utilizados?") {
    docker volume prune -f
    Write-Host "✅ Volúmenes no utilizados eliminados" -ForegroundColor Green
} else {
    Write-Host "⏭️  Saltando limpieza de volúmenes" -ForegroundColor Yellow
}

# Paso 5: Limpiar redes no utilizadas
Write-Host "`n🌐 Paso 5: Limpiando redes no utilizadas..." -ForegroundColor Cyan
if (Confirm-Action "¿Deseas eliminar redes no utilizadas?") {
    docker network prune -f
    Write-Host "✅ Redes no utilizadas eliminadas" -ForegroundColor Green
} else {
    Write-Host "⏭️  Saltando limpieza de redes" -ForegroundColor Yellow
}

# Paso 6: Limpieza completa de Docker (opcional)
Write-Host "`n🧽 Paso 6: Limpieza completa de Docker..." -ForegroundColor Cyan
if (Confirm-Action "¿Deseas realizar una limpieza completa de Docker (elimina TODO)?") {
    Write-Host "⚠️  ADVERTENCIA: Esto eliminará TODAS las imágenes, contenedores, volúmenes y redes de Docker" -ForegroundColor Red
    if (Confirm-Action "¿Estás seguro de que quieres continuar?") {
        docker system prune -a --volumes -f
        Write-Host "✅ Limpieza completa de Docker realizada" -ForegroundColor Green
    } else {
        Write-Host "⏭️  Saltando limpieza completa" -ForegroundColor Yellow
    }
} else {
    Write-Host "⏭️  Saltando limpieza completa de Docker" -ForegroundColor Yellow
}

# Verificar estado final
Write-Host "`n📊 Estado final:" -ForegroundColor Green

# Verificar que el namespace fue eliminado
$namespaceExists = kubectl get namespace sistema-agricola 2>$null
if ($LASTEXITCODE -eq 0) {
    Write-Host "⚠️  El namespace sistema-agricola aún existe" -ForegroundColor Yellow
} else {
    Write-Host "✅ El namespace sistema-agricola fue eliminado" -ForegroundColor Green
}

# Mostrar imágenes Docker restantes
Write-Host "`n🐳 Imágenes Docker restantes:" -ForegroundColor Cyan
docker images | findstr sistema-agricola

# Mostrar contenedores activos
Write-Host "`n📦 Contenedores activos:" -ForegroundColor Cyan
docker ps

Write-Host "`n🎉 ¡Limpieza completada!" -ForegroundColor Green
Write-Host "   El sistema agrícola ha sido eliminado de Kubernetes" -ForegroundColor Cyan

