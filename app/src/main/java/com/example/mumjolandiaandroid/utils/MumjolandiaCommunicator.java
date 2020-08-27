package com.example.mumjolandiaandroid.utils;

import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MumjolandiaCommunicator extends AsyncTask<String, Void, String> {
    private String serverIp;
    private int serverPort;

    public MumjolandiaCommunicator(String ip, int port){
        serverIp = ip;
        serverPort = port;
    }

    @Override
    protected String doInBackground(String... params) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
        String command = params[0];
        return send(command);
    }

    private String send(String command){
        String return_value = "error";
        try (Socket socket = new Socket(serverIp, serverPort)) {
            byte[] messageLength = intToByteArray(command.length());
            byte[] message = command.getBytes();
            byte[] byteMessage = new byte[messageLength.length + message.length];

            System.arraycopy(messageLength, 0, byteMessage, 0, messageLength.length);
            System.arraycopy(message, 0, byteMessage, messageLength.length, message.length);
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            dOut.write(byteMessage);

            int length = dIn.readInt();                    // read length of incoming message
            if(length>0) {
                byte[] return_message = new byte[length];
                dIn.readFully(return_message, 0, return_message.length); // read the message
                return_value = new String(return_message, StandardCharsets.UTF_8);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return return_value;
    }
    private byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
}
