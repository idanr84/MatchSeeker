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
 * @author Yaara Shoham
 */
@WebServlet(name = "Match", urlPatterns = {"/matches"})
public class MatchController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param servletRequest servlet servletRequest
     * @param servletResponse servlet servletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processGetRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws ServletException, IOException {
        String accessToken = servletRequest.getHeader("Authorization");
        if (accessToken == null){
            ServletHelper.writeResponse(new ApiResult("400","Authorization header missing",null),servletResponse);
            return;
        }
        SqlQueries sqlQueries = null;
        try {
            sqlQueries = new SqlQueries();
        } catch (Exception ex) {
            ServletHelper.writeResponse(new ApiResult("400", ex.getMessage(), null), servletResponse);        }
        try{
            sqlQueries.open();
            User user = sqlQueries.getUserByToken(accessToken);
            if (user == null){
                sqlQueries.close();
                ServletHelper.writeResponse(new ApiResult("403","Invalid access token",null),servletResponse);
            }else{
                List<UserMatch> users = sqlQueries.getMatchedUsers(user.id);
                for (int a=0; a<users.size(); a++){
                    UserMatch userMatch = users.get(a);
                    userMatch.user.images = sqlQueries.getUserImages(userMatch.user.id);
                    users.set(a,userMatch);
                }
                sqlQueries.close();
                ServletHelper.writeResponse(new ApiResult("200",null,users), servletResponse);
                users.clear();
            }
        }catch(Exception ex){
            sqlQueries.close();
            ServletHelper.writeResponse(new ApiResult("400",ex.getMessage(),null),servletResponse);
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
        ServletHelper.writeResponse(new ApiResult("405","POST not supported",null), response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This servlet calaulates matching users";
    }// </editor-fold>

}
