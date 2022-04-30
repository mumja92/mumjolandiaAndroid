package com.mumjolandia.android.utils;

import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MumjolandiaCommunicator extends AsyncTask<String, Void, MumjolandiaResponse> {
    private String serverIp;
    private int serverPort;

    public MumjolandiaCommunicator(String ip, int port){
        serverIp = ip;
        serverPort = port;
    }

    @Override
    protected MumjolandiaResponse doInBackground(String... params) {
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();
        String command = params[0];
        return send(command);
    }

    private MumjolandiaResponse send(String command){
        byte[] return_bytes = {};
        int return_status = -1;
        int statusSize = 2;
        try (Socket socket = new Socket(serverIp, serverPort)) {
            byte[] messageLength = intToByteArray(command.length());
            byte[] message = command.getBytes();
            byte[] byteMessage = new byte[messageLength.length + statusSize + message.length];

            // write length as bytes
            System.arraycopy(messageLength, 0, byteMessage, 0, messageLength.length);
            // write status as bytes
            for (int i=0; i< statusSize; i++){
                byteMessage[messageLength.length + i] = 0x0;
            }
            // write message as bytes
            System.arraycopy(message, 0, byteMessage, messageLength.length + statusSize, message.length);
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            dOut.write(byteMessage);

            int length = dIn.readInt();                    // read length of incoming message
            if(length>0) {
                byte[] return_message = new byte[length + statusSize];
                dIn.readFully(return_message, 0, return_message.length); // read the message
                return_status = ((return_message[0] & 0xff) << 8) | (return_message[1] & 0xff);
                return_bytes = new byte[length];
                System.arraycopy(return_message, statusSize, return_bytes, 0, length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MumjolandiaResponse(return_status, return_bytes);
    }
    private byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
}
