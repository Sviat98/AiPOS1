package model;

import com.rac021.charset.validator.CharsetDetector;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.field.address.Mailbox;
import org.apache.james.mime4j.io.BufferedLineReaderInputStream;
import org.apache.james.mime4j.io.LineReaderInputStream;
import org.apache.james.mime4j.io.LineReaderInputStreamAdaptor;
import org.apache.james.mime4j.message.*;

import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.BodyDescriptorBuilder;
import org.apache.james.mime4j.stream.MimeConfig;
import org.apache.james.mime4j.util.ByteArrayBuffer;
import sun.misc.IOUtils;
import tech.blueglacier.email.Attachment;
import tech.blueglacier.email.Email;
import tech.blueglacier.parser.CustomContentHandler;

import javax.mail.*;
import javax.mail.internet.MimeUtility;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.print.DocFlavor;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class POP3Connection {
   private SSLSocket socket = null;
    private BufferedReader inputStream = null;
    private BufferedWriter outputStream = null;
    private String response;
    private Message message;
    private String resultMessage;
    private StringBuffer txtPart ;
    private StringBuffer htmlPart;
    private ArrayList<BodyPart> attachments;
    private StringBuilder mailHeaders;





    public POP3Connection(){


       }

       public void connect(String host, int port) throws POP3ConnectionException{
           try {

               SSLSocketFactory factory=(SSLSocketFactory) SSLSocketFactory.getDefault();

               socket=(SSLSocket) factory.createSocket(host,port);

               inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));


               outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


               createResponse();


           }
           catch (IOException e){
               throw new POP3ConnectionException("Cannot connect to server");
           }
       }

       public void disconnect() throws POP3ConnectionException
       {
           if(!isConnected()){
               throw new POP3ConnectionException("Not connected");
           }
           try {

               socket.close();
           }
           catch (IOException e){
               throw new POP3ConnectionException("Cannot disconnect from server");
           }
           inputStream=null;
           outputStream = null;
       }

       public boolean isConnected(){
        return socket != null && socket.isConnected();
       }

    public void createResponse() throws POP3ConnectionException{
        response = null;
        StringBuilder result = new StringBuilder();
        try{
            String data = inputStream.readLine();

            result.append(data);
            result.append("\n");
            response = result.toString();
            if(response.startsWith("-ERR")){
                throw new POP3ConnectionException(response);
            }
        }
        catch (IOException e){
            throw new POP3ConnectionException("Error while recieving response");
        }


    }



    public String getResponse() {
        return response;
    }


    public String readResponseLine() throws POP3ConnectionException{

            createResponse();

            return getResponse();
    }

    public String getAllResponseLines(String response) throws POP3ConnectionException{
        StringBuilder multiResponse = new StringBuilder();



        multiResponse.append(response);

        while(!(response=readResponseLine()).equals(".\n")){
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
                    throw new POP3ConnectionException("Error while sending command") ;
                }
            }

    public String getResultMessage() {
        return resultMessage;
    }



    public void createMessage() throws POP3ConnectionException{
        ByteArrayInputStream bais = null;
        try{
            txtPart = new StringBuffer();
            htmlPart = new StringBuffer();
            attachments = new ArrayList<>();

            bais = new ByteArrayInputStream(getAllResponseLines(getResponse()).getBytes("UTF-8"));


            message = new Message(bais);


            StringBuilder fullMessage = new StringBuilder();


            fullMessage.append("Message:\n");
            fullMessage.append("Date: "+message.getDate());
            fullMessage.append("\n");
            fullMessage.append("From: "+message.getFrom());
            fullMessage.append("\n");
            fullMessage.append("To: "+message.getTo());
            fullMessage.append("\n");
            fullMessage.append("Subject: "+message.getSubject());
            fullMessage.append("\n");


            if(message.isMultipart()){
                Multipart multipart = (Multipart) message.getBody();
                parseBodyParts(multipart);
            }
            else{
                String text = getTxtPart(message);
                txtPart.append(text);
            }


            fullMessage.append(txtPart.toString());


            fullMessage.append("\n");

            resultMessage = fullMessage.toString();


            txtPart.delete(0,txtPart.length());

            htmlPart.delete(0,htmlPart.length());

        }
        catch ( IOException e){
            throw new POP3ConnectionException("Error while creating message");
        }
        finally {
            if(bais != null){
                try{
                    bais.close();
                }
                catch(IOException e){
                    throw new POP3ConnectionException("Errors while reding message");
                }
            }
        }

    }

    private String getTxtPart(Entity part) throws IOException{

            TextBody tb = (TextBody) part.getBody();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();


            tb.writeTo(baos);

        String some = baos.toString("UTF-8");

        baos.close();


            return some;
    }

    private void parseBodyParts(Multipart multipart) throws IOException{
            for(BodyPart bodyPart : multipart.getBodyParts()){
                if(bodyPart.isMimeType("text/plain")){
                    String txt = getTxtPart(bodyPart);
                    txtPart.append(txt);
                }
                else if(bodyPart.isMimeType("text/html")){
                    String html = getTxtPart(bodyPart);
                    htmlPart.append(html);
                }
                else if(bodyPart.getDispositionType() != null && !bodyPart.getDispositionType().equals("")){
                    attachments.add(bodyPart);
                }
         if(bodyPart.isMultipart()){
           parseBodyParts((Multipart) bodyPart.getBody());
         }
            }
    }

    public void  saveMessage(String parameter) throws POP3ConnectionException{

        File folder = new File("C:\\message "+parameter);


        if(!folder.exists()){
            boolean create  = folder.mkdir();
        }
        try{
            for(BodyPart attach : attachments){

                File file = new File(folder,attach.getFilename());

                FileOutputStream fos = new FileOutputStream(file);


                try{
                    BinaryBody binaryBody = (BinaryBody) attach.getBody();
                    binaryBody.writeTo(fos);
                }
                finally {

                    fos.close();


                }
            }
            FileUtils.writeStringToFile(new File(folder,"message.eml"),resultMessage,"UTF-8");

        }
        catch (IOException e){
            throw new POP3ConnectionException("Error while saving message");
        }


        attachments.clear();


    }

   public String getMailHeaders(String host, String port, String username,String password){

        try {

            //create properties field
            Properties properties = new Properties();

            //properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", port);
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(host, username, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            javax.mail.Message[] messages = emailFolder.getMessages();

            mailHeaders = new StringBuilder();
            for (int i = 0, n = messages.length; i < n; i++) {
                javax.mail.Message message = messages[i];
               mailHeaders.append(i+1+"\t");
               mailHeaders.append(message.getSentDate()+"\t");
               mailHeaders.append(MimeUtility.decodeText(message.getFrom()[0].toString())+"\t");
               mailHeaders.append(message.getSubject());
               mailHeaders.append("\n");

            }


            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mailHeaders.toString();

    }



}
