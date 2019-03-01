package model;

import org.apache.james.mime4j.message.*;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.util.ArrayList;

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

       private boolean isConnected(){
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
        multiResponse.append(" ");

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

        try{
            txtPart = new StringBuffer();
            htmlPart = new StringBuffer();
            attachments = new ArrayList<>();

            ByteArrayInputStream bais = new ByteArrayInputStream(getAllResponseLines(getResponse()).getBytes());

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

            fullMessage.append(htmlPart.toString());

            fullMessage.append("\n");

            for(BodyPart attach : attachments){
                String filename = attach.getFilename();
                FileOutputStream fos = new FileOutputStream(filename);

                try{
                    BinaryBody binaryBody = (BinaryBody) attach.getBody();
                    binaryBody.writeTo(fos);
                }
                finally {

                    fos.close();
                    attachments.remove(attach);
                }

            }


            resultMessage = fullMessage.toString();

            txtPart.delete(0,txtPart.length());

            htmlPart.delete(0,htmlPart.length());

            
        }
        catch ( IOException e){
            throw new POP3ConnectionException("Error while creating message");
        }

    }

    private String getTxtPart(Entity part) throws IOException{

            TextBody tb = (TextBody) part.getBody();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            tb.writeTo(baos);
            //baos.flush();

            return new String(baos.toByteArray());
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


}
