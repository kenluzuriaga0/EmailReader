# EmailReader ✉️
Procesa los correos no-leidos uno a uno obteniendo su secciones mas importantes.

## Funcionalidad:
- Obtener Servidor de correo, ip, asunto y adjuntos.
- Obtener Cuerpo del correo. Soporta imagenes embebidas y html. 🖼️
- Lee todos los correos no leidos y se marcan como leido una vez procesado (lo envia por un web service) ✅
- Si da error en procesamiento, el correo se marca como favorito (para ser revisado manualmente) ⭐


## Conceptos:
### Multipart en java-mail:
**multipart/mixed:** mensajes que contienen varios tipos de contenido combinados en una sola entidad. Por ejemplo, un mensaje de correo electrónico que contiene tanto texto como imágenes. En este caso, el contenido se combina en una sola entidad de mensaje.

**multipart/related:** mensajes que contienen varios tipos de contenido relacionados entre sí. Por ejemplo, un mensaje de correo electrónico que contiene tanto texto como imágenes, donde las imágenes están relacionadas con el contenido del mensaje. En este caso, se utiliza una entidad de mensaje principal para el texto y otras entidades para cada imagen relacionada.

**multipart/alternative:** mensajes que contienen diferentes versiones del mismo contenido en diferentes formatos. Por ejemplo, un mensaje de correo electrónico que contiene una versión en texto sin formato y otra versión en HTML. En este caso, se utilizan varias entidades de mensaje para cada versión del contenido y se incluye una cabecera que indica la preferencia del cliente de correo electrónico para el formato que debe mostrarse.
