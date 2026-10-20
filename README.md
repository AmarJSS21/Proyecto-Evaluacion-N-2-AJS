# GeoRegistro - App de Registro de Incidencias

Hola, este es mi proyecto para la Evaluación 2 de Taller de Nuevas Tecnologías. La aplicación se llama **GeoRegistro** y está pensada para resolver el problema de reportar problemas o incidencias en terreno (por ejemplo, durante una inspección). Permite tomar los datos de la incidencia, capturar la ubicación actual con el GPS, guardar todo en la nube con Firebase y ver los reportes marcados en un mapa.

---

## ¿Qué hace la aplicación?

La app se divide en tres pantallas principales y cumple con lo siguiente:

1. *Pantalla principal y menú:** Permite navegar de forma sencilla hacia el formulario de registro, el listado de incidencias guardadas y la vista del mapa.
2. *Formulario de registro y GPS:** Pide un título, descripción, categoría, fecha y hora. Al presionar el botón de ubicación, solicita el permiso de GPS al usuario en tiempo de ejecución. Si se acepta, obtiene la latitud y longitud actuales de forma automática. Si el usuario llega a rechazar el permiso, la app le muestra un aviso y le permite escribir las coordenadas manualmente para no perder el reporte. También valida que los campos no queden vacíos antes de guardar.
3. *Base de datos con Firebase Firestore:** Todos los formularios guardados se suben a una colección en Firestore en tiempo real. Si el teléfono se queda sin conexión a internet, la aplicación atrapa la excepción y muestra un mensaje informando el problema sin cerrarse de manera inesperada.
4. *Google Maps:** Muestra un mapa interactivo que lee los reportes guardados en la base de datos y coloca marcadores en las coordenadas de cada incidencia, mostrando su título al tocar sobre ellos.
5. *Sensor Acelerómetro:** Aproveché el sensor de movimiento del teléfono para una función práctica: si agitas el dispositivo mientras estás en el formulario, se detecta el movimiento brusco y se limpian todos los campos para iniciar un nuevo reporte desde cero. En caso de que el teléfono no tenga acelerómetro, la app lo detecta al inicio y simplemente desactiva la función para no fallar.

---

## Consideraciones de Seguridad

Para cuidar las claves privadas y cumplir con la pauta de entrega, este repositorio no incluye credenciales ni claves secretas:
* Archivos como `local.properties` (donde se configura la API Key de Google Maps) y `google-services.json` (de Firebase) están añadidos en el `.gitignore`.
* Las reglas de seguridad en Firestore están ajustadas para exigir la estructura del proyecto y evitar accesos no autorizados.

---

## ¿Cómo probar el proyecto?

Si quieres clonar y compilar este proyecto localmente:
1. Clona el repositorio con `git clone`.
2. Crea tu propio proyecto en Firebase Console y descarga el archivo `google-services.json` dentro de la carpeta `app/`.
3. Crea el archivo `local.properties` en la raíz de tu proyecto e incluye tu clave de Google Maps así:
   MAPS_API_KEY=tu_clave_aqui
4. Sincroniza Gradle en Android Studio y ejecuta la app.