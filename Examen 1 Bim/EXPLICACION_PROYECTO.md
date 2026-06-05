# Explicacion del Proyecto

## 1. Arquitectura utilizada

El proyecto usa MVVM con Repository Pattern. La UI en Compose solo renderiza estado y dispara eventos. Cada pantalla tiene un ViewModel que expone un StateFlow con el estado de pantalla. Los ViewModels dependen de repositorios que encapsulan el acceso a red (JSONPlaceholder) o almacenamiento local (SharedPreferences, DataStore, EncryptedSharedPreferences). Esto separa la logica de negocio de la UI y facilita pruebas y mantenimiento.

## 2. Estados de carga en Modulo 1

El flujo REST usa StateFlow y Coroutines. Al iniciar un GET o PUT, el ViewModel pone isLoading = true. La UI observa ese estado y deshabilita los campos y botones, ademas de mostrar un CircularProgressIndicator. Cuando la respuesta llega (exitosa o con error), el ViewModel actualiza isLoading = false y publica mensajes de exito o error. Asi se evita editar durante la peticion HTTP y se cumple el manejo de loading solicitado.

## 3. EncryptedSharedPreferences y DataStore

- EncryptedSharedPreferences: crea un MasterKey almacenado en Android Keystore. Usa AES256_GCM para cifrar valores y AES256_SIV para cifrar llaves. Los datos se guardan en un archivo de SharedPreferences pero cifrados y con llaves protegidas por el Keystore. El acceso es compatible con el modelo de permisos de Android y evita texto plano.
- DataStore (Preferences): reemplaza SharedPreferences con un almacenamiento asincrono basado en coroutines. Usa un archivo interno y operaciones transaccionales con edit, lo que garantiza consistencia. La lectura se hace con un Flow y la escritura es atomica.

## 4. Cumplimiento de las especificaciones academicas

- Modulo 1 (REST): existe un formulario con un OutlinedTextField numerico para el ID de serie, boton Buscar que ejecuta GET a /posts/{id}, y campos editables para titulo y resumen. El boton Actualizar ejecuta PUT a /posts/{id} y, con respuesta 200, confirma el cambio en pantalla.
- Modulo 1 (Loading): el estado isLoading viene de StateFlow y se maneja con Coroutines. Mientras esta activo, se deshabilitan inputs y botones y se muestra un CircularProgressIndicator.
- Modulo 3 (Preferencias de Series): pantalla separada con inputs de Clave y Valor, selector con tres opciones exactas (SharedPreferences, DataStore, EncryptedSharedPreferences) y botones Guardar/Cargar. Si no existe la clave, se muestra un mensaje generico con Snackbar sin revelar otras llaves.
- UI Compose: toda la interfaz esta hecha en Jetpack Compose.

La tematica se adapto a un catalogo de series y los textos de la UI reflejan ese contexto.
