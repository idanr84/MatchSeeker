/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fbapp.model.*;
import com.restfb.types.Album;
import com.restfb.types.Page;
import com.restfb.types.Photo;

/**
 * @author Yaara Shoham
 */
@WebServlet(name = "UserLogin", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     * Login servlet receives <code>facebook_user_id</code> &amp; <code>access_token</code> and
     * responds with App's <code>Access Token</code>
     *
     * @param servletRequest  servlet servletRequest
     * @param servletResponse servlet servletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
        PrintWriter wr = servletResponse.getWriter();
        String facebookAccessToken = servletRequest.getParameter("access_token");
        String facebookUserId = servletRequest.getParameter("facebook_user_id");
        if (facebookAccessToken == null) {
            ServletHelper.writeResponse(new ApiResult("400", "Parameter access_token missing", null), servletResponse);
            return;
        }
        if (facebookUserId == null) {
            ServletHelper.writeResponse(new ApiResult("400", "Parameter facebook_user_id missing", null), servletResponse);
            return;
        }

        User user;
        SqlQueries sqlQueries = null;
        try {
            sqlQueries = new SqlQueries();
        } catch (Exception ex) {
            ServletHelper.writeResponse(new ApiResult("500", ex.getMessage(), null), servletResponse);
        }
        try {
            sqlQueries.open();
            user = sqlQueries.getUserByFbId(facebookUserId);
            sqlQueries.close();
        /*    if (user!=null){
                User su=new User();
                su.id=user.id;
                su.accessToken=user.accessToken;
                ApiResult result=new ApiResult("200",null,su);
                ServletHelper.writeResponse(result,servletResponse);
            }*/
        } catch (Exception ex) {
            sqlQueries.close();
            ServletHelper.writeResponse(new ApiResult("400", ex.getMessage(), null), servletResponse);
            return;
        }

        try {
            FbHelper fbHelper = new FbHelper(facebookAccessToken);
            User facebookUser = fbHelper.getMe();
            if (user == null) {
                user = new User();
                user.id = 0;
            }
            user.initUserDetails(fbHelper, facebookUserId, facebookAccessToken);
            if (user.id == 0) {
                user.initUserInterests();
            }

            String tempAccessToken = user.populateAccessToken();

            user.fetchProfilePhotos(fbHelper);

            List<Page> pages = fbHelper.getAllLikes();
            try {
                //sqlQueries.removeUserByFbId(facebookUserId);
                if (user.id == 0) {
                    sqlQueries.saveUser(user);
                    user = sqlQueries.getUserByToken(user.accessToken);
                }
                List<UserImage> userImageList = user.images;
                user.accessToken = tempAccessToken;
                user.images = userImageList;
                sqlQueries.removeUserImages(user.id);
                if (user.images != null) {
                    for (int a = 0; a < user.images.size(); a++) {
                        UserImage userImage = user.images.get(a);
                        userImage.user_id = user.id;
                        user.images.set(a, userImage);
                    }
                    sqlQueries.saveUserImages(user.images);
                }
                sqlQueries.removeUserLikes(user.id);
                sqlQueries.savePages(user.id, pages);
                sqlQueries.close();
                User user1 = new User();
                user1.id = user.id;
                user1.accessToken = user.accessToken;
                ServletHelper.writeResponse(new ApiResult("200", null, user1), servletResponse);
            } catch (Exception ex) {
                sqlQueries.close();
                throw ex;
            }
        } catch (Exception ex) {
            ServletHelper.writeResponse(new ApiResult("400", ex.getMessage(), null), servletResponse);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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
