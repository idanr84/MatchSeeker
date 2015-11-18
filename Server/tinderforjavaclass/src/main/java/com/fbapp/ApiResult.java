/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp;

import com.google.gson.Gson;

/**
 * Each servlet responds with a JSON response
 * APIResult is the object to be instantiated for each response
 *
 * @author Yaara Shoham
 */
public class ApiResult {
    public String status;
    public String message;
    public Object data;
    
    public ApiResult(String status,String message,Object data){
        this.status = status;
        this.message = message;
        this.data = data;
    }
    
    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
