package com.ddas.androidapp.network.exception;

public class ServerNoResponseException extends RuntimeException
{
    public ServerNoResponseException(String message)
    {
        super(message);
    }
}
