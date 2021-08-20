package com.lenatopoleva.bluetoothserver;

public class BluetoothServer{

    public static void main(String[] args) {
        Thread waitThread = new Thread(new WaitThread());
        waitThread.start();
    }
}
