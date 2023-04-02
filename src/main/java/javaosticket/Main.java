package javaosticket;

import javax.mail.AuthenticationFailedException;

public class Main {

    public static void main(String[] args) {
        GmailReader reader = new GmailReader();
        boolean sendTicket = false; // Decido si enviar el correo como ticket
        try {
            reader.readEmailsAndCreateTickets(sendTicket);
        } catch (AuthenticationFailedException ex) {
            System.err.println("FALLÓ LA AUTENTICACION AL CORREO");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
