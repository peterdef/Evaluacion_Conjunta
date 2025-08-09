# Script para construir imágenes Docker del Sistema Agrícola
Write-Host "🚀 Iniciando construcción de imágenes Docker..." -ForegroundColor Green

# Volver al directorio raíz
Set-Location $PSScriptRoot

# Construir Eureka Server
Write-Host "📦 Compilando Eureka Server..." -ForegroundColor Yellow
Set-Location "ms-eureka-server"
mvn clean package -DskipTests
if ($LASTEXITCODE -eq 0) {
    docker build -t sistema-agricola/eureka-server:latest .
    Write-Host "✅ Eureka Server construido exitosamente" -ForegroundColor Green
} else {
    Write-Host "❌ Error construyendo Eureka Server" -ForegroundColor Red
}

# Volver al directorio raíz
Set-Location $PSScriptRoot

# Construir API Gateway
Write-Host "📦 Compilando API Gateway..." -ForegroundColor Yellow
Set-Location "ms-api-gateway"
mvn clean package -DskipTests
if ($LASTEXITCODE -eq 0) {
    docker build -t sistema-agricola/api-gateway:latest .
    Write-Host "✅ API Gateway construido exitosamente" -ForegroundColor Green
} else {
    Write-Host "❌ Error construyendo API Gateway" -ForegroundColor Red
}

# Volver al directorio raíz
Set-Location $PSScriptRoot

# Construir Agricultura
Write-Host "📦 Compilando Agricultura..." -ForegroundColor Yellow
Set-Location "api-publicaciones"
mvn clean package -DskipTests
if ($LASTEXITCODE -eq 0) {
    docker build -t sistema-agricola/agricultura:latest .
    Write-Host "✅ Agricultura construido exitosamente" -ForegroundColor Green
} else {
    Write-Host "❌ Error construyendo Agricultura" -ForegroundColor Red
}

# Volver al directorio raíz
Set-Location $PSScriptRoot

# Construir Inventario
Write-Host "📦 Compilando Inventario..." -ForegroundColor Yellow
Set-Location "ms-inventario"
mvn clean package -DskipTests
if ($LASTEXITCODE -eq 0) {
    docker build -t sistema-agricola/inventario:latest .
    Write-Host "✅ Inventario construido exitosamente" -ForegroundColor Green
} else {
    Write-Host "❌ Error construyendo Inventario" -ForegroundColor Red
}

# Volver al directorio raíz
Set-Location $PSScriptRoot

# Construir Facturación
Write-Host "📦 Compilando Facturación..." -ForegroundColor Yellow
Set-Location "ms-facturacion"
mvn clean package -DskipTests
if ($LASTEXITCODE -eq 0) {
    docker build -t sistema-agricola/facturacion:latest .
    Write-Host "✅ Facturación construido exitosamente" -ForegroundColor Green
} else {
    Write-Host "❌ Error construyendo Facturación" -ForegroundColor Red
}

# Volver al directorio raíz
Set-Location $PSScriptRoot

# Construir Notificaciones
Write-Host "📦 Compilando Notificaciones..." -ForegroundColor Yellow
Set-Location "ms-notificaciones"
mvn clean package -DskipTests
if ($LASTEXITCODE -eq 0) {
    docker build -t sistema-agricola/notificaciones:latest .
    Write-Host "✅ Notificaciones construido exitosamente" -ForegroundColor Green
} else {
    Write-Host "❌ Error construyendo Notificaciones" -ForegroundColor Red
}

# Volver al directorio raíz
Set-Location $PSScriptRoot

Write-Host "`n🎉 ¡Proceso de construcción completado!" -ForegroundColor Green
Write-Host "   Ahora puedes proceder con el despliegue en Kubernetes" -ForegroundColor Cyan
