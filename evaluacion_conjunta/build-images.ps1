# Script para construir imágenes Docker del Sistema Agrícola
Write-Host "🚀 Iniciando construcción de imágenes Docker..." -ForegroundColor Green

# Función para compilar y construir imagen
function Build-Microservice {
    param(
        [string]$ServiceName,
        [string]$ServicePath,
        [string]$ImageName,
        [string]$Port
    )
    
    Write-Host "📦 Compilando $ServiceName..." -ForegroundColor Yellow
    Set-Location $ServicePath
    
    # Compilar con Maven
    Write-Host "   Compilando con Maven..." -ForegroundColor Cyan
    mvn clean package -DskipTests
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Error compilando $ServiceName" -ForegroundColor Red
        return $false
    }
    
    # Construir imagen Docker
    Write-Host "   Construyendo imagen Docker..." -ForegroundColor Cyan
    docker build -t sistema-agricola/$ImageName:latest .
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Error construyendo imagen para $ServiceName" -ForegroundColor Red
        return $false
    }
    
    Write-Host "✅ $ServiceName construido exitosamente" -ForegroundColor Green
    return $true
}

# Volver al directorio raíz
Set-Location $PSScriptRoot

# Lista de microservicios a construir
$services = @(
    @{
        Name = "Eureka Server"
        Path = "ms-eureka-server"
        Image = "eureka-server"
        Port = "8761"
    },
    @{
        Name = "API Gateway"
        Path = "ms-api-gateway"
        Image = "api-gateway"
        Port = "8084"
    },
    @{
        Name = "Agricultura"
        Path = "api-publicaciones"
        Image = "agricultura"
        Port = "8080"
    },
    @{
        Name = "Inventario"
        Path = "ms-inventario"
        Image = "inventario"
        Port = "8081"
    },
    @{
        Name = "Facturación"
        Path = "ms-facturacion"
        Image = "facturacion"
        Port = "8082"
    },
    @{
        Name = "Notificaciones"
        Path = "ms-notificaciones"
        Image = "notificaciones"
        Port = "8083"
    }
)

$successCount = 0
$totalServices = $services.Count

foreach ($service in $services) {
    if (Build-Microservice -ServiceName $service.Name -ServicePath $service.Path -ImageName $service.Image -Port $service.Port) {
        $successCount++
    }
}

Write-Host "`n📊 Resumen de construcción:" -ForegroundColor Green
Write-Host "   ✅ Servicios construidos exitosamente: $successCount/$totalServices" -ForegroundColor Green

if ($successCount -eq $totalServices) {
    Write-Host "`n🎉 ¡Todas las imágenes construidas exitosamente!" -ForegroundColor Green
    Write-Host "   Ahora puedes proceder con el despliegue en Kubernetes" -ForegroundColor Cyan
} else {
    Write-Host "`n⚠️  Algunos servicios fallaron en la construcción" -ForegroundColor Yellow
    Write-Host "   Revisa los errores antes de continuar" -ForegroundColor Red
}
