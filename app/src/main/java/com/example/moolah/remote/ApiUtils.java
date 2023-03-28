package com.example.moolah.remote;

public class ApiUtils {
    //REST API server URL
    public static final String Base_URL = "https://moonks.000webhostapp.com/prestige/";

    //return UserService instance
    public static UserService getUserService(){
        return RetrofitClient.getClient(Base_URL).create(UserService.class);
    }
}
