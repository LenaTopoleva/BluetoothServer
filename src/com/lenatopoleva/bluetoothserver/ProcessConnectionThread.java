package com.lenatopoleva.bluetoothserver;

import org.json.JSONObject;

import javax.microedition.io.StreamConnection;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ProcessConnectionThread implements Runnable {

    private StreamConnection mConnection;

    public ProcessConnectionThread(StreamConnection connection)
    {
        mConnection = connection;
    }

    @Override
    public void run() {
        try {
            // prepare to receive data
            OutputStream outputStream = mConnection.openOutputStream();

            System.out.println("Open output stream");

            Thread.sleep(5000);

            File first =  new File("C:/Users/lenak/AndroidStudioProjects/BluetoothReciever/BluetoothServer/src/com/lenatopoleva/bluetoothserver/5.jpg");
            File second =  new File("C:/Users/lenak/AndroidStudioProjects/BluetoothReciever/BluetoothServer/src/com/lenatopoleva/bluetoothserver/26.jpg");

            byte[] message = createMessageToSend(first);
            outputStream.write(message);

            Thread.sleep(3000);
            message = createMessageToSend(second);
            outputStream.write(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String encodeFileToBase64Binary(File file){
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.getEncoder().encodeToString(bytes);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encodedfile;
    }

    private static byte[] createMessageToSend (File file) {
        String encodedString = encodeFileToBase64Binary(file);
        System.out.println(encodedString);

        JSONObject jsonString = new JSONObject()
                .put("type", "image")
                .put("data", encodedString);

        return jsonString.toString().getBytes(StandardCharsets.UTF_8);
    }

}
