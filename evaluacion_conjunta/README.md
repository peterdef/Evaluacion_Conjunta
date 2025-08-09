# 🚀 Guía de Despliegue en Kubernetes - Sistema Agrícola

UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE
Integrantes:
* Alisson Armijos
* Peter Defaz

- ✅ **Docker Desktop** (con Kubernetes habilitado)
- ✅ **kubectl** (cliente de Kubernetes)
- ✅ **Java 21** y **Maven 3.9.11**
- ✅ **PowerShell** (para ejecutar los scripts)

## 🔧 Paso 1: Preparar el Entorno

### 1.1 Verificar Kubernetes
```powershell
# Verificar que kubectl esté disponible
kubectl version --client

# Verificar que el cluster esté funcionando
kubectl cluster-info
```

### 1.2 Habilitar Kubernetes en Docker Desktop
1. Abrir Docker Desktop
2. Ir a Settings > Kubernetes
3. Marcar "Enable Kubernetes"
4. Aplicar y reiniciar

## 🏗️ Paso 2: Construir Imágenes Docker

### 2.1 Ejecutar Script de Construcción
```powershell
# Desde el directorio raíz del proyecto
.\build-images.ps1
```

Este script:
- ✅ Compila todos los microservicios con Maven
- ✅ Construye las imágenes Docker
- ✅ Etiqueta las imágenes con `sistema-agricola/`

### 2.2 Verificar Imágenes Construidas
```powershell
docker images | findstr sistema-agricola
```

Deberías ver:
- `sistema-agricola/eureka-server:latest`
- `sistema-agricola/api-gateway:latest`
- `sistema-agricola/agricultura:latest`
- `sistema-agricola/inventario:latest`
- `sistema-agricola/facturacion:latest`
- `sistema-agricola/notificaciones:latest`

## 🚀 Paso 3: Desplegar en Kubernetes

### 3.1 Ejecutar Script de Despliegue
```powershell
# Desde el directorio raíz del proyecto
.\deploy-kubernetes.ps1
```

Este script:
- ✅ Crea el namespace `sistema-agricola`
- ✅ Despliega todas las bases de datos
- ✅ Despliega todos los microservicios
- ✅ Configura el Ingress

### 3.2 Verificar el Despliegue
```powershell
# Ver pods
kubectl get pods -n sistema-agricola

# Ver servicios
kubectl get services -n sistema-agricola

# Ver deployments
kubectl get deployments -n sistema-agricola
```

## 🧪 Paso 4: Probar el Sistema

### 4.1 Ejecutar Script de Pruebas
```powershell
# Desde el directorio raíz del proyecto
.\test-kubernetes.ps1
```

Este script:
- ✅ Verifica que todos los servicios estén disponibles
- ✅ Ejecuta el flujo completo del sistema
- ✅ Crea agricultor, insumo y cosecha
- ✅ Valida la comunicación entre microservicios

### 4.2 Pruebas Manuales

#### Acceder a Eureka Dashboard
```
http://localhost:30761
```

#### Acceder a API Gateway
```
http://localhost:30084
```

#### Acceder a RabbitMQ Management
```
http://localhost:15672
Usuario: guest
Contraseña: guest
```

## 📊 Monitoreo y Logs

### 4.1 Ver Logs de Microservicios
```powershell
# Logs de Agricultura
kubectl logs -f deployment/agricultura -n sistema-agricola

# Logs de Inventario
kubectl logs -f deployment/inventario -n sistema-agricola

# Logs de Facturación
kubectl logs -f deployment/facturacion -n sistema-agricola

# Logs de Notificaciones
kubectl logs -f deployment/notificaciones -n sistema-agricola
```

### 4.2 Ver Estado de Recursos
```powershell
# Estado general
kubectl get all -n sistema-agricola

# Descripción detallada de pods
kubectl describe pods -n sistema-agricola

# Métricas de recursos
kubectl top pods -n sistema-agricola
```

## 🔄 Flujo del Sistema

El sistema funciona de la siguiente manera:

1. **👨‍🌾 Agricultor** registra una cosecha
2. **📦 Inventario** descuenta insumos automáticamente
3. **💰 Facturación** genera factura
4. **📧 Notificaciones** envía notificación al agricultor

### Endpoints Disponibles

#### Agricultura (Puerto 30084)
- `POST /agricultores` - Crear agricultor
- `GET /agricultores` - Listar agricultores
- `POST /cosechas` - Registrar cosecha
- `GET /cosechas` - Listar cosechas

#### Inventario (Puerto 30084)
- `POST /insumos` - Crear insumo
- `GET /insumos` - Listar insumos
- `PUT /insumos/{id}` - Actualizar stock

#### Facturación (Puerto 30084)
- `GET /facturas` - Listar facturas
- `GET /facturas/{id}` - Obtener factura

#### Notificaciones (Puerto 30084)
- `GET /notificaciones` - Listar notificaciones

## 🛠️ Solución de Problemas

### Problema: Pods no inician
```powershell
# Verificar eventos
kubectl get events -n sistema-agricola

# Verificar logs de pods
kubectl logs <pod-name> -n sistema-agricola

# Verificar descripción del pod
kubectl describe pod <pod-name> -n sistema-agricola
```

### Problema: Servicios no se comunican
```powershell
# Verificar conectividad entre pods
kubectl exec -it <pod-name> -n sistema-agricola -- ping <service-name>

# Verificar DNS
kubectl exec -it <pod-name> -n sistema-agricola -- nslookup <service-name>
```

### Problema: Bases de datos no conectan
```powershell
# Verificar estado de bases de datos
kubectl get pods -l app=postgres -n sistema-agricola
kubectl get pods -l app=mysql -n sistema-agricola
kubectl get pods -l app=mariadb -n sistema-agricola
kubectl get pods -l app=mongodb -n sistema-agricola
```

## 🧹 Limpieza

### Eliminar Todo el Sistema
```powershell
# Eliminar namespace completo
kubectl delete namespace sistema-agricola

# Eliminar imágenes Docker
docker rmi sistema-agricola/eureka-server:latest
docker rmi sistema-agricola/api-gateway:latest
docker rmi sistema-agricola/agricultura:latest
docker rmi sistema-agricola/inventario:latest
docker rmi sistema-agricola/facturacion:latest
docker rmi sistema-agricola/notificaciones:latest
```

## 📁 Estructura de Archivos Kubernetes

```
kubernetes/sistema-agricola/
├── 1-namespace.yml           # Namespace del sistema
├── 2-databases.yml           # Bases de datos (PostgreSQL, MySQL, MariaDB, MongoDB, RabbitMQ)
├── 3-microservicios.yml      # Microservicios (Eureka, Gateway, Agricultura, Inventario, Facturación, Notificaciones)
└── 4-ingress.yml            # Configuración de Ingress
```

## 🎯 Comandos Útiles

### Verificar Estado del Sistema
```powershell
# Estado general
kubectl get all -n sistema-agricola

# Verificar conectividad
kubectl port-forward service/api-gateway 30084:8084 -n sistema-agricola

# Acceder a un pod específico
kubectl exec -it <pod-name> -n sistema-agricola -- /bin/bash
```

### Escalar Microservicios
```powershell
# Escalar Agricultura a 3 réplicas
kubectl scale deployment agricultura --replicas=3 -n sistema-agricola

# Escalar Inventario a 2 réplicas
kubectl scale deployment inventario --replicas=2 -n sistema-agricola
```

## ✅ Verificación Final

Para verificar que todo funciona correctamente:

1. **Eureka Dashboard**: Debe mostrar todos los servicios registrados
2. **API Gateway**: Debe responder a peticiones HTTP
3. **RabbitMQ Management**: Debe mostrar las colas creadas
4. **Script de pruebas**: Debe ejecutar el flujo completo sin errores
5. **Logs**: Deben mostrar el procesamiento de eventos

¡El sistema agrícola está ahora ejecutándose en Kubernetes! 🎉

## 📞 Soporte

Si encuentras problemas:

1. Verifica que Docker Desktop tenga Kubernetes habilitado
2. Asegúrate de que todos los prerrequisitos estén instalados
3. Revisa los logs de los pods para identificar errores
4. Verifica la conectividad entre servicios

¡El sistema está listo para producción! 🚀

