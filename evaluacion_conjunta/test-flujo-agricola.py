#!/usr/bin/env python3
"""
Script de prueba para el flujo completo del sistema agrícola:
COSECHA -> INVENTARIO -> FACTURA -> NOTIFICACIÓN
"""

import requests
import json
import time

# URLs de los microservicios
AGRICULTURA_URL = "http://localhost:8080"
INVENTARIO_URL = "http://localhost:8081"
FACTURACION_URL = "http://localhost:8082"
NOTIFICACIONES_URL = "http://localhost:8083"

def test_crear_agricultor():
    """Crear un agricultor de prueba"""
    print("=== Creando Agricultor ===")
    
    agricultor_data = {
        "agricultorId": "AGR-001",
        "nombre": "Juan Pérez",
        "finca": "Finca El Paraíso",
        "ubicacion": "9.7489°N, 83.7534°W",
        "correo": "juan.perez@email.com"
    }
    
    response = requests.post(f"{AGRICULTURA_URL}/agricultores", json=agricultor_data)
    print(f"Respuesta: {response.status_code} - {response.text}")
    return response.status_code == 200

def test_crear_insumos():
    """Crear insumos de prueba en el inventario"""
    print("\n=== Creando Insumos ===")
    
    insumos = [
        {
            "nombreInsumo": "Semilla Arroz L-23",
            "stock": 1000,
            "categoria": "Semilla"
        },
        {
            "nombreInsumo": "Fertilizante N-PK",
            "stock": 500,
            "categoria": "Fertilizante"
        },
        {
            "nombreInsumo": "Semilla Café Premium",
            "stock": 800,
            "categoria": "Semilla"
        },
        {
            "nombreInsumo": "Fertilizante Café",
            "stock": 400,
            "categoria": "Fertilizante"
        }
    ]
    
    for insumo in insumos:
        response = requests.post(f"{INVENTARIO_URL}/insumos", json=insumo)
        print(f"Insumo {insumo['nombreInsumo']}: {response.status_code}")
    
    return True

def test_crear_cosecha():
    """Crear una cosecha que disparará el flujo completo"""
    print("\n=== Creando Cosecha ===")
    
    cosecha_data = {
        "agricultorId": "AGR-001",
        "producto": "Arroz Oro",
        "toneladas": 12.5,
        "ubicacion": "9.7489°N, 83.7534°W"
    }
    
    response = requests.post(f"{AGRICULTURA_URL}/cosechas", json=cosecha_data)
    print(f"Respuesta: {response.status_code} - {response.text}")
    return response.status_code == 200

def test_verificar_inventario():
    """Verificar que el inventario se actualizó"""
    print("\n=== Verificando Inventario ===")
    
    response = requests.get(f"{INVENTARIO_URL}/insumos")
    if response.status_code == 200:
        insumos = response.json()
        for insumo in insumos:
            print(f"{insumo['nombreInsumo']}: {insumo['stock']} kg")
    else:
        print(f"Error obteniendo inventario: {response.status_code}")

def test_verificar_facturas():
    """Verificar que se generó la factura"""
    print("\n=== Verificando Facturas ===")
    
    response = requests.get(f"{FACTURACION_URL}/facturas")
    if response.status_code == 200:
        facturas = response.json()
        for factura in facturas:
            print(f"Factura {factura['facturaId']}: ${factura['montoTotal']} - Pagado: {factura['pagado']}")
    else:
        print(f"Error obteniendo facturas: {response.status_code}")

def test_verificar_notificaciones():
    """Verificar que se generaron notificaciones"""
    print("\n=== Verificando Notificaciones ===")
    
    response = requests.get(f"{NOTIFICACIONES_URL}/notificaciones")
    if response.status_code == 200:
        notificaciones = response.json()
        for notif in notificaciones:
            print(f"Notificación: {notif['mensaje']} - Tipo: {notif['tipo']}")
    else:
        print(f"Error obteniendo notificaciones: {response.status_code}")

def main():
    """Ejecutar el flujo completo de pruebas"""
    print("🚜 INICIANDO PRUEBAS DEL SISTEMA AGRÍCOLA 🚜")
    print("=" * 50)
    
    # Paso 1: Crear agricultor
    if not test_crear_agricultor():
        print("❌ Error creando agricultor")
        return
    
    # Paso 2: Crear insumos
    if not test_crear_insumos():
        print("❌ Error creando insumos")
        return
    
    # Paso 3: Crear cosecha (dispara el flujo completo)
    if not test_crear_cosecha():
        print("❌ Error creando cosecha")
        return
    
    # Esperar un momento para que se procesen los eventos
    print("\n⏳ Esperando procesamiento de eventos...")
    time.sleep(5)
    
    # Paso 4: Verificar resultados
    test_verificar_inventario()
    test_verificar_facturas()
    test_verificar_notificaciones()
    
    print("\n✅ PRUEBAS COMPLETADAS")
    print("=" * 50)
    print("Flujo completado: COSECHA -> INVENTARIO -> FACTURA -> NOTIFICACIÓN")

if __name__ == "__main__":
    main()
