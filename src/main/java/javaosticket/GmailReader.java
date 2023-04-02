package javaosticket;

import dto.TicketDTO;
import dto.Auth;
import Core.Controlador;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

public class GmailReader {

    public void readEmailsAndCreateTickets(boolean sendTicket) throws Exception {

        // crea una sesión de correo con las credenciales proporcionadas
        Properties props = new Properties();
        props.setProperty("mail.imap.ssl.enable", "true");
        Session session = Session.getInstance(props, null);
        Store store = session.getStore("imaps");
        store.connect(Auth.HOST, Auth.MAIL, Auth.PASS);

        // abre la carpeta "Inbox"
        Folder inbox = store.getFolder("Inbox");
        inbox.open(Folder.READ_WRITE); // para marcarlos como leido posteriormente
        FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);

        // Bucle para evitar que el programa culmine
        while (true) {
            // lee los mensajes del buzón de entrada
            Message[] Messages = inbox.search(ft);
            // verifica que haya por lo menos mas de un mensaje
            if (Messages.length == 0) {
                Thread.sleep(5000);
                continue;
            }
            for (Message message : Messages) {
                try {
                    //obtiene el ultimo mensaje
                    //Muesta datos del correo
                    Controlador.show_email_data(message);
                    //Crea el ticket con las valicaciones
                    OsticketAPI.createTicket(new TicketDTO(
                            true,
                            true,
                            "API",
                            Controlador.get_name_data(message),
                            Controlador.get_email_data(message),
                            Controlador.get_phone_data(message),
                            Controlador.get_subject_data(message),
                            Controlador.get_ip_data(message),
                            Controlador.get_message_data(message),
                            Controlador.get_attachments_data(message),
                            sendTicket
                    ));
                    message.setFlag(Flags.Flag.SEEN, true);//marca como leido

                } catch (Exception e) {
                    e.printStackTrace();
                    // Si da error, el correo se marca como favorito para ser revisado manualmente
                     message.setFlag(Flags.Flag.FLAGGED, true);
                     message.setFlag(Flags.Flag.SEEN, true);//marca como leido
                    continue;
                }
            }
            Thread.sleep(5000);
        }
    }

}
