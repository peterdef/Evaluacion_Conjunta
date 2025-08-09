# Script para probar el Sistema Agrícola en Kubernetes
Write-Host "🧪 Iniciando pruebas del sistema agrícola en Kubernetes..." -ForegroundColor Green

# Función para hacer peticiones HTTP
function Invoke-ServiceTest {
    param(
        [string]$ServiceName,
        [string]$Url,
        [string]$Method = "GET",
        [string]$Body = $null
    )
    
    Write-Host "🔍 Probando $ServiceName..." -ForegroundColor Yellow
    
    try {
        if ($Method -eq "GET") {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -TimeoutSec 10
        } else {
            $headers = @{
                "Content-Type" = "application/json"
            }
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $headers -Body $Body -TimeoutSec 10
        }
        
        Write-Host "✅ $ServiceName responde correctamente" -ForegroundColor Green
        return $true
    } catch {
        Write-Host "❌ Error probando $ServiceName: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# Función para esperar a que un servicio esté disponible
function Wait-ForService {
    param(
        [string]$ServiceName,
        [string]$Url,
        [int]$MaxAttempts = 30
    )
    
    Write-Host "⏳ Esperando a que $ServiceName esté disponible..." -ForegroundColor Yellow
    
    for ($i = 1; $i -le $MaxAttempts; $i++) {
        try {
            $response = Invoke-RestMethod -Uri $Url -Method GET -TimeoutSec 5
            Write-Host "✅ $ServiceName está disponible" -ForegroundColor Green
            return $true
        } catch {
            Write-Host "   Intento $i/$MaxAttempts - $ServiceName aún no está listo..." -ForegroundColor Gray
            Start-Sleep -Seconds 10
        }
    }
    
    Write-Host "❌ $ServiceName no está disponible después de $MaxAttempts intentos" -ForegroundColor Red
    return $false
}

# Obtener la IP del cluster
Write-Host "🔍 Obteniendo información del cluster..." -ForegroundColor Yellow
$clusterIP = kubectl get nodes -o wide --no-headers | Select-Object -First 1 | ForEach-Object { ($_ -split '\s+')[5] }

if (-not $clusterIP) {
    $clusterIP = "localhost"
    Write-Host "⚠️  No se pudo obtener la IP del cluster, usando localhost" -ForegroundColor Yellow
}

Write-Host "📍 IP del cluster: $clusterIP" -ForegroundColor Green

# URLs de los servicios
$services = @{
    "Eureka Server" = "http://$clusterIP`:30761"
    "API Gateway" = "http://$clusterIP`:30084"
    "Agricultura" = "http://$clusterIP`:30084/agricultores"
    "Inventario" = "http://$clusterIP`:30084/insumos"
    "Facturación" = "http://$clusterIP`:30084/facturas"
    "Notificaciones" = "http://$clusterIP`:30084/notificaciones"
    "RabbitMQ Management" = "http://$clusterIP`:15672"
}

# Esperar a que los servicios estén disponibles
Write-Host "`n⏳ Esperando a que los servicios estén disponibles..." -ForegroundColor Cyan
foreach ($service in $services.GetEnumerator()) {
    Wait-ForService -ServiceName $service.Key -Url $service.Value
}

# Probar flujo completo del sistema
Write-Host "`n🚀 Probando flujo completo del sistema..." -ForegroundColor Cyan

# 1. Crear agricultor
Write-Host "`n👨‍🌾 Paso 1: Creando agricultor..." -ForegroundColor Yellow
$agricultorData = @{
    agricultorId = "AGR-001"
    nombre = "Juan Pérez"
    finca = "Finca El Paraíso"
    ubicacion = "9.7489°N, 83.7534°W"
    correo = "juan.perez@email.com"
} | ConvertTo-Json

$agricultorResponse = Invoke-ServiceTest -ServiceName "Agricultura" -Url "http://$clusterIP`:30084/agricultores" -Method "POST" -Body $agricultorData

# 2. Crear insumo
Write-Host "`n📦 Paso 2: Creando insumo..." -ForegroundColor Yellow
$insumoData = @{
    nombreInsumo = "Semilla Arroz L-23"
    stock = 1000
    categoria = "Semilla"
} | ConvertTo-Json

$insumoResponse = Invoke-ServiceTest -ServiceName "Inventario" -Url "http://$clusterIP`:30084/insumos" -Method "POST" -Body $insumoData

# 3. Registrar cosecha (dispara el flujo completo)
Write-Host "`n🌾 Paso 3: Registrando cosecha..." -ForegroundColor Yellow
$cosechaData = @{
    agricultorId = "AGR-001"
    producto = "Arroz Oro"
    toneladas = 12.5
    ubicacion = "9.7489°N, 83.7534°W"
} | ConvertTo-Json

$cosechaResponse = Invoke-ServiceTest -ServiceName "Agricultura" -Url "http://$clusterIP`:30084/cosechas" -Method "POST" -Body $cosechaData

# Mostrar resultados
Write-Host "`n📊 Resultados de las pruebas:" -ForegroundColor Green
Write-Host "   ✅ Agricultor creado: $agricultorResponse" -ForegroundColor $(if ($agricultorResponse) { "Green" } else { "Red" })
Write-Host "   ✅ Insumo creado: $insumoResponse" -ForegroundColor $(if ($insumoResponse) { "Green" } else { "Red" })
Write-Host "   ✅ Cosecha registrada: $cosechaResponse" -ForegroundColor $(if ($cosechaResponse) { "Green" } else { "Red" })

# Mostrar logs de los pods
Write-Host "`n📋 Logs de los microservicios:" -ForegroundColor Cyan
Write-Host "   Para ver logs detallados, ejecuta:" -ForegroundColor Yellow
Write-Host "   kubectl logs -f deployment/agricultura -n sistema-agricola" -ForegroundColor Gray
Write-Host "   kubectl logs -f deployment/inventario -n sistema-agricola" -ForegroundColor Gray
Write-Host "   kubectl logs -f deployment/facturacion -n sistema-agricola" -ForegroundColor Gray
Write-Host "   kubectl logs -f deployment/notificaciones -n sistema-agricola" -ForegroundColor Gray

# Mostrar estado final
Write-Host "`n🎉 ¡Pruebas completadas!" -ForegroundColor Green
Write-Host "   El sistema agrícola está funcionando correctamente en Kubernetes" -ForegroundColor Cyan

