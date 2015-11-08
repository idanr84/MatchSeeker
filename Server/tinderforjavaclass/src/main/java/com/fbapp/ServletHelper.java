/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Rameez Usmani
 */
public class ServletHelper {
    
    public static String getBodyAsString(HttpServletRequest request)
    throws Exception{
        java.io.BufferedReader br=request.getReader();
        StringBuilder sb=new StringBuilder();
        String str=br.readLine();
        while(str!=null){
            sb.append(str);
            str=br.readLine();
        }
        return sb.toString();
    }
    
    public static void writeResponse(ApiResult result,HttpServletResponse response)
    throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        //out.write(DbHelper.USERNAME+","+DbHelper.PASSWORD+","+DbHelper.URL);
        out.write(result.toJson());
        out.close();
    }
}
