package com.sms.todo.model;

public class Response
{
    public String success;
    public String error;
    
    private Response(String success, String error)
    {
        this.success = success;
        this.error = error;
    }
    
    public static Response success(String message)
    {
        return new Response(message, null);
    }
    
    public static Response error(String message)
    {
        return new Response(null, message);
    }
}
