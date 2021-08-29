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

    private static final int FILES_FOR_TEST_COUNT = 3;

    @Override
    public void run() {
        try {
            // prepare to receive data
            OutputStream outputStream = mConnection.openOutputStream();

            System.out.println("Open output stream");
            Thread.sleep(3000);

            File startActionsTest = new File("./src/com/lenatopoleva/bluetoothserver/picturesother/01.jpg");
            byte[] message = createFileMessageToSend(startActionsTest, "image");
            outputStream.write(message);
            Thread.sleep(3000);

            sendAllFilesFrom("./src/com/lenatopoleva/bluetoothserver/picturesaction", "image", outputStream);
            Thread.sleep(3000);

            File endOfTest = new File("./src/com/lenatopoleva/bluetoothserver/picturesother/03.jpg");
            message = createFileMessageToSend(endOfTest, "image");
            outputStream.write(message);
            Thread.sleep(3000);

            File startObjectTest = new File("./src/com/lenatopoleva/bluetoothserver/picturesother/02.jpg");
            message = createFileMessageToSend(startObjectTest, "image");
            outputStream.write(message);
            Thread.sleep(3000);

            sendAllFilesFrom("./src/com/lenatopoleva/bluetoothserver/picturesobject", "image", outputStream);
            Thread.sleep(3000);

            message = createFileMessageToSend(endOfTest, "image");
            outputStream.write(message);
            Thread.sleep(3000);

            File startSoundsTest = new File("./src/com/lenatopoleva/bluetoothserver/picturesother/00.jpg");
            message = createFileMessageToSend(startSoundsTest, "image");
            outputStream.write(message);

            sendAllFilesFrom("./src/com/lenatopoleva/bluetoothserver/sounds", "audio", outputStream);
            Thread.sleep(5000);

            message = createFileMessageToSend(endOfTest, "image");
            outputStream.write(message);
            Thread.sleep(3000);

//            message = createCommandMessage("stop");
//            outputStream.write(message);

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

    private static byte[] createFileMessageToSend (File file, String fileType) {
        String encodedString = encodeFileToBase64Binary(file);
        System.out.println(encodedString);

        JSONObject jsonString = new JSONObject()
                .put("type", fileType)
                .put("data", encodedString);

        return jsonString.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] createCommandMessage (String command) {

        JSONObject jsonString = new JSONObject()
                .put("type", command)
                .put("data", "");

        return jsonString.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static void sendAllFilesFrom(String path, String fileType, OutputStream outputStream) throws IOException, InterruptedException {
        File[] fileArray = new File(path).listFiles();
        if (fileArray != null) {
            for (int i = 0; i < FILES_FOR_TEST_COUNT; i++) {
                File file = fileArray[i];
                if (fileType.equals("image") && file.getName().endsWith(".jpg")) {
                    byte[] message = createFileMessageToSend(file, fileType);
                    outputStream.write(message);
                }
                if (fileType.equals("audio")) {
                    byte[] message = createFileMessageToSend(file, fileType);
                    outputStream.write(message);
                    Thread.sleep(1000);
                    System.out.println("Send " + file.getName() + " sound.");
                }

            }
        }
    }

}
