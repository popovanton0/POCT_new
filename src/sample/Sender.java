package sample;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

public class Sender {

    private String username;
    private String password;
    private Properties props;

    public Sender(String username, String password) {
        this.username = username;
        this.password = password;

        props = new Properties();
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
    }

    public void send(String subject, String text, String fromEmail, String toEmail, Logger log) {
        try {
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

            //Пакет для вложений
            Multipart multipart = new MimeMultipart("mixed");
            //Текст сообщения в пакет
            MimeBodyPart textAttachment = new MimeBodyPart();
            textAttachment.setText(text);
            multipart.addBodyPart(textAttachment);

            //Логи в пакет
            String[] attachments = listFiles(System.getProperty("user.dir") + "\\sample\\logs\\", log);
            for (int attlen = 0; attlen < attachments.length; attlen++)
            {
                MimeBodyPart messageAttachment = new MimeBodyPart();

                String fileName = attachments[attlen];

                messageAttachment.attachFile(System.getProperty("user.dir") + "\\sample\\logs\\" + fileName);
                messageAttachment.setFileName("logFile" + fileName + ".log");
                multipart.addBodyPart(messageAttachment);
            }

            Message message = new MimeMessage(session);
            //Добавляем пакет в сообщение
            message.setContent(multipart);

            //от кого
            message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //тема сообщения
            message.setSubject(subject);
            //отправка
            Transport.send(message);

        } catch (Exception e) {
            log.warning(Main.getStackTrace(e));
        }
    }
        public String[] listFiles(String directoryName, Logger log){
            String[] fileNames = new String[0];
            try {
                File directory = new File(directoryName);

                File[] fList = directory.listFiles();
                fileNames = new String[fList.length - 1];
                for (int i = 0; i < fList.length - 1; i++){
                    File file = fList[i];
                    if (file.isFile()){
                        fileNames[i] = file.getName();
                    }
                }
            } catch (Exception e) {
                log.warning(Main.getStackTrace(e));
            }
            return  fileNames;
        }

}