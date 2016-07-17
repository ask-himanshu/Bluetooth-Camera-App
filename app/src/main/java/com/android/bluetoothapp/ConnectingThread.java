package com.android.bluetoothapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by hp on 15-Jul-16.
 */
public class ConnectingThread extends Thread {

    private BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final static UUID uuid = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    byte[] buffer;


    public ConnectingThread(BluetoothDevice device) {

        BluetoothSocket temp = null;
        bluetoothDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            temp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothSocket = temp;

        Thread connectionThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // Always cancel discovery because it will slow down a connection
                bluetoothAdapter.cancelDiscovery();

                // Make a connection to the BluetoothSocket
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    bluetoothSocket.connect();
                } catch (IOException e) {
                    //connection to device failed so close the socket
                    try {
                        bluetoothSocket.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });

        connectionThread.start();

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the BluetoothSocket input and output streams
        try

        {
            tmpIn = bluetoothSocket.getInputStream();
            tmpOut = bluetoothSocket.getOutputStream();
            buffer = new byte[1024];
        } catch (
                IOException e
                )

        {
            e.printStackTrace();
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }


    public void run() {

        // Keep listening to the InputStream while connected
        while (true) {
            try {
                //read the data from socket stream
                mmInStream.read(buffer);

                // Send the obtained bytes to the UI Activity
            } catch (IOException e) {
                //an exception here marks connection loss
                //send message to UI Activity
                break;
            }
        }
    }

    public void write(byte[] buffer) {
        try {
            //write the data to socket stream
            mmOutStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }


       /* Scanner scan = new Scanner(new InputStreamReader(mmInStream));
        String readIn;
        try {
            readIn = scan.next();
            int bytes = 5; // I tried with or without this, since I do not think it matters...
            buffer = readIn.getBytes(Charset.forName("US-ASCII"));
        } catch (Exception e) {

        }*/

    }



   /* public void run() {
        // Cancel any discovery as it will slow down the connection
        bluetoothAdapter.cancelDiscovery();

        try {
            // This will block until it succeeds in connecting to the device
            // through the bluetoothSocket or throws an exception
            bluetoothSocket.connect();
        } catch (IOException connectException) {
            try {
                bluetoothSocket =(BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(bluetoothDevice,2);
                bluetoothSocket.connect();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

          //  connectException.printStackTrace();
            try {
                bluetoothSocket.close();
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
        }

        // Code to manage the connection in a separate thread
        *//*
            manageBluetoothConnection(bluetoothSocket);
        *//*
    }*/

    // Cancel an open connection and terminate the thread
    public void cancel() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
