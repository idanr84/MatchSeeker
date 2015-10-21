/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ServerSide;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author yaarashoham
 */
public class ServletHelper {
    public static void writeResponse(ApiResult result,HttpServletResponse response)
    throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        //out.write(DbHelper.USERNAME+","+DbHelper.PASSWORD+","+DbHelper.URL);
        out.write(result.toJson());
        out.close();
    }
}
