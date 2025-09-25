TuMatchApp
=================

Resumen rápido
--------------
Esta versión incluye:
- Login y Registro locales (SessionManager, SharedPreferences, contraseñas guardadas como hash SHA-256).
- Pantalla principal (bienvenida) con botón para buscar recomendaciones.
- Flujo de Preferencias -> Resultados -> Detalle de producto usando la API Fake Store (https://fakestoreapi.com/products).
- UI adaptada a Material (TextInputLayout, MaterialButton, MaterialToolbar, MaterialCardView).
- RecyclerView para la lista de productos, con adaptador (`ProductAdapter`).
- Carga de imágenes simple con `ImageLoader` (hilo propio) — se puede reemplazar por Glide/Picasso.

Archivos importantes añadidos/modificados
----------------------------------------
- app/src/main/java/com/example/tumatchapp_prueba/
  - SessionManager.java (gestiona usuarios y sesión local)
  - LoginActivity.java, RegisterActivity.java
  - PreferencesActivity.java (buscar por término/categoría)
  - ResultsActivity.java (llama a FakeStore y muestra resultados con RecyclerView)
  - ProductAdapter.java (Adapter para RecyclerView)
  - ProductDetailActivity.java (detalle del producto, AppBar con navegación)
  - Product.java (modelo simple)
  - ImageLoader.java (carga de imágenes en background)
- app/src/main/res/layout/ (layouts nuevos/actualizados para Login/Register/Main/Preferences/Results/Detalle)
- app/src/main/AndroidManifest.xml (se añadió permiso INTERNET y nuevas activities)
- app/build.gradle.kts (añadidas dependencias: RecyclerView, CardView)

Cómo probar localmente
----------------------
1. Abrir el proyecto en Android Studio.
2. (Opcional) Desde terminal en la raíz del proyecto:

```cmd
cd C:\Users\Litzi09\AndroidStudioProjects\TuMatchApp_prueba
gradlew.bat assembleDebug
``` 

3. Ejecutar la app en un emulador/dispositivo desde Android Studio.
4. Flujo de prueba:
   - Al abrir la app, si no hay sesión activa, verás la pantalla de Login.
   - Pulsa "¿No tienes cuenta? Regístrate" para crear un usuario (email + contraseña).
   - Tras registrarte entrarás a la pantalla principal; pulsa "Buscar recomendaciones" para ir a Preferencias.
   - Elige categoría o deja en "Todas" y pulsa Buscar: se mostrarán los productos desde FakeStore.
   - Pulsa en un producto para ver su detalle.

Notas técnicas y advertencias
----------------------------
- Autenticación local: no es segura para producción. Recomiendo Firebase Auth o un backend propio para gestión de usuarios.
- `ImageLoader` es intencionalmente simple (sin cache). Para producción usar Glide o Picasso.
- Las contraseñas se almacenan con SHA-256 (sin salt). Es mejor usar un backend y no almacenar hashes del lado cliente.
- Algunas advertencias del linter sobre `notifyDataSetChanged()` y visibilidad de ViewHolder son informativas y no bloquean la compilación.

Siguientes pasos que puedo hacer ahora
------------------------------------
- Reproducir el diseño exacto de Figma: para eso necesito que me compartas los valores exactos (paleta hex), la familia de fuentes o los archivos .ttf/.otf y los assets (logo, iconos) que quieras usar.
- Mejorar la carga de imágenes reemplazando `ImageLoader` por Glide (añade caching y manejo de errores).
- Añadir validaciones de email y requisitos de contraseña (mínimo 8 caracteres, etc.).
- Añadir almacenamiento de preferencias del usuario (categorías favoritas).
- Añadir tests unitarios básicos para `SessionManager`.

Dime cuál de los siguientes prefieres y lo implemento:
- A) "Aproximar ahora" — sigo puliendo la UI para que luzca mejor usando valores por defecto (tipografías, espaciado, sombras).
- B) "Replicar Figma exactamente" — me pasas paleta, fuente(s) y assets y yo los incorporo.
- C) "Mejorar rendimiento de imágenes" — cambio `ImageLoader` a Glide.

Si quieres que haga cualquiera de las opciones (o varias), dime cuál y empiezo.

