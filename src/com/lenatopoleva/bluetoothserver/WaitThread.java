package com.lenatopoleva.bluetoothserver;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.IOException;

/*
Using the Java APIs for Bluetooth -
https://www.oracle.com/technical-resources/articles/javame/bluetooth-wireless-technology-part2.html
*/

public class WaitThread implements Runnable{

    @Override
    public void run() {
        waitForConnection();
    }

    private void waitForConnection() {

        LocalDevice local = null;
        StreamConnectionNotifier notifier;
        StreamConnection connection = null;

        try {
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);

            System.out.println("Bluetooth (MAC) address : " + local.getBluetoothAddress());

            /* The UUID on the Bluetooth server side must be consistent with the UUID on the mobile phone side.
             UUID s on the server end need to remove the middle - separator. */
            UUID uuid = new UUID("04c6093b00001000800000805f9b34fb", false);
            System.out.println(uuid.toString());

            String url = "btspp://localhost:" + uuid.toString() + ";name=BluetoothServer";
            notifier = (StreamConnectionNotifier) Connector.open(url);

        } catch (BluetoothStateException e) {
            System.out.println("Bluetooth is not turned on.");
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // waiting for connection
        while(true) {
            try {
                System.out.println("waiting for connection...");
                connection = notifier.acceptAndOpen();

                Thread processThread = new Thread(new ProcessConnectionThread(connection));
                processThread.start();

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
