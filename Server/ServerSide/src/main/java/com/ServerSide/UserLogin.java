/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ServerSide;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author yaarashoham
 */
public class UserLogin extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accessToken=request.getParameter("access_token");
        String userId=request.getParameter("user_id");
        if (accessToken==null){
            ApiResult result=new ApiResult("400","Parameter access_token missing",null);
            ServletHelper.writeResponse(result,response);
            return;
        }
        if (userId==null){
            ApiResult result=new ApiResult("400","Parameter user_id missing",null);
            ServletHelper.writeResponse(result,response);
            return;
        }
        
        try{
            FbHelper fh=new FbHelper(accessToken);
            com.restfb.types.User user=fh.getMe();
            List<com.restfb.types.Page> pages=fh.getAllLikes();
            DbHelper dh=new DbHelper();
            try{
                dh.open();
                dh.removeUser(userId);
                dh.saveUser(userId, accessToken, user);
                dh.removeUserLikes(userId);
                dh.savePages(userId, pages);
                dh.close();
                ApiResult result=new ApiResult("200",null,null);
                ServletHelper.writeResponse(result,response);
            }catch(Exception ex){
                dh.close();
                throw ex;
            }
        }catch(Exception ex){
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
        processRequest(request,response);
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Login controller";
    }// </editor-fold>
}
