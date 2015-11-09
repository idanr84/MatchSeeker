/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp;

import com.fbapp.model.PotentialMatchUser;
import com.fbapp.model.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Yaara Shoham
 */
@WebServlet(name = "PotentialMatches", urlPatterns = {"/potentialmatches"})
public class PotentialMatchController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param servletRequest servletRequest for potential matches
     * @param servletResponse servletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws ServletException, IOException {
        String accessToken=servletRequest.getHeader("Authorization");
        if (accessToken == null){
//            ApiResult apiResult = new ApiResult("400","Authorization header missing",null);
            ServletHelper.writeResponse(new ApiResult("400","Authorization header missing",null),servletResponse);
            return;
        }
        DbHelper dbHelper = new DbHelper();
        try{
            dbHelper.open();
            User user = dbHelper.getUserByToken(accessToken);
            if (user == null){
                dbHelper.close();
//                ApiResult apiResult = new ApiResult("403","Invalid access token",null);
                ServletHelper.writeResponse(new ApiResult("403","Invalid access token",null),servletResponse);
            }else{
                List<PotentialMatchUser> users = dbHelper.getPotentialMatchingUsers(user.id);
                for (int a= 0; a<users.size(); a++){
                    PotentialMatchUser potentialMatchUser = users.get(a);
                    potentialMatchUser.images = dbHelper.getUserImages(potentialMatchUser.id);
                    users.set(a,potentialMatchUser);
                }
                dbHelper.close();
//                ApiResult apiResult = new ApiResult("200",null,users);
                ServletHelper.writeResponse(new ApiResult("200",null,users), servletResponse);
                users.clear();
            }
        }catch(Exception ex){
            dbHelper.close();
//            ApiResult apiResult = new ApiResult("400",ex.getMessage(),null);
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
        processRequest(request, response);
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
        ApiResult result=new ApiResult("405",null,"POST not supported");
        ServletHelper.writeResponse(result, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
