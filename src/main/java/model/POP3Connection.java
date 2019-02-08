package model;

import java.io.*;
import java.net.Socket;

public class POP3Connection {
   private Socket socket = null;
    private BufferedReader inputStream = null;
    private BufferedWriter outputStream = null;
    private String response;



    public POP3Connection(){



       }

       public void connect() throws POP3ConnectionException{
           try {

               socket = new Socket("pop.mail.ru", 110);


               inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

               outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

               //createResponse();

               //String line = inputStream.readLine();

               //System.out.println(line);

               //System.out.println("OK connection");

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

    public String createResponse() throws POP3ConnectionException{
        response = null;
        StringBuilder result = new StringBuilder();
        try{
            String data = inputStream.readLine();
            result.append(data);
            result.append("\n");
            response = result.toString();

            return response;

        }
        catch (IOException e){
            throw new POP3ConnectionException("Error while recieving response");
        }


    }



    public String getResponse() {
        return response;
    }





            public void sendCommand(String command) throws POP3ConnectionException {
                try {
                    inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    outputStream.write(command);
                    outputStream.flush();
                    createResponse();

                } catch (IOException e) {
                    throw new POP3ConnectionException("Error while sending command");
                }
            }
}
