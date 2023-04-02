package Core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class Controlador {

    private static String saveDirectory = "/home/nek/Documentos/org/1-Personal/0-Proyectos Clientes/Estudiantes-Universidad"; // directory to save the downloaded documents

    public static String convertBinario(String ruta) {
        try {
            //return  new BASE64Encoder().encode(Files.readAllBytes(new File(ruta).toPath())); //otra forma
            return java.util.Base64.getEncoder().encodeToString(Files.readAllBytes(new File(ruta).toPath()));

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void show_email_data(Message message) throws MessagingException, IOException {
        System.out.println("---------------------------------");
        System.out.println("Subject: " + message.getSubject());
        System.out.println("From: " + message.getFrom()[0]);
        System.out.println("Text: " + get_message_data(message));
    }

    public static String get_email_data(Message message) throws MessagingException, IOException {
        Address[] from = message.getFrom();
        String email = ((InternetAddress) from[0]).getAddress();
        if (email == null) {
            email = "No se pudo obtener la direccion del correo";
        }
        return email;
    }

    public static String get_name_data(Message message) throws MessagingException, IOException {
        Address[] from = message.getFrom();
        String name = ((InternetAddress) from[0]).getPersonal();
        if (name == null) {
            name = "No se pudo obtener el nombre del correo";
        }
        return name;
    }

    public static String get_phone_data(Message message) throws MessagingException, IOException {
        String phone = null; // por ahora no se puede obtener
        if (phone == null) {
            phone = "0999999999"; // JsonSchema permite solo formato de celular
        }
        return phone;
    }

    /**
     * Leo el body del correo y dependiendo del tipo de contenido que es su
     * Multipart, leo y seteo variables Multipatr son las secciones del correo
     * de tipo de contenido. En definicion permite a los usuarios enviar y
     * recibir correos con varios tipos de contenido, como texto, imágenes y
     * archivos adjuntos, todo en un solo mensaje. Un multipart pudede ser:
     * mixed: significa que su contenido es tener mas multipart
     * related: Hay que recorrerlo de nuevo el body part para ver si es text
     * alternative:lo mismo que el related
     */
    public static String get_message_data(Message message) throws MessagingException, IOException {

        String msg = null;
        String contentType = message.getContentType();
       //System.out.println("content type: " + contentType);
        if (contentType.toLowerCase().contains("multipart")) {
            // es un mensaje multipart
            Multipart multiPart = (Multipart) message.getContent();
            for (int partCount = 0; partCount < multiPart.getCount(); partCount++) {

                BodyPart bodyPart = multiPart.getBodyPart(partCount);

               // System.out.println(bodyPart.getContentType());

                if (bodyPart.getContentType().toLowerCase().contains("multipart")) {
                    // obtiene el contenido multipart/alternative de la parte del cuerpo
                    MimeMultipart mpAlternative = (MimeMultipart) bodyPart.getContent();
                    for (int j = 0; j < mpAlternative.getCount(); j++) {
                        BodyPart bp = mpAlternative.getBodyPart(j);
                       // System.out.println(bp.getContentType());
                        // verifica si la parte del cuerpo es de tipo "text/plain" o "text/html"
                        if (bp.isMimeType("text/plain") || bp.isMimeType("text/html")) {
                            // obtiene el cuerpo del correo electrónico
                            String body = (String) bp.getContent();
                            msg = body;
                            break; //solo deseo el primero (text o html)
                        } else if (bp.getContentType().toLowerCase().contains("multipart")) {
                            MimeMultipart mpAlternative2 = (MimeMultipart) bp.getContent();

                            for (int k = 0; k < mpAlternative2.getCount(); k++) {
                                BodyPart bp2 = mpAlternative2.getBodyPart(k);
                               // System.out.println("interno: " + bp2.getContentType());
                                // verifica si la parte del cuerpo es de tipo "text/plain" o "text/html"
                                if (bp2.isMimeType("text/plain") || bp2.isMimeType("text/html")) {
                                    // obtiene el cuerpo del correo electrónico
                                    String body = (String) bp2.getContent();
                                    msg = body;
                                    break; //solo deseo el primero (text o html)
                                }
                            }
                        }
                    }
                }

                if (bodyPart.getContentType().toUpperCase().startsWith("TEXT")) {
                    msg = bodyPart.getContent().toString();//   
                    break; // Si es Texto, solo necesito el primer mensaje
                    // procesar el contenido del mensaje
                }
            }
            // Si el cuerpo solo es texto plano o con estilos, ejecuta esto
        } else if (contentType.toUpperCase().contains("TEXT/PLAIN") || contentType.toUpperCase().contains("TEXT/HTML")) {
            msg = message.getContent().toString();
            // procesar el contenido del mensaje
        }
        if (msg == null) {
            msg = "No se pudo obtener el mensaje del correo";
        }
        return "data:text/html,MESSAGE <b>" + msg
                + "</b>";
    }

    public static String get_subject_data(Message message) throws MessagingException, IOException {
        String subject = message.getSubject();
        if (subject == null) {
            subject = "No se pudo obtener el Tema del correo";
        }
        return subject;
    }

    public static String get_ip_data(Message message) throws MessagingException, IOException {
        try {
            String[] receivedHeaders = message.getHeader("Received");
            String lastReceivedHeader = receivedHeaders[receivedHeaders.length - 1];
            String[] parts = lastReceivedHeader.split("\n"); //separo por cada linea
            // Separo por ] ya que la ip esta -> ([ip])
            String part2line = parts[1].substring(3, parts[1].indexOf("]"));
            String ip = part2line;
            InetAddress address = InetAddress.getByName(ip);
            return address.getHostAddress();
        } catch (Exception ex) {
            return "localhost";
        }
    }

    private static Map<String, String> llenarAttachment(Map<String, String> attachment, BodyPart bodyPart, String fileName, MimeBodyPart part) throws MessagingException, IOException {
        // obtiene la imagen embebida al cuerpo y la guarda en un archivo
        InputStream is = bodyPart.getInputStream();

        String rutacompleta = saveDirectory + File.separator + fileName;

        OutputStream os = new FileOutputStream(rutacompleta);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        is.close();

        String tipoContenido = part.getContentType();
        tipoContenido = tipoContenido.substring(0, tipoContenido.indexOf(";"));
        tipoContenido = "data:" + tipoContenido.toLowerCase();

        if (tipoContenido.contains("image")) {
            tipoContenido = tipoContenido + ";base64";
        }

        part.saveFile(rutacompleta);
        String archivoconvertido = convertBinario(rutacompleta);

        attachment.put(fileName, tipoContenido + "," + archivoconvertido);

        return attachment;
    }

    public static List<Map<String, String>> get_attachments_data(Message message) throws MessagingException, IOException {
        Map<String, String> attachment = new HashMap<String, String>();
        List<Map<String, String>> attachments = new ArrayList();

        String messageContent = "";
        String attachFiles = "";
        String rutacompleta = "";
        String fileName = "";
        String archivoconvertido = "";

        if (message.getContentType().contains("multipart")) {
            Multipart multiPart = (Multipart) message.getContent();
            int numberOfParts = multiPart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                attachment = new HashMap<String, String>();

                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                BodyPart bodyPart = multiPart.getBodyPart(partCount);

                // El body part que sea tipo imagen, lo guardo en la carpeta
                // Esto se ejecuta si hay una imagen en el cuerpo
                if (bodyPart.isMimeType("image/png") && !Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    fileName = "imagen_body_" + partCount + ".jpg";
                    attachment = llenarAttachment(attachment, bodyPart, fileName, part);
                    attachments.add(attachment);
                    attachFiles += fileName + ", ";
                } else if (bodyPart.getContentType().toLowerCase().contains("multipart") && !Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    // obtiene el contenido multipart/alternative de la parte del cuerpo
                    MimeMultipart mpAlternative2 = (MimeMultipart) bodyPart.getContent();
                    for (int j = 0; j < mpAlternative2.getCount(); j++) {
                         attachment = new HashMap<String, String>();
                        MimeBodyPart part2 = (MimeBodyPart) mpAlternative2.getBodyPart(j);
                        BodyPart bp2 = mpAlternative2.getBodyPart(j);
                        // verifica si la parte del cuerpo es de tipo "text/plain" o "text/html"
                        if (bp2.isMimeType("image/png") || bp2.isMimeType("image/jpg")) {
                            fileName = "imagen_body_" + j + ".jpg";
                            attachment = llenarAttachment(attachment, bp2, fileName, part2);
                            attachments.add(attachment);
                            attachFiles += fileName + ", ";
                        } else if (bp2.getContentType().toLowerCase().contains("multipart")) {
                            MimeMultipart mpAlternative3 = (MimeMultipart) bp2.getContent();

                            for (int k = 0; k < mpAlternative3.getCount(); k++) {
                                 attachment = new HashMap<String, String>();
                                MimeBodyPart part3 = (MimeBodyPart) mpAlternative3.getBodyPart(k);
                                BodyPart bp3 = mpAlternative3.getBodyPart(k);
                                // verifica si la parte del cuerpo es de tipo "text/plain" o "text/html"
                                if (bp2.isMimeType("image/png") || bp2.isMimeType("image/jpg")) {
                                    fileName = "imagen_body_" + k + ".jpg";
                                    attachment = llenarAttachment(attachment, bp3, fileName, part3);
                                    attachments.add(attachment);
                                    attachFiles += fileName + ", ";
                                }
                            }
                        }
                    }
                }
                

                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    // this part is attachment
                    fileName = part.getFileName();
                    String tipoContenido = part.getContentType();
                    tipoContenido = tipoContenido.substring(0, tipoContenido.indexOf(";"));
                    tipoContenido = "data:" + tipoContenido.toLowerCase();

                    if (tipoContenido.contains("image")) {
                        tipoContenido = tipoContenido + ";base64";
                    } else {
                        tipoContenido = tipoContenido.concat(";charset=utf-8");
                    }

                    attachFiles += fileName + ", ";

                    part.saveFile(saveDirectory + File.separator + fileName);
                    rutacompleta = saveDirectory + File.separator + fileName;
                    archivoconvertido = convertBinario(rutacompleta);

                    attachment.put(fileName, tipoContenido + "," + archivoconvertido);
                    attachments.add(attachment);
                }
            }
            if (attachFiles.length() > 1) {
                System.out.println("Archivos: ");
                attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                System.out.println(attachFiles);
            }
        }
        return attachments;
    }

}
