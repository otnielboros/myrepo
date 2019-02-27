package ro.yuhuu.backend.yubackend.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;


public class MailSender extends Thread {

    private Properties props;
    private Session sessionGmail;
    private String destination, subject, content;
    final String mailUser="yuhuuteam@gmail.com";
    final String mailPassword="Bubueb0$$";


    public MailSender(String destination, String subject, String content) {

        this.content=content;
        this.destination = destination;
        this.subject = subject;

        props=getPropertiesConfigEmail(mailUser);
        sessionGmail=Session.getInstance(props,new GMailAuthenticator(mailUser,mailPassword));
    }

    private Properties getPropertiesConfigEmail(String mail) {
        Properties props=new Properties();
        props.put("mail.smtp.user", mail);
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        return props;
    }

    class GMailAuthenticator extends Authenticator {
        String email;
        String pasword;
        public GMailAuthenticator (String username, String password)
        {
            super();
            this.email = username;
            this.pasword = password;
        }
        public PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(email, pasword);
        }
    }


    @Override
    public void run() {
        sendMail();
    }


    public void sendMail() {
        try {
            MimeMessage message=new MimeMessage(sessionGmail);
            message.setFrom(new InternetAddress(mailUser));
            message.addRecipients(Message.RecipientType.TO,InternetAddress.parse(destination));
            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(content);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport transport = sessionGmail.getTransport("smtps");
            transport.connect("smtp.gmail.com", 465, mailUser, mailPassword);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        }catch (MessagingException msg){
            throw new RuntimeException(msg);
        }
    }
}
