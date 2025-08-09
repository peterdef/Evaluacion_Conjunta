# 🚜 Guía Completa de Ejecución - Sistema Agrícola

## 📋 Prerrequisitos Verificados ✅

- ✅ Java 21
- ✅ Maven 3.9.11
- ✅ Docker 28.3.2

## 🗄️ Paso 1: Configurar Bases de Datos

Las bases de datos ya están ejecutándose en Docker:

```bash
# Verificar contenedores activos
docker ps
```

**Contenedores activos:**
- PostgreSQL (agricultura): puerto 5432
- MySQL (inventario): puerto 3306
- MariaDB (facturación): puerto 3307
- MongoDB (notificaciones): puerto 27017
- RabbitMQ: puertos 5672, 15672

## 🔧 Paso 2: Compilar Microservicios

Los microservicios ya están compilados:

- ✅ **ms-agricultura** (api-publicaciones)
- ✅ **ms-inventario**
- ⏳ **ms-facturacion** (pendiente)
- ⏳ **ms-notificaciones** (pendiente)
- ⏳ **ms-eureka-server** (pendiente)
- ⏳ **ms-api-gateway** (pendiente)

## 🚀 Paso 3: Ejecutar el Sistema

### Opción A: Script Automático (Recomendado)

```bash
# Ejecutar el script de inicio
.\start-services.bat
```

### Opción B: Ejecución Manual

#### 1. Eureka Server
```bash
cd ms-eureka-server
mvn spring-boot:run
```

#### 2. API Gateway
```bash
cd ms-api-gateway
mvn spring-boot:run
```

#### 3. Microservicio de Agricultura
```bash
cd api-publicaciones
mvn spring-boot:run
```

#### 4. Microservicio de Inventario
```bash
cd ms-inventario
mvn spring-boot:run
```

#### 5. Microservicio de Facturación
```bash
cd ms-facturacion
mvn spring-boot:run
```

#### 6. Microservicio de Notificaciones
```bash
cd ms-notificaciones
mvn spring-boot:run
```

## 🌐 URLs de Acceso

Una vez ejecutados todos los servicios:

- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8084
- **Agricultura**: http://localhost:8080
- **Inventario**: http://localhost:8081
- **Facturación**: http://localhost:8082
- **Notificaciones**: http://localhost:8083
- **RabbitMQ Management**: http://localhost:15672 (guest/guest)

## 🧪 Paso 4: Probar el Sistema

### Script de Prueba Automático
```bash
python test-flujo-agricola.py
```

### Pruebas Manuales

#### 1. Crear Agricultor
```bash
curl -X POST http://localhost:8080/agricultores \
  -H "Content-Type: application/json" \
  -d '{
    "agricultorId": "AGR-001",
    "nombre": "Juan Pérez",
    "finca": "Finca El Paraíso",
    "ubicacion": "9.7489°N, 83.7534°W",
    "correo": "juan.perez@email.com"
  }'
```

#### 2. Crear Insumos
```bash
curl -X POST http://localhost:8081/insumos \
  -H "Content-Type: application/json" \
  -d '{
    "nombreInsumo": "Semilla Arroz L-23",
    "stock": 1000,
    "categoria": "Semilla"
  }'
```

#### 3. Registrar Cosecha (dispara el flujo completo)
```bash
curl -X POST http://localhost:8080/cosechas \
  -H "Content-Type: application/json" \
  -d '{
    "agricultorId": "AGR-001",
    "producto": "Arroz Oro",
    "toneladas": 12.5,
    "ubicacion": "9.7489°N, 83.7534°W"
  }'
```

## 🔄 Flujo del Sistema

1. **COSECHA** → Agricultor registra cosecha
2. **INVENTARIO** → Se descuentan insumos automáticamente
3. **FACTURA** → Se calcula y genera factura
4. **NOTIFICACIÓN** → Se envía notificación al agricultor

## 📊 Monitoreo

- **Eureka Dashboard**: Ver servicios registrados
- **RabbitMQ Management**: Ver colas y mensajes
- **Logs de cada microservicio**: Ver el flujo de eventos

## 🛠️ Solución de Problemas

### Si un microservicio no inicia:
1. Verificar que la base de datos correspondiente esté ejecutándose
2. Verificar que RabbitMQ esté ejecutándose
3. Verificar que el puerto no esté ocupado
4. Revisar los logs del microservicio

### Si las pruebas fallan:
1. Verificar que todos los microservicios estén ejecutándose
2. Verificar que las bases de datos estén accesibles
3. Verificar que RabbitMQ esté funcionando
4. Revisar los logs de cada microservicio

## 📁 Estructura del Proyecto

```
evaluacion_conjunta/
├── api-publicaciones/          # Microservicio de agricultura
├── ms-inventario/             # Microservicio de inventario
├── ms-facturacion/            # Microservicio de facturación
├── ms-notificaciones/         # Microservicio de notificaciones
├── ms-eureka-server/          # Servidor de descubrimiento
├── ms-api-gateway/            # Gateway de servicios
├── start-services.bat         # Script de inicio automático
├── test-flujo-agricola.py    # Script de pruebas
└── GUIA_EJECUCION.md         # Esta guía
```

## ✅ Verificación Final

Para verificar que todo funciona correctamente:

1. **Eureka Dashboard**: Debe mostrar todos los servicios registrados
2. **RabbitMQ Management**: Debe mostrar las colas creadas
3. **Script de pruebas**: Debe ejecutar el flujo completo sin errores
4. **Logs**: Deben mostrar el procesamiento de eventos

¡El sistema está listo para usar! 🎉
