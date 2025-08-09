# Script para construir imÃ¡genes Docker del Sistema AgrÃ­cola
Write-Host "ðŸš€ Iniciando construcciÃ³n de imÃ¡genes Docker..." -ForegroundColor Green

# FunciÃ³n para compilar y construir imagen
function Build-Microservice {
    param(
        [string]$ServiceName,
        [string]$ServicePath,
        [string]$ImageName,
        [string]$Port
    )
    
    Write-Host "ðŸ“¦ Compilando $ServiceName..." -ForegroundColor Yellow
    Set-Location $ServicePath
    
    # Compilar con Maven
    Write-Host "   Compilando con Maven..." -ForegroundColor Cyan
    mvn clean package -DskipTests
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "âŒ Error compilando $ServiceName" -ForegroundColor Red
        return $false
    }
    
    # Construir imagen Docker
    Write-Host "   Construyendo imagen Docker..." -ForegroundColor Cyan
    docker build -t sistema-agricola/$ImageName:latest .
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "âŒ Error construyendo imagen para $ServiceName" -ForegroundColor Red
        return $false
    }
    
    Write-Host "âœ… $ServiceName construido exitosamente" -ForegroundColor Green
    return $true
}

# Volver al directorio raÃ­z
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
        Name = "FacturaciÃ³n"
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

Write-Host "`nðŸ“Š Resumen de construcciÃ³n:" -ForegroundColor Green
Write-Host "   âœ… Servicios construidos exitosamente: $successCount/$totalServices" -ForegroundColor Green

if ($successCount -eq $totalServices) {
    Write-Host "`nðŸŽ‰ Â¡Todas las imÃ¡genes construidas exitosamente!" -ForegroundColor Green
    Write-Host "   Ahora puedes proceder con el despliegue en Kubernetes" -ForegroundColor Cyan
} else {
    Write-Host "`nâš ï¸  Algunos servicios fallaron en la construcciÃ³n" -ForegroundColor Yellow
    Write-Host "   Revisa los errores antes de continuar" -ForegroundColor Red
}

