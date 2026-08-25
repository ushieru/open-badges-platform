# 🏅 Branding — Open Badges Platform

> **Sovereign, Immutable, and Standard-Compliant Recognition.**

Guía de identidad visual del proyecto **Open Badges Platform** por **GDG Guadalajara**.
Dirección creativa: **"Sello Soberano" (Sovereign Seal)**.

---

## 1. Concepto de Marca

La plataforma emite y valida insignias digitales bajo el estándar **Open Badges 2.0 (1EdTech)**
con un enfoque en **soberanía**, **inmutabilidad** y **cumplimiento de estándares**.

La identidad se construye sobre la metáfora del **sello soberano**: un emblema hexagonal que
evoca a la vez:

- una **insignia** (la credencial emitida),
- un **sello criptográfico** (la prueba inmutable e inviolable),
- un **sello de certificación** (la validación y autenticidad).

El hexágono remite a estructuras criptográficas y al sello de cera tradicional: un objeto que
nadie puede falsificar sin romperlo. En su interior, una **palomita de verificación dorada**
comunica la validación instantánea y el cumplimiento del estándar.

---

## 2. Logo

### 2.1 Marca (isotipo)

Archivo: `src/main/webui/public/brand/logo-mark.svg`

Un hexágono con gradiente **navy → teal**, un anillo interior (el "límite inmutable") y una
palomita dorada de verificación.

```
  ┌─────────┐
  │  ⬡      │   Sello hexagonal + verificación
  └─────────┘
```

### 2.2 Logo completo (isotipo + logotipo)

Archivo: `src/main/webui/public/brand/logo.svg`

Compuesto por la marca y el logotipo **"Open Badges"** con el sub-título **"PLATFORM"**
en tracking amplio y color teal.

### 2.3 Favicon

Archivo: `src/main/webui/public/favicon.svg`

Versión simplificada de la marca para navegador (32px). Mantiene el hexágono, el anillo y la
palomita con trazos más gruesos para legibilidad a tamaño pequeño.

### 2.4 Reglas de uso

- Mantener un área de respiración igual a la altura del hexágono alrededor de la marca.
- No rotar, distorsionar, aplicar gradientes arbitrarios ni re-colorear la palomita.
- Sobre fondos claros usar la versión a color. Sobre fondos oscuros puede usarse la versión
  con el anillo y la palomita en sus colores originales.

---

## 3. Paleta de Color

Paleta **"Sello Soberano"**: navy profundo + teal + dorado (acento de certificación).

### Primarios

| Token              | Hex       | Uso                                   |
| ------------------ | --------- | ------------------------------------- |
| `navy-900`         | `#0B1B33` | Texto, fondo oscuro, marca            |
| `teal-brand-600`   | `#0E7490` | Acción principal, enlaces             |
| `gold-brand-400`   | `#E8B545` | Acento de certificación / verificación|

### Neutros (modo claro)

| Token      | Hex       |
| ---------- | --------- |
| `base-100` | `#F8FAFC` |
| `base-200` | `#EEF2F7` |
| `base-300` | `#DCE4EC` |
| `base-content` | `#0B1B33` |

### Extendidos

- `teal-brand-400` `#22D3EE` — detalle/resaltado (modo claro y oscuro).
- `navy-950` `#071223` — fondo del tema oscuro.
- `navy-100`…`navy-800` — escala tonal de apoyo.

### Modo oscuro

| Token      | Hex       |
| ---------- | --------- |
| `base-100` | `#071223` |
| `base-200` | `#0B1B33` |
| `base-300` | `#142C49` |
| `primary`  | `#22D3EE` |
| `base-content` | `#E6EEF7` |

---

## 4. Tipografía

| Rol           | Fuente          | Uso                             |
| ------------- | --------------- | ------------------------------- |
| Display       | **Space Grotesk** | Titulares, marca, números grandes |
| Texto         | **Inter**       | Cuerpo, UI, formularios          |
| Mono          | **JetBrains Mono** | Código, JSON-LD, metadatos, hashes |

Reglas:

- Titulares: `font-display`, weight 600–700, tracking ajustado.
- Cuerpo: `font-sans`, weight 400–600.
- Datos técnicos (JSON-LD, SHA-256): `font-mono`.
- Los tokens Tailwind `--font-display`, `--font-sans`, `--font-mono` están definidos en
  `main.css` y generan las utilidades `font-display`, `font-sans`, `font-mono`.

---

## 5. Temas daisyUI

Definidos en `src/main/webui/app/assets/css/main.css`:

- **`openbadges`** (claro, por defecto) — mapa de roles completo (base, primary, secondary,
  accent, neutral, info, success, warning, error).
- **`openbadges-dark`** — variante nocturna del mismo sistema.

Cambio de tema: se activa con un `input.theme-controller` (toggle sol/luna incluido en el
navbar del layout por defecto).

---

## 6. Aplicaciones

- **Navbar / footer:** marca SVG `brand/logo-mark.svg`.
- **Hero de portada:** isotipo centrado + titular en `font-display` con gradiente
  `from-primary to-secondary`.
- **Botones:** `btn-primary` (teal) y `btn-secondary` (navy).
- **Tarjetas:** `bg-secondary` / `bg-primary` con sus respectivos `-content` legibles.
- **Código / JSON-LD:** `font-mono` con fondo `base-300`.

---

## 7. Diseño de Iconos

Para iconografía funcional de la UI se usa el set **Material Symbols** (vía `@nuxt/icon`),
coherente con una estética geométrica y limpia. Los iconos de estado (verificación, seguridad,
idioma, credenciales) complementan la metáfora de "sello de confianza".

---

## 8. Mensaje de Marca (Tagline)

> **"Sovereign, Immutable, and Standard-Compliant Recognition."**
> *Reconocimiento soberano, inmutable y conforme a estándares.*

Tono: confiable, institucional pero moderno, orientado a la transparencia y la integridad
de datos.

---

*Hecho con 💙 por la comunidad de GDG Guadalajara.*
