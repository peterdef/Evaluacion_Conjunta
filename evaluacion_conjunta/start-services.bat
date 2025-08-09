@echo off
echo Iniciando Sistema Agricola - Microservicios
echo ===========================================

echo.
echo 1. Iniciando Eureka Server...
start "Eureka Server" cmd /k "cd ms-eureka-server && mvn spring-boot:run"

echo.
echo 2. Iniciando API Gateway...
timeout /t 10 /nobreak >nul
start "API Gateway" cmd /k "cd ms-api-gateway && mvn spring-boot:run"

echo.
echo 3. Iniciando Microservicio de Agricultura...
timeout /t 10 /nobreak >nul
start "Agricultura" cmd /k "cd api-publicaciones && mvn spring-boot:run"

echo.
echo 4. Iniciando Microservicio de Inventario...
timeout /t 10 /nobreak >nul
start "Inventario" cmd /k "cd ms-inventario && mvn spring-boot:run"

echo.
echo 5. Iniciando Microservicio de Facturacion...
timeout /t 10 /nobreak >nul
start "Facturacion" cmd /k "cd ms-facturacion && mvn spring-boot:run"

echo.
echo 6. Iniciando Microservicio de Notificaciones...
timeout /t 10 /nobreak >nul
start "Notificaciones" cmd /k "cd ms-notificaciones && mvn spring-boot:run"

echo.
echo Todos los microservicios estan iniciando...
echo.
echo URLs de acceso:
echo - Eureka Dashboard: http://localhost:8761
echo - API Gateway: http://localhost:8084
echo - Agricultura: http://localhost:8080
echo - Inventario: http://localhost:8081
echo - Facturacion: http://localhost:8082
echo - Notificaciones: http://localhost:8083
echo - RabbitMQ Management: http://localhost:15672 (guest/guest)
echo.
echo Presiona cualquier tecla para ejecutar las pruebas...
pause >nul

echo.
echo Ejecutando pruebas del sistema...
python test-flujo-agricola.py

echo.
echo Sistema iniciado completamente!
pause
