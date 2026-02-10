# App Android de Gestión de Datos con Clean Architecture

Este proyecto es mi implementación de una aplicación Android nativa, desarrollada como parte de mi formación en ViewNext. El objetivo principal no era la funcionalidad en sí, sino demostrar un dominio profundo de las **arquitecturas de software modernas** y las **buenas prácticas de desarrollo** en un entorno profesional.

## 📸 Showcase Visual

Aquí se puede ver el flujo principal de la aplicación, desde el dashboard hasta el listado y el sistema de filtrado.

<img width="1920" height="1080" alt="Image" src="https://github.com/user-attachments/assets/ace5172e-ae61-47e2-a2b1-2c2cd0f8c090" />

## 🚀 Probar la Aplicación (APK)

Se ha generado una versión `release` de la aplicación para que pueda ser instalada y probada en un dispositivo Android.

➡️ **[Descargar la última versión desde Releases](https://github.com/Ethzal/practicaviewnext/releases/latest)**

## ✨ Funcionalidades y Mejoras Clave

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-blueviolet)
![Clean Architecture](https://img.shields.io/badge/Clean%20Architecture-100%25-blue)
![Room DB](https://img.shields.io/badge/Room%20Database-✅-green)
![Hilt DI](https://img.shields.io/badge/Hilt%20DI-✅-orange)

*   **Migración completa Java → Kotlin (100%).**

*   **Arquitectura limpia y modular:**
Implementación estricta de Clean Architecture con capas domain, data y presentation.
Repositorios e interfaces claramente separados para mejorar la mantenibilidad y testeo.

*   **Gestión de datos robusta:**
Integración de Room Database para persistencia local.
Repositorios que combinan API y almacenamiento local de forma eficiente.
Manejo de LiveData para reflejar cambios en la UI de manera reactiva.

*   **Mejoras en la UI/UX:**
Skeleton shimmer loading en la lista de facturas.
Rotación circular de mocks con Retromock para pruebas visuales.

## 🏛️ Arquitectura y Principios de Diseño

La base de este proyecto es una implementación estricta de **Clean Architecture**, separando el código en tres capas principales:

*   **Capa de Presentación (Presentation):** Implementada con el patrón **MVVM (Model-View-ViewModel)**. Se encarga de la lógica de la UI y de la gestión del estado, utilizando componentes como `ViewModel` y `LiveData`.
*   **Capa de Dominio (Domain):** Contiene la lógica de negocio pura y los casos de uso. Es independiente de cualquier framework, lo que la hace 100% testeable.
*   **Capa de Datos (Data):** Gestiona el origen de los datos, ya sea de una fuente remota (API) o local. Implementa el patrón Repositorio para abstraer el origen de los datos.

Este enfoque garantiza un código **desacoplado, escalable, mantenible y altamente testeable.**

## 📁 Estructura de Módulos

EnergyApp/
- **app/** — Presentation + DI (Hilt)
- **data/** — Room + Retrofit + Retromock
- **domain/** — Use Cases + Models + Interfaces (Pure Logic)
- **presentation/** — Activities + Fragments + ViewModels

## 🧪 Testing y Calidad de Código

Una de las prioridades de este proyecto fue asegurar la calidad y la robustez del código a través de **tests unitarios**.

*   **Frameworks Utilizados:** Se utilizó **JUnit 4** para la estructura de los tests y **Mockito** para crear objetos mock y simular las dependencias (como los Casos de Uso).
*   **Componentes Testeados:** La cobertura en Domain Layer (Use Cases) y de presentación, específicamente los `ViewModel`, para validar la lógica de negocio y la correcta actualización del estado de la UI a través de `LiveData`.
*   **Técnicas Avanzadas:** Se utilizó `InstantTaskExecutorRule` para manejar los componentes de Arquitectura de Android fuera del hilo principal y se aplicó **reflexión** para la inyección de dependencias en un entorno de testing.

El código de los tests se puede encontrar en el directorio `/domain/src/test/`.

## 🛠️ Stack Tecnológico y Librerías

| Componente               | Tecnología / Librería                      |
|:-------------------------|:-------------------------------------------|
| **Lenguaje**             | `Kotlin 100%` *(migrated from Java)*       |
| **Arquitectura**         | `Clean Architecture`, `MVVM`               |
| **Inyección Dependencias**| `Hilt`                                    |
| **Base de Datos**        | `Room Database`                            |
| **Networking**           | `Retrofit 2`, `Gson`, `Retromock`          |
| **Interfaz de Usuario**  | `XML`, `Material Design`, `Shimmer`        |
| **Control de Versiones** | `Git`, `GitHub Flow`                       |
| **Testing**              | `JUnit 4`, `Mockito`, `AndroidX Test Core` |

## 💡 Lecciones Aprendidas

Esta fue una experiencia de aprendizaje profundo donde pude aplicar en un entorno práctico conceptos teóricos complejos. Los mayores aprendizajes fueron:

1.  **El valor de un código desacoplado:** La facilidad para modificar o testear una parte de la aplicación sin afectar a las demás.
2.  **Gestión del estado de la UI:** El uso de `ViewModel` y `LiveData` para crear interfaces robustas y resistentes a los cambios de configuración.
3.  **Flujo de trabajo profesional con Git:** La importancia de trabajar con ramas (`feature-branches`) y realizar Pull Requests para un desarrollo ordenado.
4.  **Testing unitario:** La importancia crítica del testing unitario para validar la lógica de forma aislada y permitir refactorizar el código con seguridad.
