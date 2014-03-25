package com.sms.todo.model;

public class Response
{
    public final String success;
    public final String error;
    public final Object data;
    
    private Response(String success, String error, Object data)
    {
        this.success = success;
        this.error = error;
        this.data = data;
    }
    
    public static Response success(String message)
    {
        return new Response(message, null, null);
    }
    
    public static Response error(String message)
    {
        return new Response(null, message, null);
    }
    
    public static Response data(String success, Object data)
    {
        return new Response(success, null, data);
    }
}
