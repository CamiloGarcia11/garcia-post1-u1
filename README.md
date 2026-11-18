# Post-contenido — Unidad 1: Fundamentos de Patrones de Diseño y Buenas Prácticas

**Estudiante:** Camilo García  
**Curso:** Patrones de Diseño de Software — Sexto Semestre  
**Unidad:** Unidad 1 — Fundamentos de Patrones de Diseño y Buenas Prácticas  
**Actividad:** Post-contenido — Refactorización SOLID y Análisis de Patrones GoF en Spring Framework  
**Fecha de entrega:** 2026-11-18  
**Plataforma:** NPLAD UFPS  

---

## Descripción
Repositorio del post-contenido de la Unidad 1 de Patrones de Diseño de Software — Sexto Semestre. Contiene dos partes evaluadas de manera integral: la refactorización orientada a objetos y principios SOLID de un God Object heredado (`parte-1-refactorizacion-solid/`) y la investigación técnica con análisis de patrones GoF en el código fuente de Spring Framework (`parte-2-analisis-gof-spring/`).

---

## Parte 1 — Refactorización SOLID
Proyecto Maven que refactoriza la clase monolítica `OrderProcessor` aplicando los principios de Responsabilidad Única (SRP), Abierto/Cerrado (OCP) e Inversión de Dependencias (DIP). Ver carpeta [parte-1-refactorizacion-solid/](parte-1-refactorizacion-solid/).

### Análisis de Violaciones SOLID
| Principio | Método/Sección afectada | Descripción de la violación | Solución aplicada en la refactorización |
|---|---|---|---|
| **SRP** | `calculateTotal` + `applyDiscount` + `saveOrder` + `sendEmail` + `printReport` | `OrderProcessor` concentraba cinco responsabilidades no cohesionadas: lógica impositiva, reglas de descuento, almacenamiento, comunicación por email y renderizado de consola. Cualquier modificación en un detalle técnico obligaba a alterar y reprobar toda la lógica de negocio. | Se fragmentó la clase en colaboradores con responsabilidad única: `TaxCalculator` (impuestos), `OrderRepository` (persistencia), `EmailNotifier` (notificación) y `OrderReporter` (reportes). |
| **OCP** | `applyDiscount` (`if/else` sobre `customerType`) | El cálculo de descuentos utilizaba bifurcaciones condicionales encadenadas evaluando cadenas fijas (`"VIP"`, `"REGULAR"`). Agregar un nuevo beneficio implicaba modificar directamente el código fuente de una clase ya probada y en funcionamiento. | Se aplicó el patrón *Strategy* extrayendo la abstracción `DiscountStrategy` y sus implementaciones concretas `VipDiscount`, `RegularDiscount` y `NoDiscount`. Nuevas promociones se añaden mediante nuevas clases sin alterar el código existente. |
| **DIP** | Toda la clase (dependencias internas y salidas directas) | La orquestación dependía de instanciaciones y operaciones internas acopladas rígidamente, impidiendo desacoplar el flujo de negocio y bloqueando la sustitución de colaboradores por dobles de prueba (*mocks*) en pruebas unitarias. | `OrderService` recibe todos sus colaboradores mediante inyección por constructor (`TaxCalculator`, `OrderRepository`, `EmailNotifier`, `DiscountStrategy`), dependiendo de contratos y abstracciones. |

### Estructura de Clases Resultante
- `OrderProcessor`: God Object inicial tomado como referencia del estado legacy.
- `TaxCalculator`: Responsabilidad única de cálculo de subtotales e impuestos (19 %).
- `OrderRepository`: Responsabilidad única de persistencia en memoria y consulta de órdenes guardadas.
- `EmailNotifier`: Responsabilidad única de transmisión de confirmaciones de orden al cliente.
- `OrderReporter`: Responsabilidad única de formato y emisión del reporte de órdenes.
- `DiscountStrategy`: Interfaz cohesiva abierta a extensión (OCP / DIP / ISP).
- `VipDiscount`: Implementación de descuento VIP (15 % de descuento).
- `RegularDiscount`: Implementación de descuento Regular (5 % de descuento).
- `NoDiscount`: Implementación de descuento base (0 % de descuento).
- `OrderService`: Orquestador de negocio con inyección por constructor (DIP / SRP).
- `Main`: Demostración ejecutable del sistema refactorizado.
- `OrderServiceTest`: Suite de pruebas unitarias automatizadas con JUnit 5 y Mockito.

### Compilación y Ejecución (Parte 1)
```bash
cd parte-1-refactorizacion-solid
mvn compile
mvn exec:java -Dexec.mainClass="com.patrones.u1.Main"
```

#### Salida esperada por consola
```text
[DB] Orden guardada: ORD-001
[EMAIL] Enviando a vip@mail.com confirmación de orden ORD-001
[DB] Orden guardada: ORD-002
[EMAIL] Enviando a reg@mail.com confirmación de orden ORD-002
=== Reporte de Órdenes ===
 ORD-001:354.025
 ORD-002:226.1
```

---

## Parte 2 — Análisis de Patrones GoF en Spring
Investigación y análisis contrafáctico en el código fuente de Spring Framework, documentado en detalle en [documento-analisis.md](parte-2-analisis-gof-spring/documento-analisis.md) con evidencias reales en la subcarpeta `evidencia/`.

| # | Patrón | Categoría | Clase en Spring | Módulo | SOLID Reforzado |
|---|--------|-----------|-----------------|--------|-----------------|
| 1 | **Factory Method** | Creacional | `org.springframework.beans.factory.BeanFactory` | `spring-beans` | **DIP** (Inversión de Dependencias) |
| 2 | **Proxy** | Estructural | `org.springframework.aop.framework.JdkDynamicAopProxy` | `spring-aop` | **OCP** (Abierto/Cerrado) |
| 3 | **Template Method** | Comportamiento | `org.springframework.jdbc.core.JdbcTemplate` | `spring-jdbc` | **SRP** (Responsabilidad Única) |

Ver documento completo: [parte-2-analisis-gof-spring/documento-analisis.md](parte-2-analisis-gof-spring/documento-analisis.md).

---

## Estructura Completa del Repositorio
```text
garcia-post1-u1/
├── .gitignore
├── README.md
├── parte-1-refactorizacion-solid/
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/patrones/u1/
│       │   ├── DiscountStrategy.java
│       │   ├── EmailNotifier.java
│       │   ├── Main.java
│       │   ├── NoDiscount.java
│       │   ├── OrderProcessor.java
│       │   ├── OrderReporter.java
│       │   ├── OrderRepository.java
│       │   ├── OrderService.java
│       │   ├── RegularDiscount.java
│       │   ├── TaxCalculator.java
│       │   └── VipDiscount.java
│       └── test/java/com/patrones/u1/
│           └── OrderServiceTest.java
└── parte-2-analisis-gof-spring/
    ├── documento-analisis.md
    └── evidencia/
        ├── BeanFactory.java
        ├── JdbcTemplate.java
        └── JdkDynamicAopProxy.java
```

---

## Herramientas utilizadas
- Java 17, Apache Maven, VS Code, Git, GitHub
- Código fuente de Spring Framework (investigación)

---

## Conclusiones
La realización de este post-contenido permitió constatar de forma práctica que el desacoplamiento mediante principios SOLID no es solo un ideal teórico, sino una necesidad imperativa para garantizar la evolución y testabilidad del software. Al refactorizar el God Object `OrderProcessor`, se transformó una estructura monolítica frágil en un ecosistema modular de clases cohesivas donde la adición de nuevas reglas comerciales no compromete el código existente. Asimismo, el análisis del código fuente de Spring Framework demostró cómo patrones fundamentales como Factory Method, Proxy y Template Method permiten a un framework de escala global resolver preocupaciones transversales y estandarizar algoritmos sin sobrecargar la lógica de negocio del desarrollador. En conclusión, el dominio de los patrones GoF y las buenas prácticas de diseño constituye la base para construir aplicaciones empresariales escalables, legibles y resilientes al cambio.
