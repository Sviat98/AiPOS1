package model;

import org.apache.james.mime4j.message.Message;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class POP3Connection {
   private SSLSocket socket = null;
    private BufferedReader inputStream = null;
    private BufferedWriter outputStream = null;
    private String response;



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
                    //inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    outputStream.write(command);
                    outputStream.flush();
                    createResponse();


                } catch (IOException e) {
                    throw new POP3ConnectionException("Error while sending command") ;
                }
            }

    public String createMessage(){


        Message message = new Message();

        message.setFrom();
        message.setSubject("");
        message.setTo();
        //message.setDate();


        return message.toString();
    }
}
