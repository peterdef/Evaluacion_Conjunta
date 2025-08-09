# Sistema Agrícola - Microservicios

## Descripción
Sistema de gestión agrícola basado en microservicios que implementa el flujo:
**COSECHA -> INVENTARIO -> FACTURA -> NOTIFICACIÓN**

## Arquitectura

### Microservicios
1. **ms-agricultura** (Puerto 8080) - Gestión de agricultores y cosechas
2. **ms-inventario** (Puerto 8081) - Control de stock de insumos
3. **ms-facturacion** (Puerto 8082) - Generación de facturas
4. **ms-notificaciones** (Puerto 8083) - Sistema de notificaciones
5. **ms-eureka-server** (Puerto 8761) - Servidor de descubrimiento
6. **ms-api-gateway** (Puerto 8084) - Gateway de servicios

### Bases de Datos
- **PostgreSQL** - Microservicio de agricultura
- **MySQL** - Microservicio de inventario  
- **MariaDB** - Microservicio de facturación
- **MongoDB** - Microservicio de notificaciones

### Message Broker
- **RabbitMQ** - Comunicación asíncrona entre servicios

## Flujo del Sistema

1. **Registro de Cosecha**: Agricultor registra una nueva cosecha
2. **Procesamiento de Inventario**: Se descuentan insumos automáticamente
3. **Generación de Factura**: Se calcula y genera la factura
4. **Notificación**: Se envía notificación al agricultor

## Instalación y Configuración

### Prerrequisitos
- Java 21
- Maven
- Docker y Docker Compose
- Python 3.8+ (para pruebas)

### 1. Configurar Bases de Datos

```bash
# PostgreSQL para agricultura
docker run -d --name postgres-agricultura \
  -e POSTGRES_DB=agricultura_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 postgres:14

# MySQL para inventario
docker run -d --name mysql-inventario \
  -e MYSQL_DATABASE=inventario_db \
  -e MYSQL_ROOT_PASSWORD=root \
  -p 3306:3306 mysql:8

# MariaDB para facturación
docker run -d --name mariadb-facturacion \
  -e MYSQL_DATABASE=facturacion_db \
  -e MYSQL_ROOT_PASSWORD=root \
  -p 3307:3306 mariadb:10.11

# MongoDB para notificaciones
docker run -d --name mongodb-notificaciones \
  -e MONGO_INITDB_DATABASE=notificaciones_db \
  -p 27017:27017 mongo:6
```

### 2. Configurar RabbitMQ

```bash
docker run -d --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management
```

### 3. Compilar Microservicios

```bash
# Microservicio de agricultura
cd api-publicaciones
mvn clean package

# Microservicio de inventario
cd ../ms-inventario
mvn clean package

# Microservicio de facturación
cd ../ms-facturacion
mvn clean package

# Microservicio de notificaciones
cd ../ms-notificaciones
mvn clean package
```

### 4. Ejecutar Microservicios

```bash
# 1. Eureka Server
cd ms-eureka-server
mvn spring-boot:run

# 2. API Gateway
cd ../ms-api-gateway
mvn spring-boot:run

# 3. Microservicio de agricultura
cd ../api-publicaciones
mvn spring-boot:run

# 4. Microservicio de inventario
cd ../ms-inventario
mvn spring-boot:run

# 5. Microservicio de facturación
cd ../ms-facturacion
mvn spring-boot:run

# 6. Microservicio de notificaciones
cd ../ms-notificaciones
mvn spring-boot:run
```

## Pruebas del Sistema

### Script de Prueba Automatizado

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

## Endpoints Principales

### Microservicio de Agricultura (8080)
- `GET /agricultores` - Listar agricultores
- `POST /agricultores` - Crear agricultor
- `GET /cosechas` - Listar cosechas
- `POST /cosechas` - Registrar cosecha

### Microservicio de Inventario (8081)
- `GET /insumos` - Listar insumos
- `POST /insumos` - Crear insumo
- `PUT /insumos/stock/{nombreInsumo}` - Actualizar stock

### Microservicio de Facturación (8082)
- `GET /facturas` - Listar facturas
- `POST /facturas` - Crear factura
- `GET /facturas/cosecha/{cosechaId}` - Buscar por cosecha

### Microservicio de Notificaciones (8083)
- `GET /notificaciones` - Listar notificaciones

## Docker

### Construir Imágenes
```bash
# Agricultura
cd api-publicaciones
docker build -t ms-agricultura:v1.0 .

# Inventario
cd ../ms-inventario
docker build -t ms-inventario:v1.0 .

# Facturación
cd ../ms-facturacion
docker build -t ms-facturacion:v1.0 .
```

### Publicar en Docker Hub
```bash
docker tag ms-agricultura:v1.0 tu-usuario/ms-agricultura:v1.0
docker push tu-usuario/ms-agricultura:v1.0

docker tag ms-inventario:v1.0 tu-usuario/ms-inventario:v1.0
docker push tu-usuario/ms-inventario:v1.0

docker tag ms-facturacion:v1.0 tu-usuario/ms-facturacion:v1.0
docker push tu-usuario/ms-facturacion:v1.0
```

## Kubernetes

Los archivos de configuración de Kubernetes se encuentran en la carpeta `kubernetes/`.

### Desplegar en Kubernetes
```bash
kubectl apply -f kubernetes/
```

## Monitoreo

- **Eureka Dashboard**: http://localhost:8761
- **RabbitMQ Management**: http://localhost:15672 (guest/guest)

## Estructura del Proyecto

```
evaluacion_conjunta/
├── api-publicaciones/          # Microservicio de agricultura
├── ms-inventario/             # Microservicio de inventario
├── ms-facturacion/            # Microservicio de facturación
├── ms-notificaciones/         # Microservicio de notificaciones
├── ms-eureka-server/          # Servidor de descubrimiento
├── ms-api-gateway/            # Gateway de servicios
├── kubernetes/                # Configuraciones de K8s
├── test-flujo-agricola.py    # Script de pruebas
└── README.md                 # Este archivo
```

## Tecnologías Utilizadas

- **Backend**: Spring Boot 3.5.3, Java 21
- **Bases de Datos**: PostgreSQL, MySQL, MariaDB, MongoDB
- **Message Broker**: RabbitMQ
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Containerización**: Docker
- **Orquestación**: Kubernetes
- **Testing**: Python Requests

## Autores

- [Tu Nombre]
- [Nombre del Grupo]

## Licencia

Este proyecto es parte de la evaluación conjunta de Arquitectura de Software.
