# Taller: Evolución de las Herramientas de Desarrollo Móvil

**Objetivo:** Entender cómo han evolucionado las herramientas de desarrollo móvil, por qué surgieron, cómo funcionan, y analizar su relevancia en la era de nuevas interfaces como foldables y AR.

---

## 📚 PARTE 1: EL VIAJE ARQUITECTÓNICO DEL DESARROLLO MÓVIL

### 1. Desarrollo Nativo Puro (iOS/Android) - El Principio (2007-2012)

#### 🏗️ **Arquitectura**
- **iOS:** Swift/Objective-C → UIKit/SwiftUI → Metal GPU
- **Android:** Java/Kotlin → Android Framework → OpenGL ES

#### 💡 **¿Por qué surgió?**
- Las primeras apps necesitaban máximo rendimiento
- Acceso directo a APIs del sistema operativo
- No había abstracciones intermedias (todo era plataforma-específico)

#### 🔧 **Cómo funciona**
```
Usuario → App Nativa (Swift/Kotlin) → Framework del SO → Kernel → Hardware
                    ↓ Acceso directo ↓
        Cámara, GPS, Acelerómetro, etc.
```

**Flujo de renderizado:**
- UIViewController (iOS) / Activity (Android)
- Cálculos en CPU
- Rendering en GPU mediante Metal (iOS) u OpenGL ES (Android)
- Actualización por frame (60 FPS en ese entonces)

#### 📊 **Tecnologías y Lenguajes**

| Plataforma | Lenguaje | Framework | GPU | Relevancia 2026 |
|-----------|----------|-----------|-----|-----------------|
| iOS | Swift/Obj-C | UIKit/SwiftUI | Metal | ⭐⭐⭐⭐⭐ (Alto rendimiento) |
| Android | Kotlin/Java | Android XML/Jetpack | OpenGL ES/Vulkan | ⭐⭐⭐⭐⭐ (Standard) |

#### ✅ **Ventajas**
- Máximo rendimiento
- Acceso completo a HW (sensores, cámara, etc.)
- UX nativa y natural
- Mejor batería

#### ❌ **Desventajas**
- Código duplicado (iOS + Android)
- Curva de aprendizaje por SO
- Actualización lenta de features entre plataformas

#### 📱 **Ejemplos de Apps**
- Instagram (nativo inicial)
- Snapchat
- Uber (versión 1.0)

---

### 2. La Era del WebView / Híbridos (2011-2017)

#### 🌐 **Arquitectura**
```
App Wrapper (Android/iOS) 
    ↓
WebView (Chromium/Safari Engine)
    ↓
HTML/CSS/JavaScript
    ↓
Device APIs via Bridge
```

#### 💡 **¿Por qué surgió?**
- Presión del mercado: necesitaban apps iOS Y Android rápidamente
- Desarrolladores web querían ingeniería móvil sin aprender Swift/Kotlin
- Reducir tiempo de desarrollo = más barato

#### 🔧 **Tecnologías Principales**

| Framework | Año | Lenguaje | Motor | Estado 2026 |
|-----------|-----|----------|-------|------------|
| **PhoneGap** | 2011 | HTML/CSS/JS | WebView | ❌ Deprecado |
| **Cordova** | 2012 | HTML/CSS/JS | WebView | ⚠️ Mantenimiento |
| **Ionic** (v1) | 2013 | AngularJS + Cordova | WebView | ⚠️ Legacy |
| **Ionic** (v2+) | 2016 | Angular/React/Vue + Capacitor | WebView | ⭐⭐⭐ (Activo) |

#### 🔌 **Cómo funciona el Bridge**

```javascript
// JavaScript en WebView
navigator.geolocation.getPosition(success, error);

        ↓ (Bridge nativo)

// Android/iOS ejecuta código nativo
CLLocationManager.requestLocation()

        ↓ (Callback)

// Resultado vuelve a JavaScript
success({ latitude: 10.5, longitude: -75.2 })
```

**Flujo de Rendering:**
- JavaScript engine (V8/JavaScriptCore) → Cálculos
- DOM manipulation → CSS styling
- WebView rendering → Pintura de píxeles
- **Problema:** Un frame tarda más (No es nativo en velocidad)

#### 📊 **Arquitectura Interna: Cordova**

```
HTML/CSS/JS Code
        ↓
WebView Container
        ↓
Cordova Plugins
        ↓
Native Android/iOS APIs
```

#### ✅ **Ventajas**
- Código compartido entre iOS y Android
- Desarrolladores web pueden hacer mobile
- Deploy rápido

#### ❌ **Desventajas**
- Performance: 30-50% más lento que nativo
- Animaciones lentas
- Acceso limitado a HW
- Actualización lenta del WebView
- **No es realmente "cross-platform":** Todavía hay que compilar por plataforma

#### 📱 **Ejemplos**
- LinkedIn (híbrido inicial)
- Slack (antes de versiones nativas)
- Trello

---

### 3. El Puente Nativo-JavaScript: React Native y Flutter (2015-2020)

#### 🌉 **Arquitectura: React Native**

```
JavaScript Code (React syntax)
        ↓
React Native Bridge
        ↓
Componentes Nativos (UIView/Android View)
        ↓
Native Rendering
```

#### 💡 **¿Por qué surgió?**
- Meta (Facebook) necesitaba actualizar apps rápidamente
- "Learn once, write anywhere" no "Write once, run anywhere"
- Performance mejor que WebView, pero desarrollo más rápido que nativo puro

#### 🔧 **React Native - Cómo Funciona**

**Flujo interno:**
1. **Metro Bundler** empaqueta código React/JavaScript
2. **Bridge** comunica JS thread con Native thread
3. **Components mapping:** `<View>` → `UIView` (iOS) / `ViewGroup` (Android)
4. **Eventos:** Touch events → JS → State update → Re-render nativo

**Threading Model:**
```
JavaScript Thread (Single Thread)
    ↓
    Bridge (Serialización JSON)
    ↓
Native Thread (UIThread/MainThread)
    ↓
GPU Rendering
```

#### 📊 **React Native vs Flutter vs Nativo**

| Métrica | Nativo | React Native | Flutter |
|---------|--------|--------------|---------|
| **Performance** | 100% | ~80-90% | ~90-95% |
| **Startup Time** | ~100ms | ~500-800ms | ~200-400ms |
| **APK/Bundle Size** | ~5MB | ~25-40MB | ~15-20MB |
| **HotReload** | ❌ | ✅ | ✅ |
| **Código Compartido** | ❌ | ~70% | ~95% |

#### 🔧 **Flutter - Arquitectura Diferente**

```
Flutter App Code (Dart)
        ↓
Flutter Engine (C++)
        ↓
Skia Graphics Engine
        ↓
Platform Channel
        ↓
Native APIs (Kotlin/Swift)
```

**Diferencia clave con React Native:**
- React Native: Usa componentes nativos del SO
- Flutter: **Dibuja TODO** (UI widgets, animaciones) usando Skia
- Skia es un motor 2D que funciona como un "canvas" - igual rendering en iOS/Android

#### 📊 **Tecnologías**

| Framework | Lenguaje | Motor Render | Relevancia 2026 |
|-----------|----------|--------------|-----------------|
| **React Native** | JavaScript (JSBridge) | Componentes Nativos | ⭐⭐⭐⭐ (Muy usado) |
| **Flutter** | Dart | Skia (propio) | ⭐⭐⭐⭐⭐ (Creciente) |
| **Xamarin** | C# | Componentes Nativos | ⚠️ Migración a MAUI |
| **NativeScript** | TypeScript | Componentes Nativos | ⭐⭐ (Nicho) |

#### ✅ **Ventajas**
- Mejor performance que WebView
- Código compartido real (especialmente Flutter)
- HotReload para development
- Acceso a APIs nativas más fácil

#### ❌ **Desventajas**
- Más peso que nativo (~30-40MB)
- Bridge serializa datos (overhead)
- Ciertos features nativos aún requieren código específico de plataforma

#### 📱 **Ejemplos**
- **React Native:** Facebook, Instagram, Discord, Shopify
- **Flutter:** Google Pay, Alibaba, eBay, Tencent

---

### 4. El Motor de Renderizado Propio: WebAssembly + Canvas (2018-Presente)

#### 🎨 **Arquitectura**

```
TypeScript/Rust Code
        ↓
WebAssembly (WASM)
        ↓
Canvas API / GPU context
        ↓
Pixel rendering
```

#### 💡 **¿Por qué surgió?**
- Necesidad de control total sobre el rendering
- WebAssembly permite código de alta performance
- Juegos y apps exigentes (Figma, photoshop)

#### 🔧 **Cómo Funciona en Mobile**

**Tauri + React/Vue en Desktop** se adapta a:
- **React Native Web:** Mismo código React en web, iOS, Android
- **Expo:** Manage React Native sin Xcode/Android Studio

**Más experimental: Capacitor + WebAssembly**
```
HTML5 Canvas API
        ↓
WebAssembly (Rust/C++)
        ↓
GPU (Metal/Vulkan)
```

#### 🔧 **Flutter Web (Experimental)**
```
Dart App
        ↓
Dart to JavaScript (dart2js) o WebAssembly
        ↓
HTML Canvas / SkiaWeb
        ↓
Browser Rendering
```

#### ✅ **Ventajas**
- Control total del rendering
- Performance cercana a nativo
- Código compartido web/mobile

#### ❌ **Desventajas**
- Stack muy nuevo
- Menos librerías disponibles
- WASM aún no está optimizado en todos lados

---

### 5. El Futuro: Foldables, AR y Wearables (2020+)

#### 📱 **Paradigma Actual: Una Pantalla Plana**

Todos los frameworks anteriores asumen:
- Tamaño de pantalla FIJO (en orientación específica)
- Ratio de aspecto constante
- Ciclo de vida lineal: onCreate → onResume → onPause → onDestroy

#### 🔄 **Los Foldables Rompen ESTO**

**Ej: Samsung Galaxy Z Fold**
```
Estado 1: Cerrado      Estado 2: Abierto
┌─────────┐            ┌──────────────────┐
│         │            │                  │
│ 6.2" 21:9│      →    │ 7.6" 17.4:13      │
│         │            │                  │
└─────────┘            └──────────────────┘
    Teléfono                  Tablet
```

**Desafíos:**
- **Estado transicional:** Durante 500ms el dispositivo cambia
- **Datos persistentes:** Ej: scroll position, formularios parcialmente llenos deben preservarse
- **Layouts reactivos:** Componentes deben re-distribuirse en milisegundos

**¿Qué frameworks lo soportan?**

| Framework | Soporte Foldables | Tecnología |
|-----------|------------------|-----------|
| **Kotlin Multiplatform** | ✅ Native, completo control | Coroutines + StateFlow |
| **Jetpack Compose** (Android) | ✅ Window Size Classes | Composable adaptativos |
| **SwiftUI** (iOS) | ⚠️ ProMotion + Scene Phases | `@Environment(\.scenePhase)` |
| **React Native** | ⚠️ Comunidad (RN Foldable lib) | Custom bridge |
| **Flutter** | ⚠️ Plugin (flutter_displayfeature) | MediaQuery adaptativos |
| **Web** | ⭐⭐ CSS Media Queries (Ventana browser) | `@media (screen-fold-*)` |

**Ejemplo: Compose (Android)**
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WindowSizeClass()
        }
    }
}

@Composable
fun WindowSizeClass() {
    val windowSize = rememberWindowSizeClass()
    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            // Layout para teléfono
            Column { /*...*/ }
        }
        WindowWidthSizeClass.Expanded -> {
            // Layout para tablet/foldable abierto
            Row { /*...*/ }
        }
    }
}
```

---

#### 🥽 **AR/XR: Realidad Aumentada y Extendida**

**Requisitos técnicos:**
- Cálculos matemáticos 3D en tiempo real
- Latencia < 10ms (caso contrario, motion sickness)
- Renderizado 60-120 FPS mínimo
- Reconocimiento de objetos en tiempo real

**¿Por qué frameworks cross-platform NO funcionan bien?**

| Framework | AR Nativo | Problema |
|-----------|-----------|----------|
| **React Native** | ❌ Muy overhead bridge | Bridge no puede manejar streams 3D a 120FPS |
| **Flutter** | ⚠️ Plugin, pero lento | Skia no es para 3D |
| **Unity** | ✅✅✅ Diseñado para esto | Motor 3D nativo |
| **Unreal** | ✅✅✅ Diseñado para esto | Motor 3D nativo |
| **ARCore/ARKit Nativo** | ✅ La mejor opción | Sin overhead |

**Stack recomendado para AR:**
```
Vision Processing (ARCore/ARKit) → 120 FPS
        ↓
Tracking 3D / Pose Estimation
        ↓
Renderizado 3D (OpenGL ES / Metal)
        ↓
Composite en pantalla
```

**Ejemplos AR en producción:**
- **Snapchat:** ARCore/ARKit nativo + OpenGL
- **Pokemon GO:** Unity (motor 3D especializado)
- **Instagram Filters:** WebGL en WebView
- **IKEA Place:** Swift/Kotlin + ARKit/ARCore

---

#### ⌚ **Wearables (Smartwatches)**

**Desafíos únicos:**
- Pantalla pequeña (1.4")
- Batería limitadísima
- Conectividad intermitente

| Plataforma | Framework | Lenguaje | Battery Impact |
|-----------|-----------|----------|-----------------|
| **Wear OS** | Kotlin Compose for Wear | Kotlin | Mejor ⭐⭐⭐⭐ |
| **watchOS** | SwiftUI | Swift | Mejor ⭐⭐⭐⭐ |
| **Tizen** | Proprietary HTML/JS | HTML5 | Media ⭐⭐⭐ |
| **React Native** | react-native-watch | JS | Peor ⭐⭐ |

---

## 🔍 PARTE 2: ANÁLISIS DE RELEVANCIA 2026

### Cuadro Comparativo Completo

```
┌─────────────────┬──────────┬──────────┬──────────┬──────────┬──────────┐
│ Framework       │ Perfor.  │ DX       │ Comunidad│ Foldable │ AR/XR    │
├─────────────────┼──────────┼──────────┼──────────┼──────────┼──────────┤
│ Nativo (Swift)  │ ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐   | ⭐⭐⭐⭐  | ✅       | ✅✅✅   │
│ Nativo (Kotlin) │ ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐   | ⭐⭐⭐⭐  | ✅✅✅   | ✅✅✅   │
│ React Native    │ ⭐⭐⭐    | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⚠️       | ❌       │
│ Flutter         │ ⭐⭐⭐⭐  | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐  | ⚠️       | ❌/⚠️    │
│ Ionic/Capacitor │ ⭐⭐     | ⭐⭐⭐    | ⭐⭐⭐    | ⚠️       | ❌       │
│ Unity (Game)    │ ⭐⭐⭐⭐⭐ | ⭐⭐⭐    | ⭐⭐⭐⭐⭐ | ✅       | ✅✅✅   │
│ Unreal          │ ⭐⭐⭐⭐⭐ | ⭐⭐     | ⭐⭐⭐⭐  | ✅       | ✅✅✅   │
└─────────────────┴──────────┴──────────┴──────────┴──────────┴──────────┘
```

### Escenarios de Uso 2026

| Caso de Uso | Recomendado | Por qué |
|------------|-------------|--------|
| **App de Productividad** | Nativo (Swift/Kotlin) o React Native | Necesita UX excelente, nativa feel |
| **Prototipo MVP rápido** | Flutter o React Native | TTM corto, buen DX |
| **App empresarial compleja** | Kotlin Multiplatform | Máximo control, máxima performance |
| **Juego 3D** | Unity o Unreal | Diseñados para esto |
| **Experiencia AR** | ARKit/ARCore nativo + OpenGL | Sin overhead, latencia baja |
| **Foldable-first** | Kotlin Compose o SwiftUI | Soporte nativo de layouts adaptativos |
| **Wearable** | Kotlin/Swift específico | Optimizado para batería |
| **PWA mobile** | Capacitor + React/Vue + WASM | Si no necesitas App Store |

---

## 💡 CONCLUSIONES

### 1. **No hay una bala de plata**
- Elegir framework es un trade-off entre rendimiento, TTM, y comunidad
- En 2026, la decisión depende del **problema**, no del framework

### 2. **Los frameworks cross-platform maduraron**
- React Native: 9 años en producción, usado por Meta, Discord, Shopify
- Flutter: 5 años, usado por Google, Ebay, Alibaba
- Pero aún no reemplazan nativo para apps de máximo rendimiento

### 3. **Las nuevas interfaces exigen nuevas arquitecturas**
- **Foldables:** Reactive state management (Kotlin Flow/Swift Combine)
- **AR/XR:** No hay sustituto para nativo; necesitas motor 3D
- **Wearables:** Stack específico y ultraoptimizado

### 4. **Tendencia 2026-2030**
```
2024: React Native, Flutter dominan cross-platform
         ↓
2025: Kotlin Multiplatform (KMP) crece exponencialmente
         ↓
2026+: Híbrido emergente
- Nativo para UI crítica (Foldables, AR)
- Cross-platform para módulos de negocio
- Arquitectura modular (microfrontends móviles)
```

### 5. **La realidad sin hype**
- **Nativo puro seguirá siendo:** Más rápido, mejor UX, pero más caro
- **Cross-platform ahora es:** Maduro, eficiente en costo, satisfactorio (no perfecto)
- **Nueva frontera:** Arquitecturas modulares que mezclen nativo + cross-platform

---

## 📖 REFERENCIAS

### Documentation Oficial
- [Flutter Documentation](https://flutter.dev/docs)
- [React Native Documentation](https://reactnative.dev/docs)
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [ARCore (Google)](https://developers.google.com/ar)
- [ARKit (Apple)](https://developer.apple.com/arkit/)

### Articulos Clave
- "Flutter vs React Native 2026" - The Software House
- "Foldable Devices Architecture" - Android Developers Blog
- "WebAssembly in Production" - WebAssembly.org

---

## 🎯 PREGUNTAS DE REFLEXIÓN

1. ¿Por qué Meta sigue invirtiendo en React Native si tienen acceso a desarrolladores nativos ilimitados?
2. Si Flutter dibuja todo con Skia, ¿Por qué no es 100% idéntico en iOS y Android?
3. ¿En qué año crees que más del 50% de apps móviles serán cross-platform?
4. ¿Qué tecnología desplazará a React Native/Flutter en los próximos 5 años?
5. ¿Puede un desarrollador full-stack web construir experiencias AR/VR de calidad sin aprender un motor 3D?

---

**Última actualización:** Abril 2026  
**Autor:** Taller de Desarrollo Móvil - MOV  
**Institución:** Escuela Politécnica Nacional
