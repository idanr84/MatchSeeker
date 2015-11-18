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
import com.fbapp.model.*;
import com.google.gson.Gson;

/**
 *
 * @author Yaara Shoham
 */
@WebServlet(name = "MeController", urlPatterns = {"/me"})
public class MeController extends HttpServlet {

    /**
     * Processes requests for HTTP <code>GET</code>
     * method.
     *
     * Me servlet receives the user's accesstoken within the Authorization header and responds to the client
     * with User's full info, a list of potential matched users &amp; a list of matched users
     *
     * @param servletRequest servlet servletRequest
     * @param servletResponse servlet servletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws ServletException, IOException {
        String accessToken = servletRequest.getHeader("Authorization");
        if (accessToken == null){
//            ApiResult apiResult = new ApiResult("400","Authorization header missing",null);
            ServletHelper.writeResponse(new ApiResult("400","Authorization header missing",null),servletResponse);
            return;
        }
        SqlQueries sqlQueries = null;
        try {
            sqlQueries = new SqlQueries();
        } catch (Exception ex) {
            ServletHelper.writeResponse(new ApiResult("500", ex.getMessage(), null), servletResponse);

        }
        try{
            sqlQueries.open();
            User user = sqlQueries.getUserByToken(accessToken);
            if (user == null){
                sqlQueries.close();
//                ApiResult apiResult = new ApiResult("403","Invalid access token",null);
                ServletHelper.writeResponse(new ApiResult("403","Invalid access token",null),servletResponse);
            }else{
                MeModel meModel = new MeModel();
                try{
                    user.images = sqlQueries.getUserImages(user.id);
                    meModel.user = user;
                    meModel.potential_matches = sqlQueries.getPotentialMatchingUsers(user.id);
                    for (int a=0; a<meModel.potential_matches.size(); a++){
                        PotentialMatchUser potentialMatchUser = meModel.potential_matches.get(a);
                        potentialMatchUser.images = sqlQueries.getUserImages(potentialMatchUser.id);
                        meModel.potential_matches.set(a,potentialMatchUser);
                    }
                    meModel.matched_users = sqlQueries.getMatchedUsers(user.id);
                    for (int a=0; a<meModel.matched_users.size(); a++){
                        UserMatch userMatch = meModel.matched_users.get(a);
                        userMatch.user.images = sqlQueries.getUserImages(userMatch.user.id);
                        meModel.matched_users.set(a,userMatch);
                    }
                    sqlQueries.close();
                    ServletHelper.writeResponse(new ApiResult("200",null,meModel),servletResponse);
                }catch(Exception ex){
                    sqlQueries.close();
                    throw ex;
                }
            }
        }catch(Exception ex){
            ServletHelper.writeResponse(new ApiResult("400",ex.getMessage(),null),servletResponse);
        }
    }

    /**
     * Processes requests for HTTP <code>POST</code>
     * method.
     *
     * Me servlet receives from the client body request with a list of liked users, disliked users &amp; matched users
     *
     * @param servletRequest servlet servletRequest
     * @param servletResponse servlet servletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processPostRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws ServletException, IOException {
        String accessToken=servletRequest.getHeader("Authorization");
        if (accessToken == null){
            ServletHelper.writeResponse(new ApiResult("400","Authorization header missing",null),servletResponse);
            return;
        }
        SqlQueries sqlQueries = null;
        try {
            sqlQueries = new SqlQueries();
        } catch (Exception ex) {
            ServletHelper.writeResponse(new ApiResult("500", ex.getMessage(), null), servletResponse);
        }
        try{
            String jsonBody = ServletHelper.getBodyAsString(servletRequest);
            Gson gson = new Gson();
            SubmitModel submitModel = gson.fromJson(jsonBody,SubmitModel.class);
            sqlQueries.open();
            User user = sqlQueries.getUserByToken(accessToken);
            if (user == null){
                sqlQueries.close();
                ServletHelper.writeResponse(new ApiResult("403","Invalid access token",null),servletResponse);
            }else{
                if (submitModel.liked_users != null){
                    for (int a = 0; a<submitModel.liked_users.size(); a++){
                        sqlQueries.removeLike(user.id, submitModel.liked_users.get(a));
                        sqlQueries.saveLike(user.id, submitModel.liked_users.get(a));
                    }
                }
                if (submitModel.disliked_users != null){
                    for (int a = 0; a < submitModel.disliked_users.size(); a++){
                        sqlQueries.removeDislike(user.id, submitModel.disliked_users.get(a));
                        sqlQueries.saveDislike(user.id, submitModel.disliked_users.get(a));
                    }
                }
                if (submitModel.matched_users!=null){
                    UserMatch userMatch = new UserMatch();
                    userMatch.user = new User();
                    for (int a=0; a<submitModel.matched_users.size(); a++){
                        sqlQueries.removeMatch(user.id, submitModel.matched_users.get(a).user_id);
                        userMatch.user.id = submitModel.matched_users.get(a).user_id;
                        userMatch.match_viewed = submitModel.matched_users.get(a).match_viewed;
                        userMatch.match_announced = submitModel.matched_users.get(a).match_announced;
                        sqlQueries.saveMatch(user.id, userMatch);
                        
                        //now save another
                        sqlQueries.removeMatch(submitModel.matched_users.get(a).user_id, user.id);
                        userMatch.user.id = user.id;
                        userMatch.match_viewed = submitModel.matched_users.get(a).match_viewed;
                        userMatch.match_announced = submitModel.matched_users.get(a).match_announced;
                        sqlQueries.saveMatch(submitModel.matched_users.get(a).user_id, userMatch);
                    }
                }
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
        processPostRequest(request, response);
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
