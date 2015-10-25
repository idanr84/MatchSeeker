/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ServerSide;

import com.restfb.types.Page;
import com.restfb.types.User;

import java.sql.*;
import java.util.List;

/**
 *
 * @author yaarashoham
 */
public class DbHelper {
    

//    public static final String DB_HOST="127.3.35.2:3306";
//    public static final String DB_NAME="rameezusmani";
    public static final String DB_HOST="localhost";
    static final String DB_NAME="fbapp";
    
    public static final String DRIVER = "com.mysql.jdbc.Driver";  
    public static final String URL = "jdbc:mysql://"+DB_HOST+"/"+DB_NAME;
    
//    public static final String USERNAME="admingyMDr67";
//    public static final String PASSWORD="eRcxjvyJFeNv";
    //  Database credentials
    static final String USERNAME = "root";
    static final String PASSWORD = "1234";

    private Connection connection;
    
    public DbHelper(){
    }
    
    public boolean open()
    throws Exception{
        Class.forName(DRIVER);
        connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
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
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,userId);
        int rows = preparedStatement.executeUpdate();
        preparedStatement.close();
        return rows;
    }
    
    private long getAge(Date birthDate){
        long currentDate = new java.util.Date().getTime();
        long dateOfBirth = birthDate.getTime();
        long diff = currentDate-dateOfBirth;
        if (diff > 0){
            diff = diff/1000/(60*60*24*360);
            if (diff > 0){
                System.out.println("Age: "+String.valueOf(diff));
            }else{
                System.out.println("Invalid values for age");
            }
        }else{
            System.out.println("Invalid values for age");
        }
        return diff;
    }
    
    public int saveUser(String userId,String accessToken, User user)
    throws Exception{
        String query="INSERT INTO UserInformation(user_id,access_token,profile_pic_url,location,name,profile_url,age,profile_id)";
        query+=" VALUES(?,?,?,?,?,?,?,?)";
        
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        preparedStatement.setString(1,userId);
        preparedStatement.setString(2,accessToken);
        preparedStatement.setString(3,user.getPicture()==null?"":user.getPicture().getUrl());
        preparedStatement.setString(4,user.getLocation()==null?"":user.getLocation().getName());
        preparedStatement.setString(5,user.getName());
        preparedStatement.setString(6,user.getLink());
        preparedStatement.setString(7,String.valueOf(getAge((Date) user.getBirthdayAsDate())));
        preparedStatement.setString(8,user.getId());
        
        //preparedStatement.execute();
        int rows=preparedStatement.executeUpdate();
        preparedStatement.close();
        return rows;
    }
    
    public int removeUserLikes(String userId)
    throws Exception {
        String query="DELETE FROM UserLikes WHERE user_id=?";
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        preparedStatement.setString(1,userId);
        int rows=preparedStatement.executeUpdate();
        preparedStatement.close();
        return rows;
    }
    
    public int savePages(String userId,java.util.List<com.restfb.types.Page> pagesList)
    throws Exception {
        String query="INSERT INTO UserLikes(user_id,page_id,name,category)";
        query+=" VALUES(?,?,?,?)";
        
        int totalRows=0;
        
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        for (int a = 0; a<pagesList.size(); a++){
            Page page = pagesList.get(a);
            preparedStatement.setString(1,userId);
            preparedStatement.setString(2,page.getId());
            preparedStatement.setString(3,page.getName());
            preparedStatement.setString(4,page.getCategory());
            
            int rc = preparedStatement.executeUpdate();
            totalRows+=rc;
        }
        preparedStatement.close();
        return totalRows;
    }
    
    public int savePage(String userId,com.restfb.types.Page page)
    throws Exception{
        String query="INSERT INTO UserLikes(user_id,page_id,name,category)";
        query+=" VALUES(?,?,?,?)";
        
        PreparedStatement preparedStatement=connection.prepareStatement(query);
        preparedStatement.setString(1,userId);
        preparedStatement.setString(2,page.getId());
        preparedStatement.setString(3,page.getName());
        preparedStatement.setString(4,page.getCategory());
        
        int rows=preparedStatement.executeUpdate();
        preparedStatement.close();
        return rows;
    }
    
    public java.util.List<MatchUser> getMatchedUsers(String userId)
    throws Exception{
        String query="SELECT ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,COUNT(ul.user_id) AS cnt FROM userinformation ui,(SELECT u2.user_id AS user_id,u2.category AS category,ui2.location AS location FROM userlikes u2,userinformation ui2 WHERE u2.user_id='"+userId+"' AND ui2.user_id=u2.user_id GROUP BY u2.category) AS u3 INNER JOIN userlikes ul ON ul.category=u3.category WHERE ul.user_id<>u3.user_id AND ui.location<>u3.location AND ui.user_id=ul.user_id GROUP BY ul.user_id ORDER BY cnt DESC";
        System.out.println(query);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        List<MatchUser> users = new java.util.Vector<MatchUser>();

        while(resultSet.next()){
            MatchUser matchUser = new MatchUser();
            matchUser.user_id = resultSet.getString("user_id");
            matchUser.name = resultSet.getString("name");
            matchUser.location = resultSet.getString("location");
            matchUser.profile_pic_url = resultSet.getString("profile_pic_url");
            matchUser.profile_url = resultSet.getString("profile_url");
            matchUser.profile_id = resultSet.getString("profile_id");
            matchUser.age = resultSet.getString("age");
            matchUser.count = resultSet.getInt("cnt");
            
            users.add(matchUser);
        }
        resultSet.close();
        statement.close();
        return users;
    }
}
