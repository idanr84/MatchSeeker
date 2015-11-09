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
 *
 * @author Yaara Shoham
 */
@WebServlet(name = "UserLogin", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param servletRequest servlet servletRequest
     * @param servletResponse servlet servletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws ServletException, IOException {
        PrintWriter wr = servletResponse.getWriter();
        String fbAccessToken = servletRequest.getParameter("access_token");
        String fbUserId = servletRequest.getParameter("facebook_user_id");
        if (fbAccessToken == null){
            ServletHelper.writeResponse(new ApiResult("400","Parameter access_token missing",null),servletResponse);
            return;
        }
        if (fbUserId==null){
            ServletHelper.writeResponse(new ApiResult("400","Parameter facebook_user_id missing",null),servletResponse);
            return;
        }
        
        User user;
        DbHelper dbHelper = new DbHelper();
        try{
            dbHelper.open();
            user = dbHelper.getUserByFbId(fbUserId);
            dbHelper.close();
//            if (user!=null){
//                User su=new User();
//                su.id=user.id;
//                su.accessToken=user.accessToken;
//                ApiResult result=new ApiResult("200",null,su);
//                ServletHelper.writeResponse(result,servletResponse);
//            }
        }catch(Exception ex){
            dbHelper.close();
            ServletHelper.writeResponse(new ApiResult("400",ex.getMessage(),null),servletResponse);
            return;
        }
        
        try{
            FbHelper fbHelper = new FbHelper(fbAccessToken);
            User fbuser = fbHelper.getMe();
            if (user == null){
                user = new User();
                user.id = 0;
            }
            user.birthday_as_date = fbuser.birthday_as_date;
            user.gender = fbuser.gender;
            user.profile_url = fbuser.profile_url;
            user.profile_pic_url = fbuser.profile_pic_url;
            user.profile_id = fbuser.profile_id;
            user.location = fbuser.location;
            user.name = fbuser.name;
            user.facebook_user_id = fbUserId;
            user.fbAccessToken = fbAccessToken;
            user.age = DbHelper.getAge(user.birthday_as_date);
            if (user.id == 0){
                if (user.gender.equalsIgnoreCase("m") || user.gender.equalsIgnoreCase("male")){
                    user.gender_interested="female";
                }else if (user.gender.equalsIgnoreCase("f") || user.gender.equalsIgnoreCase("female")){
                    user.gender_interested="male";
                }else{
                    user.gender_interested="both";
                }
                if (user.age > 0){
                    user.min_age_interested = user.age-10;
                    user.max_age_interested = user.age+10;
                }else{
                    user.min_age_interested=18;
                    user.max_age_interested=35;
                }
            }
            
            String tempAccessToken = null;
            if (user.id == 0){
                // algorithm can be "MD5", "SHA-1", "SHA-256"
                MessageDigest digest = MessageDigest.getInstance("MD5");
                String toEncode = fbUserId + String.valueOf((new Date()).getTime());
                byte[] inputBytes = toEncode.getBytes();// get bytes array from message
                byte[] hashBytes = digest.digest(inputBytes);
                // convert hash bytes to string (usually in hexadecimal form)
                String encodedStr = String.format("%x", new BigInteger(1, hashBytes));
                tempAccessToken = encodedStr;
                user.accessToken = encodedStr;
            }else{
                tempAccessToken = user.accessToken;
            }

            List<Album> albums = fbHelper.getAlbums();
            for (int a=0; a<albums.size(); a++){
                Album album = albums.get(a);
                //wr.print(album.getName()+"<br />");
                if (album.getType().contains("profile")){
                    user.images = new Vector<UserImage>();
                    //wr.print("Yes profile<br />");
                    List<Photo> photos = fbHelper.getPictures(album.getId());
                    int photoCount = 0;
                    for (int b=0; b<photos.size(); b++){
                        Photo photo = photos.get(b);
                        UserImage userImage = new UserImage();
                        userImage.picture_url = photo.getPicture();
                        user.images.add(userImage);
                        userImage=null;
                        photoCount++;
                        if (photoCount==5)
                            break;
                    }
                    photos.clear();
                    break;
                }
            }
            albums.clear();
            List<Page> pages = fbHelper.getAllLikes();
            try{
                //dbHelper.removeUserByFbId(fbUserId);
                if (user.id == 0){
                    dbHelper.saveUser(user);
                    user = dbHelper.getUserByToken(user.accessToken);
                }
                List<UserImage> userImageList = user.images;
                user.accessToken = tempAccessToken;
                user.images = userImageList;
                dbHelper.removeUserImages(user.id);
                if (user.images != null){
                    for (int a=0; a<user.images.size(); a++){
                        UserImage userImage = user.images.get(a);
                        userImage.user_id = user.id;
                        user.images.set(a,userImage);
                    }
                    dbHelper.saveUserImages(user.images);
                }
                dbHelper.removeUserLikes(user.id);
                dbHelper.savePages(user.id, pages);
                dbHelper.close();
                User user1 = new User();
                user1.id = user.id;
                user1.accessToken = user.accessToken;
                ServletHelper.writeResponse(new ApiResult("200",null,user1),servletResponse);
            }catch(Exception ex){
                dbHelper.close();
                throw ex;
            }
        }catch(Exception ex){
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
