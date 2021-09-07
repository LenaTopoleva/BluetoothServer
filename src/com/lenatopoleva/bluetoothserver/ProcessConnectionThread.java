package com.lenatopoleva.bluetoothserver;

import org.json.JSONObject;

import javax.microedition.io.StreamConnection;
import java.io.*;

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
            String message;
            // prepare to receive data
            OutputStream outputStream = mConnection.openOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            System.out.println("Open output stream");
            Thread.sleep(3000);

            message = createMessageToSend("show_image", "other", "01.jpg", false);
            objectOutputStream.writeObject(message);
            Thread.sleep(1000);

            sendAllFilesFrom("./src/com/lenatopoleva/bluetoothserver/picturesaction", "show_image",
                    "action", objectOutputStream);

            message = createMessageToSend("show_image", "other", "03.jpg", false);
            objectOutputStream.writeObject(message);
            Thread.sleep(1000);

            message = createMessageToSend("show_image", "other", "02.jpg", false);
            objectOutputStream.writeObject(message);

            sendAllFilesFrom("./src/com/lenatopoleva/bluetoothserver/picturesobject", "show_image",
                    "object", objectOutputStream);

            message = createMessageToSend("show_image", "other", "03.jpg", false);
            objectOutputStream.writeObject(message);
            Thread.sleep(1000);

            // shows 'end of session' message
            message = createMessageToSend("stop_session", "", "", false);
            objectOutputStream.writeObject(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String createMessageToSend (String messageType, String fileSubtype, String fileName, Boolean playTone) {

        JSONObject jsonString = new JSONObject()
                .put("type", messageType)
                .put("subtype", fileSubtype)
                .put("fileName", fileName)
                .put("tone", playTone);

        return jsonString.toString();
    }


    private static void sendAllFilesFrom(String path, String messageType, String fileSubtype,
                                         ObjectOutputStream objectOutputStream) throws IOException, InterruptedException {
        File[] fileArray = new File(path).listFiles();
        if (fileArray != null) {
            for (int i = 0; i < FILES_FOR_TEST_COUNT; i++) {
                File file = fileArray[i];
                if (messageType.equals("show_image") && file.getName().endsWith(".jpg")) {
                    System.out.println("file name = " + file.getName());
                    String message = createMessageToSend(messageType, fileSubtype, file.getName(), true);
                    objectOutputStream.writeObject(message);
                    Thread.sleep(1000);
                }

                /* 'play_audio' command for the future ability to send audio
                 example: {"type":"play_audio","subtype":"","fileName":"01.wav","tone":false}
                 play audio files from 'sounds' package
                 even if playTone true, it is ignored */
                if (messageType.equals("play_audio")) {
                    String message = createMessageToSend(messageType, fileSubtype, file.getName(), false);
                    objectOutputStream.writeObject(message);
                    Thread.sleep(1000);
                    System.out.println("Send " + file.getName() + " sound.");
                }
            }
        }
    }

}
