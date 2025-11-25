# 🧬 Mutant Detector API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

API REST desarrollada con Spring Boot para detectar mutantes mediante el análisis de secuencias de ADN. Este proyecto implementa un algoritmo optimizado que identifica patrones genéticos específicos en matrices cuadradas.

## 📋 Descripción

El **Mutant Detector** es una aplicación que analiza secuencias de ADN representadas como matrices cuadradas (NxN) y determina si pertenecen a un mutante o a un humano, basándose en la presencia de secuencias repetitivas de bases nitrogenadas.

### Criterio de Detección

Un individuo es considerado **mutante** si su ADN contiene **más de una secuencia de 4 caracteres iguales** consecutivos en cualquiera de estas direcciones:
- Horizontal (→)
- Vertical (↓)
- Diagonal descendente (↘)
- Diagonal ascendente (↗)

### Ejemplo

**ADN Mutante** (retorna `200 OK`):
```
Secuencia vertical: GGGG
    ↓   
ATGCGA
CAGTGC
TTATGT
AGAAGG
CCCCTA  ← Secuencia horizontal: CCCC
TCACTG
```

**ADN Humano** (retorna `403 Forbidden`):
```
ATGCGA
CAGTGC
TCTTTT  ← Solo 1 secuencia: TTTT
AGACGG
GCGTCA
TCACTG
```

## 🚀 Características Principales

- ✅ **API REST** completa con endpoints documentados
- ✅ **Algoritmo optimizado** con early termination y single-pass traversal
- ✅ **Sistema de caché** usando hash SHA-256 para evitar recálculos
- ✅ **Validaciones robustas** con validadores personalizados
- ✅ **Documentación interactiva** con Swagger/OpenAPI
- ✅ **Arquitectura en capas** siguiendo principios SOLID
- ✅ **Tests completos** con alta cobertura de código
- ✅ **Manejo de excepciones** centralizado

## 🛠️ Stack Tecnológico

### Framework y Lenguaje
- **Java 21** - Lenguaje de programación
- **Spring Boot 3.3.3** - Framework de aplicación
- **Maven** - Gestor de dependencias

### Persistencia
- **Spring Data JPA** - Abstracción de acceso a datos
- **H2 Database** - Base de datos en memoria (desarrollo)

### Utilidades
- **Lombok** - Reducción de código boilerplate
- **Bean Validation** - Validaciones de datos
- **SpringDoc OpenAPI** - Documentación automática de API

### Testing
- **JUnit 5** - Framework de testing
- **JaCoCo** - Medición de cobertura de código

## 📁 Estructura del Proyecto

```
src/main/java/com/main/MutantDetector/
├── config/                  # Configuraciones
│   └── SwaggerConfig.java   # Configuración de Swagger/OpenAPI
├── controller/              # Capa de presentación
│   └── MutantController.java
├── dto/                     # Objetos de transferencia de datos
│   ├── DnaRequestDTO.java
│   ├── DnaResponseDTO.java
│   └── StatsResponseDTO.java
├── entity/                  # Entidades JPA
│   └── DnaRecord.java
├── exceptions/              # Manejo de excepciones
│   ├── GlobalExceptionHandler.java
│   ├── InvalidDnaException.java
│   └── DnaHashCalculationException.java
├── repository/              # Acceso a datos
│   └── DnaRecordRepository.java
├── service/                 # Lógica de negocio
│   ├── MutantDetector.java  # Algoritmo core
│   ├── MutantService.java   # Orquestación y caché
│   └── StatsService.java    # Estadísticas
└── validation/              # Validadores personalizados
    ├── ValidDnaSequence.java
    └── ValidDnaSequenceValidator.java
```

## 🏗️ Arquitectura

El proyecto sigue una **arquitectura en capas** con separación clara de responsabilidades:

```
┌─────────────────┐
│   Controller    │  ← Maneja requests HTTP y respuestas
└────────┬────────┘
         │
┌────────▼────────┐
│     Service     │  ← Lógica de negocio y orquestación
└────────┬────────┘
         │
┌────────▼────────┐
│   Repository    │  ← Acceso a base de datos
└────────┬────────┘
         │
┌────────▼────────┐
│     Entity      │  ← Modelo de datos
└─────────────────┘
```

### Flujo de una Request

1. **Cliente** envía POST `/mutant` con JSON del ADN
2. **Controller** valida el request y delega al Service
3. **Service** calcula hash SHA-256 y verifica caché en BD
4. Si no existe en caché, **MutantDetector** ejecuta el algoritmo
5. Resultado se guarda en BD para futuras consultas
6. **Controller** retorna respuesta HTTP apropiada

## 🔧 Requisitos Previos

- **Java JDK 21** o superior
- **Maven 3.6+**
- **Git** (opcional)

## 📦 Instalación y Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/Gonzalez-Matias/IntegradorMutantes.git
cd IntegradorMutantes
```

### 2. Compilar el proyecto

```bash
mvn clean install
```

### 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

### 4. Acceder a la documentación

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Usuario: `sa`
  - Contraseña: (vacío)

## 🐳 Ejecución con Docker

El proyecto incluye un `Dockerfile` optimizado con multi-stage build para crear una imagen ligera y eficiente.

### Prerequisitos

- **Docker** instalado y funcionando

### Construir la imagen

```bash
docker build -t mutant-detector:latest .
```

Este comando:
- Compila el proyecto usando Maven en una imagen temporal
- Crea una imagen final usando `distroless` (solo ~50MB)
- Incluye el JAR ejecutable optimizado

### Ejecutar el contenedor

```bash
# Ejecutar en modo interactivo
docker run -p 8080:8080 mutant-detector:latest

# Ejecutar en segundo plano (detached)
docker run -d -p 8080:8080 --name mutant-detector mutant-detector:latest

# Acceder a la aplicación
# http://localhost:8080/swagger-ui.html
```

## 📡 API Endpoints

### POST /mutant

Verifica si una secuencia de ADN pertenece a un mutante.

**Request:**
```json
{
  "dna": [
    "ATGCGA",
    "CAGTGC",
    "TTATGT",
    "AGAAGG",
    "CCCCTA",
    "TCACTG"
  ]
}
```

**Responses:**
- `200 OK` - Es mutante
- `403 Forbidden` - No es mutante
- `400 Bad Request` - ADN inválido

### GET /stats

Obtiene estadísticas de todas las verificaciones realizadas.

**Response:**
```json
{
  "cantHumanos": 100,
  "cantMutantes": 40,
  "ratio": 0.4
}
```

## 🧪 Testing

### Ejecutar todos los tests

```bash
mvn test
```

### Generar reporte de cobertura

```bash
mvn test jacoco:report
```

El reporte estará disponible en: `target/site/jacoco/index.html`

### Tests incluidos

- **Tests unitarios**: `MutantDetectorTest`, `MutantServiceTest`, `StatsServiceTest`
- **Tests de integración**: `MutantControllerTest`

## ⚡ Optimizaciones Implementadas

### 1. Early Termination
El algoritmo se detiene inmediatamente al encontrar 2 secuencias, evitando recorrer toda la matriz innecesariamente.

### 2. Single-Pass Traversal
Recorre la matriz una sola vez verificando las 4 direcciones simultáneamente.

### 3. Caché con Hash SHA-256
Evita recalcular ADN ya analizados usando hash como identificador único.

### 4. Conversión a char[][]
Optimiza el acceso a caracteres individuales para mejor rendimiento.

### 5. Boundary Checking
Verifica límites antes de buscar secuencias para evitar accesos fuera de rango.

## 💾 Base de Datos

### Esquema

La aplicación utiliza H2 Database con la siguiente estructura:

**Tabla: `dna_records`**
- `id` (BIGINT, PK, auto-increment)
- `dna_hash` (VARCHAR(64), UNIQUE, INDEXED) - Hash SHA-256 del ADN
- `es_mutant` (BOOLEAN, INDEXED) - Resultado del análisis
- `fecha_creacion` (TIMESTAMP) - Fecha de creación

### Estrategia de Hash

En lugar de almacenar la secuencia completa de ADN, se guarda su hash SHA-256:
- **Ventajas**:
  - Búsqueda O(1) con índice
  - Ahorro de espacio
  - Garantiza unicidad
  - Evita duplicados

## 📚 Ejemplos de Uso

### Usando cURL

**Verificar si es mutante:**
```bash
curl -X POST http://localhost:8080/mutant \
  -H "Content-Type: application/json" \
  -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}'
```

**Obtener estadísticas:**
```bash
curl http://localhost:8080/stats
```

### Usando Swagger UI

1. Accede a http://localhost:8080/swagger-ui.html
2. Expande el endpoint deseado
3. Haz clic en "Try it out"
4. Modifica el JSON según necesites
5. Haz clic en "Execute"

## 🔍 Validaciones

El sistema valida que el ADN:
- ✅ No sea `null` o vacío
- ✅ Sea una matriz cuadrada (NxN)
- ✅ Tenga tamaño mínimo de 4x4
- ✅ Solo contenga caracteres válidos: A, T, C, G
- ✅ Todas las filas tengan el mismo tamaño

## 📈 Estadísticas del Proyecto

- **Líneas de código**: ~2,500+
- **Tests**: 44
- **Cobertura total del código**: 93%
- **Cobertura Servicios**: 98%
- **Cobertura Controlador**: 100%
- **Endpoints REST**: 2
