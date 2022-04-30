package com.mumjolandia.android.utils;

import java.nio.charset.StandardCharsets;

public class MumjolandiaResponse {
    private final int status;
    private final byte[] message;

    MumjolandiaResponse(int _status, byte[] _bytes){
        status = _status;
        message = _bytes;
    }
    public int getStatus(){
        return status;
    }

    public String getString(){
        return new String(message, StandardCharsets.UTF_8);
    }

    public byte[] getBytes(){
        return message;
    }

    public int getLength(){
        return message.length;
    }
}
