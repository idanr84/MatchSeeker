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
 * @author Rameez Usmani
 */
@WebServlet(name = "PotentialMatches", urlPatterns = {"/potentialmatches"})
public class PotentialMatchController extends HttpServlet {

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
        String accessToken=request.getHeader("Authorization");
        if (accessToken==null){
            ApiResult result=new ApiResult("400","Authorization header missing",null);
            ServletHelper.writeResponse(result,response);
            return;
        }
        DbHelper db=new DbHelper();
        try{
            db.open();
            User u=db.getUserByToken(accessToken);
            if (u==null){
                db.close();
                ApiResult result=new ApiResult("403","Invalid access token",null);
                ServletHelper.writeResponse(result,response);
            }else{
                List<PotentialMatchUser> users=db.getPotentialMatchingUsers(u.id);
                for (int a=0;a<users.size();a++){
                    PotentialMatchUser pmu=users.get(a);
                    pmu.images=db.getUserImages(pmu.id);
                    users.set(a,pmu);
                }
                db.close();
                ApiResult result=new ApiResult("200",null,users);
                ServletHelper.writeResponse(result, response);
                users.clear();
            }
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
