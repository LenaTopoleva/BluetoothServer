package com.lenatopoleva.bluetoothserver;

import javax.microedition.io.StreamConnection;
import java.io.OutputStream;

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

            while(true){
                for (int i = 1; i < 4 ; i++) {
                    String testMessage = "HELLO - " + i;
                    byte[] send = testMessage.getBytes();
                    outputStream.write(send);
                    Thread.sleep(1000);
                    outputStream.flush();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
