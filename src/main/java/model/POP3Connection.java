package model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;

import org.apache.james.mime4j.message.*;

import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


import javax.mail.*;
import javax.mail.internet.MimeUtility;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import static main.Main.PATH;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.Properties;

public class POP3Connection {
    private SSLSocket socket = null;
    private BufferedReader inputStream = null;
    private BufferedWriter outputStream = null;
    private String response;
    private Message message;
    private String resultMessage;
    private StringBuffer txtPart;
    private StringBuffer htmlPart;
    private ArrayList<BodyPart> attachments;
    private StringBuilder mailHeader;
    private javax.mail.Message[] messages;
    private ObservableList<String> headers;
    private Logger logger = Logger.getLogger(POP3Connection.class);


    public POP3Connection() {

        Properties log4jProperties = new Properties();
        log4jProperties.setProperty("log4j.rootLogger", "INFO, stdout,file");
        log4jProperties.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        log4jProperties.setProperty("log4j.appender.stdout.Target", "System.out");
        log4jProperties.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        log4jProperties.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
        log4jProperties.setProperty("log4j.appender.file", "org.apache.log4j.RollingFileAppender");
        log4jProperties.setProperty("log4j.appender.file.File", "log_file.log");
        log4jProperties.setProperty("log4j.appender.file.MaxFileSize", "5MB");
        log4jProperties.setProperty("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
        log4jProperties.setProperty("log4j.appender.file.layout.ConversionPattern", "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");

        PropertyConfigurator.configure(log4jProperties);

    }


    public void connect(String host, int port) throws POP3ConnectionException {
        try {

            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

            socket = (SSLSocket) factory.createSocket(host, port);

            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            createResponse();

        } catch (IOException e) {
            throw new POP3ConnectionException("Cannot connect to server");
        }
    }

    public void disconnect() throws POP3ConnectionException {
        if (!isConnected()) {
            throw new POP3ConnectionException("Not connected");
        }
        try {

            socket.close();
        } catch (IOException e) {
            throw new POP3ConnectionException("Cannot disconnect from server");
        }
        inputStream = null;
        outputStream = null;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public void createResponse() throws POP3ConnectionException {
        response = null;
        StringBuilder result = new StringBuilder();
        try {
            String data = inputStream.readLine();

            result.append(data);
            result.append("\n");
            response = result.toString();
            if (response.startsWith("-ERR")) {
                throw new POP3ConnectionException(response);
            }
        } catch (IOException e) {
            throw new POP3ConnectionException("Error while recieving response");
        }

    }


    public String getResponse() {
        return response;
    }


    public String readResponseLine() throws POP3ConnectionException {

        createResponse();

        return getResponse();
    }

    public String getAllResponseLines(String response) throws POP3ConnectionException {
        StringBuilder multiResponse = new StringBuilder();


        multiResponse.append(response);

        while (!(response = readResponseLine()).equals(".\n")) {
            multiResponse.append(response);
        }


        return multiResponse.toString();
    }


    public void sendCommand(String command) throws POP3ConnectionException {
        try {
            outputStream.write(command);
            outputStream.flush();
            createResponse();


        } catch (IOException e) {
            throw new POP3ConnectionException("Error while sending command");
        }
    }

    public String getResultMessage() {
        return resultMessage;
    }


    public void createMessage() throws POP3ConnectionException {

        ByteArrayInputStream bais = null;
        try {
            txtPart = new StringBuffer();
            htmlPart = new StringBuffer();
            attachments = new ArrayList<>();

            String str1 = new String(getAllResponseLines(getResponse()).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

            bais = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));

            message = new Message(bais);

            StringBuilder fullMessage = new StringBuilder();

            fullMessage.append("Message:\n");
            fullMessage.append("Date: " + message.getDate());
            fullMessage.append("\n");
            fullMessage.append("From: " + message.getFrom());
            fullMessage.append("\n");
            fullMessage.append("To: " + message.getTo());
            fullMessage.append("\n");
            fullMessage.append("Subject: " + message.getSubject());
            fullMessage.append("\n");


            if (message.isMultipart()) {
                Multipart multipart = (Multipart) message.getBody();
                parseBodyParts(multipart);
            } else {
                String text = getTxtPart(message);
                txtPart.append(text);
            }

            fullMessage.append(txtPart.toString());

            fullMessage.append("\n");

            resultMessage = fullMessage.toString();

            txtPart.delete(0, txtPart.length());

            htmlPart.delete(0, htmlPart.length());
        } catch (IOException e) {
            throw new POP3ConnectionException("Error while creating message");
        } finally {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    throw new POP3ConnectionException("Errors while reading message");
                }
            }
        }
    }

    /**
     * функция получения текстовой части сообщения электронной почты
     * @param part часть сообщения электронной почты
     * @return строку текста сообщения
     * @throws IOException ошибка ввода-вывода
     */
    private String getTxtPart(Entity part) throws IOException {

        TextBody tb = (TextBody) part.getBody();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        tb.writeTo(baos);

        return new String(baos.toByteArray(), tb.getMimeCharset());
    }

    /**
     *
     * @param multipart
     * рекурсивно обрабатывает части сообщения электронной почты
     * @throws IOException ошибка ввода-вывода
     */

    protected void parseBodyParts(Multipart multipart) throws IOException {
        for (BodyPart bodyPart : multipart.getBodyParts()) {
            if (bodyPart.isMimeType("text/plain")) {
                String txt = getTxtPart(bodyPart);
                txtPart.append(txt);
            } else if (bodyPart.isMimeType("text/html")) {
                String html = getTxtPart(bodyPart);
                htmlPart.append(html);
            } else if (bodyPart.getDispositionType() != null && !bodyPart.getDispositionType().equals("")) {
                attachments.add(bodyPart);
            }
            if (bodyPart.isMultipart()) {
                parseBodyParts((Multipart) bodyPart.getBody());
            }
        }
    }

    public void saveMessage(String parameter) throws POP3ConnectionException {

        File folder = new File(PATH + "/message " + parameter);

        if (!folder.exists()) {
            boolean create = folder.mkdir();
        }
        try {
            for (BodyPart attach : attachments) {

                File file = new File(folder, attach.getFilename());

                FileOutputStream fos = new FileOutputStream(file);


                try {
                    BinaryBody binaryBody = (BinaryBody) attach.getBody();
                    binaryBody.writeTo(fos);
                } finally {

                    fos.close();

                }
            }
            FileUtils.writeStringToFile(new File(folder, "message.eml"), resultMessage, "UTF-8");
        } catch (IOException e) {
            throw new POP3ConnectionException("Error while saving message");
        }

        attachments.clear();
    }

    /**
     * функция получения заголовков ссобщений электронной почты
     * @param host адрес хоста
     * @param port номер порта
     * @param username адрес электронной почты пользователя
     * @param password пароль электронной почты пользователя
     * @return массив строк, содержащий заголовки сообщений
     */

    public ObservableList<String> getMailHeaders(String host, String port, String username, String password) {

        try {
            Properties properties = new Properties();

            properties.put("mail.pop3.port", port);
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            Store store = emailSession.getStore("pop3s");

            store.connect(host, username, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            messages = emailFolder.getMessages();

            mailHeader = new StringBuilder();

            headers = FXCollections.observableArrayList();

            for (int i = 0, n = messages.length; i < n; i++) {
                javax.mail.Message message = messages[i];
                mailHeader.append(i + 1 + "\t");
                mailHeader.append(message.getSentDate() + "\t");
                mailHeader.append(MimeUtility.decodeText(message.getFrom()[0].toString()) + "\t");
                mailHeader.append(message.getSubject());

                String header = mailHeader.toString();

                headers.add(header);

                mailHeader.delete(0, mailHeader.length());
            }

            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return headers;
    }

}
