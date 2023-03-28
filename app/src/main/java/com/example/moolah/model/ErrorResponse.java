package com.example.moolah.model;

/**
 * POJO for error element sent by API
 */

public class ErrorResponse {
    //fail login
    private String status;
    private Error error;

    public String getStatus(){
        return status;
    }

    public void setStatus(String Status){
        this.status  = status;
    }

    public Error getError(){
        return error;
    }

    public void setError(Error error){
        this.error = error;
    }
}
