/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fbapp.model.User;
import com.google.gson.*;
import com.fbapp.model.InterestedModel;
import com.fbapp.model.UserMatch;
/**
 *
 * @author Rameez Usmani
 */
@WebServlet(name = "InterestedController", urlPatterns = {"/interested"})
public class InterestedController extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accessToken=request.getHeader("Authorization");
        if (accessToken==null){
            ApiResult result=new ApiResult("400","Authorization header missing",null);
            ServletHelper.writeResponse(result,response);
            return;
        }
        DbHelper db=new DbHelper();
        try{
            String jsonBody=ServletHelper.getBodyAsString(request);
            //response.getWriter().write(jsonBody);
            Gson gs=new Gson();
            InterestedModel sm=gs.fromJson(jsonBody,InterestedModel.class);
            db.open();
            User u=db.getUserByToken(accessToken);
            if (u==null){
                db.close();
                ApiResult result=new ApiResult("403","Invalid access token",null);
                ServletHelper.writeResponse(result,response);
            }else{
                u.gender_interested=sm.gender;
                u.max_age_interested=sm.max_age;
                u.min_age_interested=sm.min_age;
                db.updateInterested(u);
                ApiResult result=new ApiResult("200",null,null);
                ServletHelper.writeResponse(result, response);
            }
            db.close();
        }catch(Exception ex){
            db.close();
            ApiResult result=new ApiResult("400",ex.getMessage(),null);
            ServletHelper.writeResponse(result,response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ApiResult result=new ApiResult("405","GET not supported",null);
        ServletHelper.writeResponse(result, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request,response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
