package dto;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Jordano
 */
public class TicketDTO {
    /*
    {
    "alert": true,
    "autorespond": true,
    "source": "API",
    "name": "Angry User",
    "email": "api@osticket.com",
    "phone": "3185558634X123",
    "subject": "Testing API",
    "ip": "123.211.233.122",
    "message": "data:text/html,MESSAGE <b>HERE</b>",
    "attachments": [
        {"file.txt": "data:text/plain;charset=utf-8,content"},
        {"image.png": "data:image/png;base64,R0lGODdhMAA..."}
    ]
}
    */
    private boolean sendTicket; // campo de control. no pertenece a la doc.

    private boolean alert;
    private boolean autorespond;
    private String source;
    private String name;
    private String email;
    private String phone;
    private String subject;
    private String ip;
    private String message;
    private List<Map<String, String>> attachments;
    
     // Constructor
    public TicketDTO(boolean alert, boolean autorespond, String source, String name, String email,
            String phone, String subject, String ip, String message, List<Map<String, String>> attachments,boolean sendTicket) {
        this.alert = alert;
        this.autorespond = autorespond;
        this.source = source;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.subject = subject;
        this.ip = ip;
        this.message = message;
        this.attachments = attachments;
        this.sendTicket = sendTicket;
    }

    // Métodos para obtener y establecer valores para los atributos privados
    public boolean isAlert() {
        return alert;
    }

    private void setAlert(boolean alert) {
        this.alert = alert;
    }

    public boolean isAutorespond() {
        return autorespond;
    }

    private void setAutorespond(boolean autorespond) {
        this.autorespond = autorespond;
    }

    public String getSource() {
        return source;
    }

    private void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    private void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSubject() {
        return subject;
    }

    private void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIp() {
        return ip;
    }

    private void setIp(String ip) {
        this.ip = ip;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, String>> getAttachments() {
        return attachments;
    }

    private void setAttachments(List<Map<String, String>> attachments) {
        this.attachments = attachments;
    }

    public boolean isSendTicket() {
        return sendTicket;
    }

    public void setSendTicket(boolean sendTicket) {
        this.sendTicket = sendTicket;
    }
}
    


