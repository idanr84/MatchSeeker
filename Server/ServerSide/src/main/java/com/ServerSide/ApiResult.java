/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ServerSide;

import com.google.gson.Gson;

/**
 *
 * @author yaarashoham
 */
public class ApiResult {
    public String status;
    public String message;
    public Object data;
    
    public ApiResult(String status,String message,Object data){
        this.status=status;
        this.message=message;
        this.data=data;
    }
    
    public String toJson(){
        Gson g=new Gson();
        return g.toJson(this);
    }
}
