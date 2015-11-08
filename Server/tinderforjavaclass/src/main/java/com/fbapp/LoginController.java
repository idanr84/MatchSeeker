/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fbapp.model.*;

/**
 *
 * @author Rameez Usmani
 */
@WebServlet(name = "UserLogin", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

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
        PrintWriter wr=response.getWriter();
        String fbAccessToken=request.getParameter("access_token");
        String fbUserId=request.getParameter("facebook_user_id");
        if (fbAccessToken==null){
            ApiResult result=new ApiResult("400","Parameter access_token missing",null);
            ServletHelper.writeResponse(result,response);
            return;
        }
        if (fbUserId==null){
            ApiResult result=new ApiResult("400","Parameter facebook_user_id missing",null);
            ServletHelper.writeResponse(result,response);
            return;
        }
        
        User user=null;
        DbHelper dh=new DbHelper();
        try{
            dh.open();
            user=dh.getUserByFbId(fbUserId);
            dh.close();
//            if (user!=null){
//                User su=new User();
//                su.id=user.id;
//                su.accessToken=user.accessToken;
//                ApiResult result=new ApiResult("200",null,su);
//                ServletHelper.writeResponse(result,response);
//            }
        }catch(Exception ex){
            dh.close();
            ApiResult result=new ApiResult("400",ex.getMessage(),null);
            ServletHelper.writeResponse(result,response);
            return;
        }
        
        try{
            FbHelper fh=new FbHelper(fbAccessToken);
            User fbuser=fh.getMe();
            if (user==null){
                user=new User();
                user.id=0;
            }
            user.birthday_as_date=fbuser.birthday_as_date;
            user.gender=fbuser.gender;
            user.profile_url=fbuser.profile_url;
            user.profile_pic_url=fbuser.profile_pic_url;
            user.profile_id=fbuser.profile_id;
            user.location=fbuser.location;
            user.name=fbuser.name;
            user.facebook_user_id=fbUserId;
            user.fbAccessToken=fbAccessToken;
            user.age=DbHelper.getAge(user.birthday_as_date);
            if (user.id==0){
                if (user.gender.toLowerCase().compareTo("m")==0 || user.gender.toLowerCase().compareTo("male")==0){
                    user.gender_interested="female";
                }else if (user.gender.toLowerCase().compareTo("f")==0 || user.gender.toLowerCase().compareTo("female")==0){
                    user.gender_interested="male";
                }else{
                    user.gender_interested="both";
                }
                if (user.age>0){
                    user.min_age_interested=user.age-10;
                    user.max_age_interested=user.age+10;
                }else{
                    user.min_age_interested=18;
                    user.max_age_interested=35;
                }
            }
            
            String tempAccessToken=null;
            if (user.id==0){
                // algorithm can be "MD5", "SHA-1", "SHA-256"
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                String toEncode=fbUserId+String.valueOf((new java.util.Date()).getTime());
                byte[] inputBytes = toEncode.getBytes();// get bytes array from message
                byte[] hashBytes = digest.digest(inputBytes);
                // convert hash bytes to string (usually in hexadecimal form)
                String encodedStr=String.format("%x", new java.math.BigInteger(1, hashBytes));
                tempAccessToken=encodedStr;
                user.accessToken=encodedStr;
            }else{
                tempAccessToken=user.accessToken;
            }
            
            List<com.restfb.types.Album> albums=fh.getAlbums();
            for (int a=0;a<albums.size();a++){
                com.restfb.types.Album alb=albums.get(a);
                //wr.print(alb.getName()+"<br />");
                if (alb.getType().contains("profile")){
                    user.images=new java.util.Vector<UserImage>();
                    //wr.print("Yes profile<br />");
                    List<com.restfb.types.Photo> photos=fh.getPictures(alb.getId());
                    int photoCount=0;
                    for (int b=0;b<photos.size();b++){
                        com.restfb.types.Photo pt=photos.get(b);
                        UserImage ui=new UserImage();
                        ui.picture_url=pt.getPicture();
                        user.images.add(ui);
                        ui=null;
                        photoCount++;
                        if (photoCount==5)
                            break;
                    }
                    photos.clear();
                    break;
                }
            }
            albums.clear();
            List<com.restfb.types.Page> pages=fh.getAllLikes();
            try{
                //dh.removeUserByFbId(fbUserId);
                if (user.id==0){
                    dh.saveUser(user);                
                    user=dh.getUserByToken(user.accessToken);
                }
                List<UserImage> imgs=user.images;
                user.accessToken=tempAccessToken;
                user.images=imgs;
                dh.removeUserImages(user.id);
                if (user.images!=null){
                    for (int a=0;a<user.images.size();a++){
                        UserImage img=user.images.get(a);
                        img.user_id=user.id;
                        user.images.set(a,img);
                    }
                    dh.saveUserImages(user.images);
                }
                dh.removeUserLikes(user.id);
                dh.savePages(user.id, pages);
                dh.close();
                User su=new User();
                su.id=user.id;
                su.accessToken=user.accessToken;
                ApiResult result=new ApiResult("200",null,su);
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
