/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Yaara Shoham
 */
public class ServletHelper {
    
    public static String getBodyAsString(HttpServletRequest request)
    throws Exception{
        BufferedReader requestReader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String readLine = requestReader.readLine();
        while(readLine != null){
            stringBuilder.append(readLine);
            readLine = requestReader.readLine();
        }
        return stringBuilder.toString();
    }
    
    public static void writeResponse(ApiResult result,HttpServletResponse response)
    throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        //out.write(SqlQueries.USERNAME+","+SqlQueries.PASSWORD+","+SqlQueries.URL);
        out.write(result.toJson());
        out.close();
    }
}
