# garcia-post1-u1

Post-contenido — Refactorización SOLID y análisis de patrones GoF en Spring

## Análisis de Violaciones SOLID — Parte 1

| Principio | Método/Sección afectada | Descripción de la violación |
|-----------|-------------------------|-----------------------------|
| SRP | calculateTotal + applyDiscount + saveOrder + sendEmail + printReport | La clase OrderProcessor concentra simultáneamente la lógica de negocio (cálculo de subtotal e impuestos), la aplicación de políticas comerciales de descuento, la persistencia en memoria, la notificación al usuario y la presentación del reporte en consola. Al acumular cinco responsabilidades heterogéneas, cualquier cambio en las reglas impositivas, formato de persistencia o plantilla de correo obliga a modificar y recompilar toda la clase. |
| OCP | applyDiscount (if/else sobre customerType) | El método resuelve el tipo de descuento mediante condicionales encadenados (`if/else`) evaluando directamente literales de cadena (`"VIP"`, `"REGULAR"`). La incorporación de una nueva categoría de cliente (ej. corporativo, mayorista o sin descuento) exige modificar la lógica interna de una clase ya probada y desplegada, en lugar de extenderla sin alterar el código existente. |
| DIP | Toda la clase (dependencias internas sin abstracciones) | La clase maneja internamente sus operaciones de almacenamiento y comunicación directa mediante `System.out` sin apoyarse en abstracciones ni permitir la inyección de dependencias. Esto acopla fuertemente el servicio a las implementaciones concretas e impide la realización de pruebas unitarias mediante dobles de prueba (*mocks*). |
