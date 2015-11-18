/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fbapp.model.User;
import com.google.gson.*;
import com.fbapp.model.InterestedModel;

/**
 *
 * @author Yaara Shoham
 */
@WebServlet(name = "InterestedController", urlPatterns = {"/interested"})
public class InterestedController extends HttpServlet {
    
    protected void processRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws ServletException, IOException {
        String accessToken = servletRequest.getHeader("Authorization");
        if (accessToken == null){
            ServletHelper.writeResponse(new ApiResult("400","Authorization header missing",null),servletResponse);
            return;
        }
        SqlQueries sqlQueries = null;
        try {
            sqlQueries = new SqlQueries();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            String jsonBody = ServletHelper.getBodyAsString(servletRequest);
            //servletResponse.getWriter().write(jsonBody);
            Gson gson = new Gson();
            InterestedModel interestedModel = gson.fromJson(jsonBody,InterestedModel.class);
            sqlQueries.open();
            User user = sqlQueries.getUserByToken(accessToken);
            if (user == null){
                sqlQueries.close();
                ServletHelper.writeResponse(new ApiResult("403","Invalid access token",null),servletResponse);
            }else{
                user.gender_interested = interestedModel.gender;
                user.max_age_interested = interestedModel.max_age;
                user.min_age_interested = interestedModel.min_age;
                sqlQueries.updateInterested(user);
                ServletHelper.writeResponse(new ApiResult("200",null,null), servletResponse);
            }
            sqlQueries.close();
        }catch(Exception ex){
            sqlQueries.close();
            ServletHelper.writeResponse(new ApiResult("400",ex.getMessage(),null),servletResponse);
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
        ServletHelper.writeResponse(new ApiResult("405","GET not supported",null), response);
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
        return "This servlet lets users edit their settings of interest";
    }

}
