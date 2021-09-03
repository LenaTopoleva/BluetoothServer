package com.lenatopoleva.bluetoothserver;

import org.json.JSONObject;

import javax.microedition.io.StreamConnection;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ProcessConnectionThread implements Runnable {

    private final StreamConnection mConnection;

    public ProcessConnectionThread(StreamConnection connection)
    {
        mConnection = connection;
    }

    private static final int FILES_FOR_TEST_COUNT = 2;

    @Override
    public void run() {
        try {
            byte[] message;
            // prepare to receive data
            OutputStream outputStream = mConnection.openOutputStream();

            System.out.println("Open output stream");
            Thread.sleep(3000);

            message = createMessageToSend("show_image", "other", "01.jpg");
            outputStream.write(message);
            Thread.sleep(1000);

            message = createMessageToSend("tone_enable","","");
            outputStream.write(message);

            sendAllFilesFrom("./src/com/lenatopoleva/bluetoothserver/picturesaction", "show_image", "action", outputStream);
            Thread.sleep(1000);

            message = createMessageToSend("show_image", "other", "03.jpg");
            outputStream.write(message);
            Thread.sleep(1000);

            message = createMessageToSend("show_image", "other", "02.jpg");
            outputStream.write(message);
            Thread.sleep(1000);

            message = createMessageToSend("tone_disable","","");
            outputStream.write(message);

            sendAllFilesFrom("./src/com/lenatopoleva/bluetoothserver/picturesobject", "show_image", "object", outputStream);
            Thread.sleep(1000);

            message = createMessageToSend("show_image", "other", "03.jpg");
            outputStream.write(message);
            Thread.sleep(1000);

            message = createMessageToSend("stop_session", "", "");
            outputStream.write(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] createMessageToSend (String messageType, String fileSubtype, String fileName) {

        JSONObject jsonString = new JSONObject()
                .put("type", messageType)
                .put("subtype", fileSubtype)
                .put("fileName", fileName);

        return jsonString.toString().getBytes(StandardCharsets.UTF_8);
    }


    private static void sendAllFilesFrom(String path, String messageType, String fileSubtype, OutputStream outputStream) throws IOException, InterruptedException {
        File[] fileArray = new File(path).listFiles();
        if (fileArray != null) {
            for (int i = 0; i < FILES_FOR_TEST_COUNT; i++) {
                File file = fileArray[i];
                if (messageType.equals("show_image") && file.getName().endsWith(".jpg")) {
                    System.out.println("file name = " + file.getName());
                    byte[] message = createMessageToSend(messageType, fileSubtype, file.getName());
                    outputStream.write(message);
                    Thread.sleep(1000);

                }
                if (messageType.equals("play_audio")) {
                    byte[] message = createMessageToSend(messageType, fileSubtype, file.getName());
                    outputStream.write(message);
                    Thread.sleep(1000);
                    System.out.println("Send " + file.getName() + " sound.");
                }

            }
        }
    }

}
