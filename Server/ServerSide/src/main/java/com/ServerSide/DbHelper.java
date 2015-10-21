/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ServerSide;

import java.sql.*;

/**
 *
 * @author yaarashoham
 */
public class DbHelper {
    
    //public static final String DB_HOST="localhost";
    public static final String DB_HOST="127.3.35.2:3306";
    
    public static final String DB_NAME="rameezusmani";
    //static final String DB_NAME="fbapp";
    
    public static final String DRIVER = "com.mysql.jdbc.Driver";  
    public static final String URL = "jdbc:mysql://"+DB_HOST+"/"+DB_NAME;
    
    //  Database credentials
    //static final String USERNAME = "root";
    public static final String USERNAME="admingyMDr67";
    //static final String PASSWORD = "dsfdsfdf";
    public static final String PASSWORD="eRcxjvyJFeNv";
    
    private Connection connection;
    
    public DbHelper(){
    }
    
    public boolean open()
    throws Exception{
        Class.forName(DRIVER);
        connection=DriverManager.getConnection(URL,USERNAME,PASSWORD);
        return true;
    }
    
    public boolean close(){
        if (connection!=null){
            try{
                connection.close();
            }catch(Exception ex){
                System.out.println("Error in closing connection: "+ex.getMessage());
            }
        }
        return true;
    }
    
    public int removeUser(String userId)
    throws Exception {
        String query="DELETE FROM UserInformation WHERE user_id=?";
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setString(1,userId);
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    private long getAge(java.util.Date birthDate){
        long dtNow=new java.util.Date().getTime();
        long dtBirth=birthDate.getTime();
        long diff=dtNow-dtBirth;
        if (diff>0){
            diff=diff/1000;
            diff=diff/(60*60*24*360);
            if (diff>0){
                System.out.println("Age: "+String.valueOf(diff));
            }else{
                System.out.println("Invalid values for age");
            }
        }else{
            System.out.println("Invalid values for age");
        }
        return diff;
    }
    
    public int saveUser(String userId,String accessToken,com.restfb.types.User user)
    throws Exception{
        String query="INSERT INTO UserInformation(user_id,access_token,profile_pic_url,location,name,profile_url,age,profile_id)";
        query+=" VALUES(?,?,?,?,?,?,?,?)";
        
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setString(1,userId);
        pstmt.setString(2,accessToken);
        pstmt.setString(3,user.getPicture()==null?"":user.getPicture().getUrl());
        pstmt.setString(4,user.getLocation()==null?"":user.getLocation().getName());
        pstmt.setString(5,user.getName());
        pstmt.setString(6,user.getLink());
        pstmt.setString(7,String.valueOf(getAge(user.getBirthdayAsDate())));
        pstmt.setString(8,user.getId());
        
        //pstmt.execute();
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int removeUserLikes(String userId)
    throws Exception {
        String query="DELETE FROM UserLikes WHERE user_id=?";
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setString(1,userId);
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int savePages(String userId,java.util.List<com.restfb.types.Page> pagesList)
    throws Exception {
        String query="INSERT INTO UserLikes(user_id,page_id,name,category)";
        query+=" VALUES(?,?,?,?)";
        
        int totalRows=0;
        
        PreparedStatement pstmt=connection.prepareStatement(query);
        for (int a=0;a<pagesList.size();a++){
            com.restfb.types.Page page=pagesList.get(a);
            pstmt.setString(1,userId);
            pstmt.setString(2,page.getId());
            pstmt.setString(3,page.getName());
            pstmt.setString(4,page.getCategory());
            
            int rc=pstmt.executeUpdate();
            totalRows+=rc;
        }
        pstmt.close();
        return totalRows;
    }
    
    public int savePage(String userId,com.restfb.types.Page page)
    throws Exception{
        String query="INSERT INTO UserLikes(user_id,page_id,name,category)";
        query+=" VALUES(?,?,?,?)";
        
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setString(1,userId);
        pstmt.setString(2,page.getId());
        pstmt.setString(3,page.getName());
        pstmt.setString(4,page.getCategory());
        
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public java.util.List<MatchUser> getMatchedUsers(String userId)
    throws Exception{
        String query="SELECT ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,COUNT(ul.user_id) AS cnt FROM userinformation ui,(SELECT u2.user_id AS user_id,u2.category AS category,ui2.location AS location FROM userlikes u2,userinformation ui2 WHERE u2.user_id='"+userId+"' AND ui2.user_id=u2.user_id GROUP BY u2.category) AS u3 INNER JOIN userlikes ul ON ul.category=u3.category WHERE ul.user_id<>u3.user_id AND ui.location<>u3.location AND ui.user_id=ul.user_id GROUP BY ul.user_id ORDER BY cnt DESC";
        System.out.println(query);
        Statement stmt=connection.createStatement();
        ResultSet rs=stmt.executeQuery(query);
        java.util.List<MatchUser> users=new java.util.Vector<MatchUser>();
        while(rs.next()){
            MatchUser mu=new MatchUser();
            mu.user_id=rs.getString("user_id");
            mu.name=rs.getString("name");
            mu.location=rs.getString("location");
            mu.profile_pic_url=rs.getString("profile_pic_url");
            mu.profile_url=rs.getString("profile_url");
            mu.profile_id=rs.getString("profile_id");
            mu.age=rs.getString("age");
            mu.count=rs.getInt("cnt");
            
            users.add(mu);
        }
        rs.close();
        stmt.close();
        return users;
    }
}
