# **TP - Algoritmos y Estructura de Datos I**

![class-diagram](/class-diagram.png "Class Diagram")

Se desea construir una librería que permita **manipular** y **analizar**
**datos** en forma tabular (2 dimensiones) para el lenguaje Java. Deberá
ofrecer **estructuras de datos** y operaciones que soporten la
funcionalidad solicitada, como así también contemple posibles
extensiones futuras, minimizando el impacto ante alguna modificación.

En principio no es necesario hacer foco en la eficiencia de las
operaciones, pero sería deseable disponer también de algún mecanismo que
nos permita **cuantificar**, al menos en tiempo, el **costo de su
ejecución**.

Es posible apoyarse en **estructuras existentes de forma nativa** en el
lenguaje Java para construir las estructuras de datos, pero **no** se
debe **depender de librerías externas**. En caso de necesitar consumir
alguna librería externa, se debe consultar previamente si es viable su
aplicación.

## **Objetivos y Alcance**

- Crear un paquete que permita la **lectura de distintos tipos de archivos** y darle un formato con encabezados y registros. También realizar operaciones con el conjunto de datos.

- Este paquete, en principio, **será compatible con** los tipos de archivos **.csv**, **.json** y **.xlsx**.

## **Descripción de alto nivel del sistema**

El sistema consiste en una librería desarrollada en Java para la
manipulación y análisis de **datos tabulares**. Su principal
funcionalidad es ofrecer una estructura de datos que permita la
**gestión de tablas** con soporte para varias operaciones, incluyendo
**generación**, **modificación**, **selección**, **filtrado** y
**visualización de datos**, entre otras.

**Estructura**

- El sistema **manipulará datos en 2 dimensiones** (filas y columnas), y permitirá asociar **etiquetas a filas y columnas**, además de soportar varios tipos de datos como números, cadenas y booleanos.

- Se implementarán métodos para la manipulación de los datos, incluyendo **acceso indexado**, carga/descarga desde archivos CSV, y la **creación de vistas parciales** (slicing) de los datos.

- Se debe permitir la modificación de datos mediante la **inserción y eliminación de filas y columnas**, así como la **imputación de valores faltantes** y el muestreo aleatorio de filas.

**Operaciones principales**

- Acceso a datos a través de etiquetas de fila y columna.

- Modificación de datos a nivel de celda, fila y columna.

- Operaciones de selección y filtrado basadas en condiciones lógicas.

- Ordenamiento y concatenación de estructuras tabulares.

- Visualización de los datos en formato tabular con límites configurables.

**Mantenibilidad y Extensibilidad**

El sistema está diseñado bajo principios de Programación Orientada a
Objetos (POO), lo que asegura su **capacidad de extensión y
mantenimiento**, permitiendo la incorporación de nueva funcionalidad sin
mayores cambios en el código base. Además, debe tener un enfoque en la
gestión de errores, utilizando **excepciones para manejar casos de
errores** en el tiempo de ejecución.

**Futuras extensiones**

El sistema contempla posibles extensiones, como la **agregación de datos
mediante operaciones estadísticas** sobre grupos de filas, para futuras
versiones.

## **Requerimientos Funcionales**

**Carga y manipulación de datos**

- Permitir la carga de datos desde archivos en formato CSV.

- Soportar operaciones CRUD: creación, lectura, actualización y eliminación de tablas.

- Implementar operaciones comunes de transformación de datos, como filtrado, ordenamiento, y concatenación.

- Proveer acceso indexado a las filas, columnas y celdas, tanto por etiquetas numéricas como por etiquetas de cadenas.

- Operaciones de imputación: permitir rellenar celdas con valores faltantes (NA).

- Soportar operaciones estadísticas básicas: suma, promedio, máximo, mínimo, etc.

- Operar con tipos de datos soportados: numéricos, booleanos y cadenas.

**Operaciones de consultas**

- Permitir búsquedas indexadas por filas y columnas.

- Soportar operaciones de selección y filtrado de datos, incluyendo la visualización parcial de la estructura tabular a través de _head(x)_ y _tail(x)_.

**Operaciones de exportación**

- Soportar la exportación de estructuras tabulares a formato CSV.

**Manejo de errores**

- Validar tipos de datos al momento de manipular las tablas.

- Manejar de manera robusta los valores nulos o faltantes.

## **Requerimientos No Funcionales**

**Rendimiento**

- El sistema debe poder manejar grandes volúmenes de datos sin comprometer la velocidad en operaciones comunes como filtros y ordenamientos.

**Escalabilidad**

- Diseñar la librería para que sea fácilmente extensible y adaptable a futuras mejoras o nuevos tipos de datos.

**Usabilidad**

- Proveer una API amigable, sencilla de utilizar y bien documentada, con ejemplos claros de uso.

**Portabilidad**

- La librería debe ser compatible con distintos sistemas operativos (Windows, macOS, Linux).

**Mantenibilidad**

- El código debe estar modularizado para facilitar su mantenimiento y actualización.
