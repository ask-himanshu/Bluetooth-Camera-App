package com.android.bluetoothapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;
import java.util.jar.Attributes;

/**
 * Created by hp on 15-Jul-16.
 */
public class ListeningThread extends Thread {

    private final BluetoothServerSocket bluetoothServerSocket;
    private final String TAG = "MainActivity";
    private final static UUID uuid = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    static Context mCtx;

    public ListeningThread(BluetoothAdapter mBluetoothAdapter) {
        BluetoothServerSocket temp = null;
        try {
            temp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(TAG, uuid);

        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothServerSocket = temp;
    }


    public void run() {
        BluetoothSocket bluetoothSocket;
        // This will block while listening until a BluetoothSocket is returned
        // or an exception occurs
        while (true) {
            try {
                bluetoothSocket = bluetoothServerSocket.accept();
             //   Toast.makeText(mCtx, "A connection has been accepted.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                break;
            }
            // If a connection is accepted
            if (bluetoothSocket != null) {
                Toast.makeText(mCtx, "A connection has been accepted.",
                        Toast.LENGTH_SHORT).show();
               /* runOnUiThread(new Runnable() {


                    public void run() {
                        Toast.makeText(mCtx, "A connection has been accepted.",
                                Toast.LENGTH_SHORT).show();
                    }
                });*/

                // Manage the connection in a separate thread

                try {


                    bluetoothServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    // Cancel the listening socket and terminate the thread
    public void cancel() {
        try {
            bluetoothServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
