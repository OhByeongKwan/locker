package nuc.util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Mail {
    private BodyPart getImage(String filename, String contextId) throws MessagingException {
        // 파일을 읽어와서 BodyPart 클래스로 받는다.
        BodyPart mbp = getFileAttachment(filename);
        if (contextId != null) {
            mbp.setHeader("Content-ID", "<" + contextId + ">");
        }
        return mbp;
    }
    private BodyPart getFileAttachment(String filename) throws MessagingException {
        // BodyPart 생성
        BodyPart mbp = new MimeBodyPart();
        // 파일 읽어서 BodyPart에 설정(바운더리 변환)
        File file = new File(filename);
        DataSource source = new FileDataSource(file);
        mbp.setDataHandler(new DataHandler(source));
        mbp.setDisposition(Part.ATTACHMENT);
        mbp.setFileName(file.getName());
        return mbp;
    }

    private BodyPart getContents(String html) throws MessagingException {
        BodyPart mbp = new MimeBodyPart();
        // setText를 이용할 경우 일반 텍스트 내용으로 설정된다.
        // mbp.setText(html);
        // html 형식으로 설정
        mbp.setContent(html, "text/html; charset=utf-8");
        return mbp;
    }


    public void send(String recipient, String code) {

        // 1. 발신자의 메일 계정과 비밀번호 설정
        final String user = "locker@gmail.com";
        final String password = "";

        // 2. Property에 SMTP 서버 정보 설정
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", 465);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // 3. SMTP 서버정보와 사용자 정보를 기반으로 Session 클래스의 인스턴스 생성
        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        // 4. Message 클래스의 객체를 사용하여 수신자와 내용, 제목의 메시지를 작성한다.
        // 5. Transport 클래스를 사용하여 작성한 메세지를 전달한다.

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(user));

            // 수신자 메일 주소
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            // Subject
            message.setSubject("FoxChart - Password Code.");

            // Text
            //message.setText("Foxchart입니다. 비밀번호 재설정을 위한 코드는  ["+code+"] 입니다.");

            //추가 코드
            message.setContent(new MimeMultipart());
            Multipart mp = (Multipart) message.getContent();

            String body = "";
            body += "<div style='float: left; width: 100%; background-color: #1111'>";
            body += "<div style='float: left; width: 100%; margin: 25px 0'>";

            body += "<div style='display: table; margin: 0 auto;'>";
            body += "<div style='text-align:center;color: white;font-weight: bold;font-size: 40px;height: 60px;width: 600px;background-image: linear-gradient(45deg,#FF5B81 0%,#FF5B81 100%);'>폭 스 차 트";
            body += "</div>";
            body += "</div>";

            body += "<div style='display: table; margin: 0 auto;'>";
            body += "<div style='float: left; width: 600px; background-color: #fff; padding: 30px 20px; box-sizing: border-box'>";
            body += "<div style='margin: 15px 0; text-align: center; color:#666; font-size: 17px;'><b>비밀번호 변경을 위한 인증코드.</b></div>";
            body += "<div style='margin: 10px 0; text-align: center; color:#888; font-size: 14px;'></div>";

            body += "<table border='0' style='margin: 25px auto'>";
            body += "<tr><td align='center' bgcolor='#444444' style='width: 200px; border-radius: 10px'>";
            body += "<p style='line-height:40px; text-align:center;'>";
            body += "<a href='' style='color:#FF5B81; font-size:20px; font-weight: bold; text-decoration:none;'>" + code + "</a></p>";
            body += "</td></tr></table>";

            body += "<div style='float: left; width: 100%; margin: 10px 0 20px 0; border-top: 1px solid #ddd'></div>";

            body += "<div style='float: left; width: 100%; color:#888; font-size: 13px; line-height: 1.5em;'>폭스차트 회원이 아닌 경우 이 메일을 무시해주세요.";
            body += "<a href='#' style='color:#FF5B81; opacity:0.7; font-size: 13px; text-decoration:none' target='_blank'> <b> 코드 인증 시 비밀번호 변경이 가능합니다.</div>";
            body += "</div>";
            body += "</div>";

            body += "</div>";
            body += "</div>";

            mp.addBodyPart(getContents(body));

            Transport.send(message);    // send message


        } catch (AddressException e) {
            e.printStackTrace();
            return;
        } catch (MessagingException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }

}