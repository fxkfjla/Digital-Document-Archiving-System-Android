package com.ddas.androidapp.network.api.model;

public class TokenDTO
{
    public TokenDTO(String token)
    {
        this.token = token;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    private String token;
}
