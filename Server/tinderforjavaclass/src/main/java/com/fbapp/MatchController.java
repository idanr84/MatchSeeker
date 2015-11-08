/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp;

import com.fbapp.model.User;
import com.fbapp.model.UserMatch;
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
@WebServlet(name = "Match", urlPatterns = {"/matches"})
public class MatchController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processGetRequest(HttpServletRequest request, HttpServletResponse response)
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
                List<UserMatch> users=db.getMatchedUsers(u.id);
                for (int a=0;a<users.size();a++){
                    UserMatch pmu=users.get(a);
                    pmu.user.images=db.getUserImages(pmu.user.id);
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processGetRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ApiResult result=new ApiResult("405","POST not supported",null);
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
