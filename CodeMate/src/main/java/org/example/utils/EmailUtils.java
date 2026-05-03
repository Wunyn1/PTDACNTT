package org.example.utils;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Date;
import java.util.Properties;

public class EmailUtils {
    private static final String email_regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String emailFrom = "sakuraminekochan@gmail.com";
    private static final String password = "yhxtlucitucjlwse";

    public static void sendEmail(String emailTo,String subject, String OTP) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, password);
            }
        };

        Session session = Session.getInstance(props, authenticator);

        try{
            MimeMessage message = new MimeMessage(session);

            message.setFrom(emailFrom);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo, false));
            message.setSubject(subject, "UTF-8");
            message.setSentDate(new Date());
            message.setText(OTP, "UTF-8");

            Transport.send(message);

        } catch (MessagingException e) {
            System.out.println("Lỗi ở đây");
            e.printStackTrace();
        }
    }

    public static boolean isValidEmail(String email) {
        return email.matches(email_regex);
    }
}
