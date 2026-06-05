# Guión (≈3 minutos) — Taller: App KMP + Compose + Layout Inspector

> **Objetivo del guión:** que suene natural, como si lo estuviera explicando en clase. Puedes leerlo casi tal cual.

---

## 0:00–0:20 — Introducción (qué construí)

Hola, hoy voy a explicar el taller con una app sencilla tipo **biblioteca básica** hecha con **Kotlin Multiplatform (KMP)** y **Compose Multiplatform**.

La idea fue pasar de una plantilla mínima a una app con **arquitectura clara**, **componentes visuales reutilizables** y **renderizado multiplataforma**, pero manteniendo todo simple: sin base de datos real, usando datos en memoria.

---

## 0:20–1:05 — Arquitectura y pantallas (qué piezas tiene)

Para que el proyecto sea entendible, lo organicé por responsabilidades:

- **`model/`**: el modelo, por ejemplo el libro (`Book`).
- **`data/`**: la “fuente de datos” en memoria (`InMemoryLibrary`), con operaciones CRUD.
- **`ui/screens/`**: pantallas completas.
- **`ui/components/`**: componentes visuales pequeños y reutilizables.
- **`platform/`**: cosas específicas por plataforma con `expect/actual`.

Y con eso implementé los 3 flujos del taller:

1) **Listado (Read):** una lista con **imagen**, **título** y **subtítulo**.

2) **Formulario (Create/Update):** inputs de texto y controles como **switches** (y un campo tipo fecha como texto, para mantenerlo básico).

3) **Eliminar (Delete):** un **diálogo de confirmación** y luego un mensaje de éxito tipo **Toast**.

Algo importante es que la navegación la hice simple, con estado: una pantalla “List” y otra “Form”, sin meter librerías extra.

---

## 1:05–3:00 — Parte clave: Layout Inspector y layouts (lo que muestran mis capturas)

Ahora, lo más importante para el análisis: **el Layout Inspector**.

Cuando yo abro el Layout Inspector en una app tradicional con **XML Views**, normalmente el árbol que veo es casi 1 a 1 con lo que puse en pantalla:
- un `LinearLayout` o `ConstraintLayout`,
- dentro `TextView`, `ImageView`, `Button`, etc.

O sea: **cada widget suele ser un “nodo nativo” (una View real)**. Si tu pantalla tiene 30 cosas, es normal ver un árbol grande con muchas Views.

En esta app uso **Compose**, y por eso el inspector tiene dos ideas importantes.

- Por un lado está el **View Tree**, que es el árbol “nativo” de Android (Views). En una app Compose normalmente no se llena de nodos, porque Compose no crea una View por cada Text o cada Card.

- Y por otro lado está el **Compose Tree**, que es donde sí ves la estructura real de la pantalla en Compose.

Entonces, para el taller, cuando me piden **“cuántos nodos nativos”** se crean, yo miro el **View Tree**.

Y ahora sí, lo que se ve en mis capturas:

En la primera captura, a la izquierda aparece el árbol y yo puedo seguirlo como si fuera una lista de “capas” de la pantalla: `App → LibraryApp → MaterialTheme → LibraryListScreen` y luego el `Scaffold`. Y dentro del `Scaffold` se ven cosas claras como el `LazyColumn` (la lista) y el `FloatingActionButton` (el botón de +).

Aquí es donde yo explico el **layout**, o sea, cómo se organiza el espacio:

- El `Scaffold` es como una “estructura base” de pantalla. Me ayuda a separar zonas: el contenido principal, y elementos como el botón flotante. Entonces, en vez de tener todo suelto, está organizado por “secciones” del layout.

- El `LazyColumn` me dice: “esto es una lista vertical”. Y es importante porque no dibuja todo de golpe: va creando y midiendo los ítems según se necesiten en pantalla. Por eso es ideal para listados.

- El `FloatingActionButton` se posiciona sobre el contenido. En la captura se ve como un hijo del `Scaffold`, pero en la pantalla se entiende que es un botón que flota encima de la lista, normalmente abajo a la derecha.

O sea, solo con ver el árbol ya puedo contar una historia clara: primero tengo la pantalla, luego una estructura (`Scaffold`), luego una lista (`LazyColumn`), y encima un botón flotante.

En la segunda captura, yo selecciono un elemento de la lista, por ejemplo `BookListItem`. Ahí se ve que ese `BookListItem` por dentro está usando un `Card` y un `Row`. Y a la derecha aparece la parte que a mí más me ayuda para explicar la implementación: **Parameters**.

Y aquí me detengo un poquito en el layout interno del ítem, porque es justo lo que pide el taller:

- El `Card` es el contenedor visual: me da la idea de “tarjeta” para cada libro.

- El `Row` significa que adentro, los elementos van **en horizontal**. Entonces, lo típico es: a la izquierda un bloque de imagen o color, y a la derecha el texto.

- Normalmente, dentro de ese `Row` también hay una `Column` para apilar el **título** y el **subtítulo** en vertical. Aunque no siempre lo ves expandido en el árbol, la idea se entiende: `Row` organiza horizontal; `Column` organiza vertical.

Y algo que yo puedo mencionar cuando lo estoy presentando es: “Compose no solo dibuja; primero **mide** y después **coloca**”. Entonces el inspector sirve para ver qué pieza está controlando el espacio.

En **Parameters** puedo mostrar, con palabras simples, “qué le llega” a ese componente:
- el `book` (el libro que está renderizando),
- el `modifier`,
- y los eventos como `onClick`, `onLongPress` y `onDeleteClick`.

El punto del `modifier` es clave para hablar de layouts, porque el `modifier` es donde casi siempre pongo cosas como:
- tamaño (por ejemplo `height(88.dp)`),
- ancho (`fillMaxWidth()`),
- padding,
- y comportamiento de interacción (click, long press).

Entonces, cuando en la derecha me sale por ejemplo el **height = 88dp**, yo puedo decir: “este item de la lista tiene una altura fija, y por eso todos los ítems se ven uniformes”.

Algo chévere es que esos eventos salen como un enlace a líneas del archivo donde se definieron (por ejemplo `LibraryListScreen.kt:70`, `:71`, `:72`). Entonces yo puedo decir: “esto que estoy tocando aquí en el inspector, viene exactamente de esta parte del código”.

Y en la tercera captura aparece otra herramienta: **Recomposition counts**.

Cuando yo activo **Recomposition counts**, el inspector empieza a mostrar **números a la derecha** del árbol. Y eso responde a tu pregunta: esos números son el conteo de cuántas veces se **recompuso** cada composable desde la última vez que presioné **Reset**.

Entonces, cuando yo aplasto “Eliminar”, como se abre el diálogo o cambia el estado del ítem, Compose vuelve a ejecutar ciertas partes de la UI, y por eso esos números aparecen o suben. No es un error: es una forma visual de ver “qué se volvió a dibujar” cuando cambió el estado.

Con esto cierro la idea: con el Layout Inspector yo puedo explicar la **estructura del layout** (Scaffold → lista → ítems), puedo ver cómo está hecho cada ítem (Card, Row), y puedo comprobar comportamiento (eventos y recomposición) cuando interactúo, por ejemplo al presionar “Eliminar”.
