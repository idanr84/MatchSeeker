/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp;

import com.fbapp.model.PotentialMatchUser;
import com.fbapp.model.User;
import com.fbapp.model.UserImage;
import com.fbapp.model.UserMatch;
import java.sql.*;
/**
 *
 * @author Rameez Usmani
 */
public class DbHelper {
    
    protected Connection connection;
    
    private static String DbType="mysql-heroku";
    //private static String DbType="mysql-local";
    
    public String DB_HOST="localhost";
    public String DB_NAME="fbapp";
    
    public String DRIVER = "com.mysql.jdbc.Driver";
    public String URL = "jdbc:mysql://"+DB_HOST+"/"+DB_NAME;
    
    //  Database credentials
    public String USERNAME = "root";
    public String PASSWORD = "kartoos";
    
    public DbHelper(){
        if (DbType.compareTo("postgres-heroku")==0){
            DRIVER="org.postgresql.Driver";
            DB_HOST="ec2-54-225-199-108.compute-1.amazonaws.com:5432";
            DB_NAME="dfbenh61ht1ph";
            USERNAME="zirpnpgnfoqirg";
            PASSWORD="yx_S1twE1Pz-_aph1BCfNnQY42";
            URL="jdbc:postgresql://"+DB_HOST+"/"+DB_NAME;
        }else if (DbType.compareTo("mysql-local")==0){
            DRIVER = "com.mysql.jdbc.Driver";
            DB_HOST="localhost";
            DB_NAME="fbapp";
            USERNAME = "root";
            PASSWORD = "kartoos";
            URL = "jdbc:mysql://"+DB_HOST+"/"+DB_NAME;
        }else if (DbType.compareTo("mysql-heroku")==0){
            DRIVER = "com.mysql.jdbc.Driver";
            DB_HOST="us-cdbr-iron-east-03.cleardb.net";
            DB_NAME="heroku_ac01b3fc78454e8";
            USERNAME = "baa4b397146d35";
            PASSWORD = "a053ee7d";
            URL = "jdbc:mysql://"+DB_HOST+"/"+DB_NAME;
        }
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
    
    public int removeUser(long userId)
    throws Exception {
        String query="DELETE FROM users WHERE id=?";
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setLong(1,userId);
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int removeUserByFbId(String userId)
    throws Exception {
        String query="DELETE FROM users WHERE user_id=?";
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setString(1,userId);
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public static int getAge(java.util.Date birthDate){
        if (birthDate==null){
            return 0;
        }
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
        return (int)diff;
    }
    
    public int saveUserImage(UserImage image)
    throws Exception{
        String query="INSERT INTO images(user_id,picture_url)";
        query+=" VALUES(?,?)";
        
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setLong(1,image.user_id);
        pstmt.setString(2,image.picture_url);
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int updateInterested(User user)
    throws Exception {
        String query="UPDATE users SET gender_interested=?,min_age_interested=?,max_age_interested=?";
        query+=" WHERE id=?";
        
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setString(1,user.gender_interested);
        pstmt.setInt(2,user.min_age_interested);
        pstmt.setInt(3,user.max_age_interested);
        pstmt.setLong(4,user.id);
        
        //pstmt.execute();
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int saveUser(User user)
    throws Exception{
        String query="INSERT INTO users(user_id,access_token,profile_pic_url,location,name,profile_url,age,profile_id,fb_access_token,gender_interested,min_age_interested,max_age_interested,gender)";
        query+=" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setString(1,user.facebook_user_id);
        pstmt.setString(2,user.accessToken);
        pstmt.setString(3,user.profile_pic_url==null?"":user.profile_pic_url);
        pstmt.setString(4,user.location==null?"":user.location);
        pstmt.setString(5,user.name==null?"":user.name);
        pstmt.setString(6,user.profile_url);
        //pstmt.setString(7,String.valueOf(getAge(user.birthday_as_date)));
        pstmt.setInt(7,user.age);
        pstmt.setString(8,user.profile_id);
        pstmt.setString(9,user.fbAccessToken);
        pstmt.setString(10,user.gender_interested);
        pstmt.setInt(11,user.min_age_interested);
        pstmt.setInt(12,user.min_age_interested);
        pstmt.setString(13,user.gender);
        
        //pstmt.execute();
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int saveMatch(long userId1,UserMatch um)
    throws Exception{
        String query="INSERT INTO matches(user_id1,user_id2,match_viewed,match_announced) VALUES(?,?,?,?)";
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setLong(1,userId1);
        pstmt.setLong(2,um.user.id);
        pstmt.setInt(3,um.match_viewed?1:0);
        pstmt.setInt(4,um.match_announced?1:0);
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int removeMatch(long userId1,long userId2)
    throws Exception{
        String query="DELETE FROM matches WHERE user_id1=? AND user_id2=?";
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setLong(1,userId1);
        pstmt.setLong(2,userId2);
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int saveLike(long userId1,long userId2)
    throws Exception{
        String query="INSERT INTO likes(user_id1,user_id2) VALUES(?,?)";
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setLong(1,userId1);
        pstmt.setLong(2,userId2);
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int removeLike(long userId1,long userId2)
    throws Exception{
        String query="DELETE FROM likes WHERE user_id1=? AND user_id2=?";
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setLong(1,userId1);
        pstmt.setLong(2,userId2);
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int saveDislike(long userId1,long userId2)
    throws Exception{
        String query="INSERT INTO dislikes(user_id1,user_id2) VALUES(?,?)";
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setLong(1,userId1);
        pstmt.setLong(2,userId2);
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int removeDislike(long userId1,long userId2)
    throws Exception{
        String query="DELETE FROM dislikes WHERE user_id1=? AND user_id2=?";
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setLong(1,userId1);
        pstmt.setLong(2,userId2);
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int removeUserImages(long userId)
    throws Exception{
        String query="DELETE FROM images WHERE user_id=?";
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setLong(1,userId);
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int removeUserLikes(long userId)
    throws Exception {
        String query="DELETE FROM interests WHERE user_id=?";
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setLong(1,userId);
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public int saveUserImages(java.util.List<UserImage> images)
    throws Exception{
        String query="INSERT INTO images(user_id,picture_url)";
        query+=" VALUES(?,?)";
        
        int totalRows=0;
        
        PreparedStatement pstmt=connection.prepareStatement(query);
        for (int a=0;a<images.size();a++){
            UserImage img=images.get(a);
            int rc=saveUserImage(img);
            totalRows+=rc;
        }
        pstmt.close();
        return totalRows;
    }
    
    public int savePages(long userId,java.util.List<com.restfb.types.Page> pagesList)
    throws Exception {
        String query="INSERT INTO interests(user_id,page_id,name,category)";
        query+=" VALUES(?,?,?,?)";
        
        int totalRows=0;
        
        PreparedStatement pstmt=connection.prepareStatement(query);
        for (int a=0;a<pagesList.size();a++){
            com.restfb.types.Page page=pagesList.get(a);
            int rc=savePage(userId,page);
            totalRows+=rc;
        }
        pstmt.close();
        return totalRows;
    }
    
    public int savePage(long userId,com.restfb.types.Page page)
    throws Exception{
        String query="INSERT INTO interests(user_id,page_id,name,category)";
        query+=" VALUES(?,?,?,?)";
        
        PreparedStatement pstmt=connection.prepareStatement(query);
        pstmt.setLong(1,userId);
        pstmt.setString(2,page.getId());
        pstmt.setString(3,page.getName());
        pstmt.setString(4,page.getCategory());
        
        int rows=pstmt.executeUpdate();
        pstmt.close();
        return rows;
    }
    
    public User getCommonUser(User mu,ResultSet rs)
    throws Exception{
        try{
            mu.id=rs.getLong("uid");
        }catch(Exception ex){
            mu.id=rs.getLong("id");
        }
        mu.facebook_user_id=rs.getString("user_id");
        mu.name=rs.getString("name");
        mu.location=rs.getString("location");
        mu.profile_pic_url=rs.getString("profile_pic_url");
        mu.profile_url=rs.getString("profile_url");
        mu.profile_id=rs.getString("profile_id");
        mu.age=rs.getInt("age");
        
        return mu;
    }
    
    public java.util.List<UserMatch> getMatchedUsers(long userId)
    throws Exception{
        //String query="SELECT ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,m.id,m.match_viewed,m.match_announced FROM users ui,(SELECT m2.user_id2 AS user_id2 FROM matches m2 WHERE m2.user_id1='"+userId+"') AS m3 INNER JOIN matches m ON m.user_id1=m3.user_id2 WHERE ui.user_id=m.user_id1";
        String query="SELECT ui.id AS uid,ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,m.id,m.match_viewed,m.match_announced FROM users ui,matches m WHERE ui.id=m.user_id2 AND m.user_id1="+String.valueOf(userId);
        System.out.println(query);
        Statement stmt=connection.createStatement();
        ResultSet rs=stmt.executeQuery(query);
        java.util.List<UserMatch> users=new java.util.Vector<UserMatch>();
        while(rs.next()){
            User mu=new User();
            getCommonUser(mu,rs);
            UserMatch um=new UserMatch();
            um.user=mu;
            um.id=rs.getLong("id");
            um.match_viewed=rs.getInt("match_viewed")==1;
            um.match_announced=rs.getInt("match_announced")==1;
            
            users.add(um);
            
            um=null;
            mu=null;
        }
        rs.close();
        stmt.close();
        return users;
    }
    
    public java.util.List<UserImage> getUserImages(long userId)
    throws Exception{
        //String query="SELECT ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,m.id,m.match_viewed,m.match_announced FROM users ui,(SELECT m2.user_id2 AS user_id2 FROM matches m2 WHERE m2.user_id1='"+userId+"') AS m3 INNER JOIN matches m ON m.user_id1=m3.user_id2 WHERE ui.user_id=m.user_id1";
        String query="SELECT img.* FROM images img WHERE img.user_id="+String.valueOf(userId);
        Statement stmt=connection.createStatement();
        ResultSet rs=stmt.executeQuery(query);
        java.util.List<UserImage> images=new java.util.Vector<UserImage>();
        while(rs.next()){
            UserImage mu=new UserImage();
            mu.id=rs.getLong("id");
            mu.user_id=rs.getLong("user_id");
            mu.picture_url=rs.getString("picture_url");
            images.add(mu);
            mu=null;
        }
        rs.close();
        stmt.close();
        return images;
    }
    
    public java.util.List<PotentialMatchUser> getPotentialMatchingUsers(long userId)
    throws Exception{
        //String query="SELECT ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,COUNT(ul.user_id) AS cnt FROM users ui,(SELECT u2.user_id AS user_id,u2.category AS category,ui2.location AS location FROM interests u2,users ui2 WHERE u2.user_id='"+userId+"' AND ui2.user_id=u2.user_id GROUP BY u2.category) AS u3 INNER JOIN interests ul ON ul.category=u3.category WHERE ul.user_id<>u3.user_id AND ui.location<>u3.location AND ui.user_id=ul.user_id";
        //String query="SELECT ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,COUNT(ul.user_id) AS cnt FROM users ui,(SELECT u2.user_id AS user_id,u2.category AS category,ui2.location AS location FROM interests u2,users ui2 WHERE u2.user_id='"+userId+"' AND ui2.user_id=u2.user_id GROUP BY u2.category) AS u3 INNER JOIN interests ul ON ul.category=u3.category WHERE ul.user_id<>u3.user_id AND ui.location<>u3.location AND ui.user_id=ul.user_id";
        //String query="SELECT ui.id,ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,COUNT(ul.user_id) AS cnt,(SELECT COUNT(l.user_id1) FROM likes l WHERE l.user_id1=ui.id AND l.user_id2="+String.valueOf(userId)+") AS like_count,(SELECT COUNT(l.user_id1) FROM dislikes l WHERE l.user_id1=ui.id AND l.user_id2="+String.valueOf(userId)+") AS dislike_count FROM users ui,(SELECT u2.user_id AS user_id,u2.category AS category,ui2.location AS location FROM interests u2,users ui2 WHERE u2.user_id="+String.valueOf(userId)+" AND ui2.id=u2.user_id GROUP BY u2.category) AS u3 INNER JOIN interests ul ON ul.category=u3.category WHERE ul.user_id<>u3.user_id AND ui.location<>u3.location AND ui.id=ul.user_id";
        String query="SELECT ui.id,ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,COUNT(ul.user_id) AS cnt,(SELECT COUNT(l.user_id1) FROM likes l WHERE l.user_id1=ui.id AND l.user_id2="+String.valueOf(userId)+") AS like_count,(SELECT COUNT(l.user_id1) FROM dislikes l WHERE l.user_id1=ui.id AND l.user_id2="+String.valueOf(userId)+") AS dislike_count FROM users ui,(SELECT u2.user_id AS user_id,u2.category AS category,ui2.location AS location,ui2.gender_interested AS gender_interested,ui2.max_age_interested AS max_age_interested,ui2.min_age_interested AS min_age_interested FROM interests u2,users ui2 WHERE u2.user_id="+String.valueOf(userId)+" AND ui2.id=u2.user_id GROUP BY u2.category) AS u3 INNER JOIN interests ul ON ul.category=u3.category WHERE ul.user_id<>u3.user_id AND ui.location<>u3.location AND ui.id=ul.user_id";
        query+= " AND ui.gender=u3.gender_interested AND ui.age>=u3.min_age_interested AND ui.age<=u3.max_age_interested";
        query+= " AND (SELECT COUNT(*) FROM likes WHERE user_id1=u3.user_id AND user_id2=ui.id)=0" +
                " AND (SELECT COUNT(*) FROM dislikes WHERE user_id1=u3.user_id AND user_id2=ui.id)=0" +
                " AND (SELECT COUNT(*) FROM matches WHERE user_id1=u3.user_id AND user_id2=ui.id)=0";
        query+=" GROUP BY ul.user_id ORDER BY cnt DESC";
        System.out.println(query);
        Statement stmt=connection.createStatement();
        ResultSet rs=stmt.executeQuery(query);
        java.util.List<PotentialMatchUser> users=new java.util.Vector<PotentialMatchUser>();
        while(rs.next()){
            PotentialMatchUser mu=new PotentialMatchUser();
            getCommonUser(mu,rs);
            mu.count=rs.getInt("cnt");
            int like_count=rs.getInt("like_count");
            int dislike_count=rs.getInt("dislike_count");
            if (like_count>0){
                mu.like_state="true";
                users.add(mu);
            }else if (dislike_count>0){
                mu.like_state="false";
            }else{
                mu.like_state="none";
                users.add(mu);
            }
        }
        rs.close();
        stmt.close();
        return users;
    }
    
    public User getUserByToken(String token)
    throws Exception{
        String query="SELECT id,user_id,name,age,location,profile_pic_url,profile_id,gender,gender_interested,min_age_interested,max_age_interested FROM users WHERE access_token='"+token+"'";
        System.out.println(query);
        Statement stmt=connection.createStatement();
        ResultSet rs=stmt.executeQuery(query);
        User mu=null;
        if(rs.next()){
            mu=new User();
            mu.id=rs.getLong("id");
            mu.facebook_user_id=rs.getString("user_id");
            mu.age=rs.getInt("age");
            mu.name=rs.getString("name");
            mu.profile_pic_url=rs.getString("profile_pic_url");
            mu.profile_id=rs.getString("profile_id");
            mu.gender=rs.getString("gender");
            mu.gender_interested=rs.getString("gender_interested");
            mu.max_age_interested=rs.getInt("max_age_interested");
            mu.min_age_interested=rs.getInt("min_age_interested");
        }
        rs.close();
        stmt.close();
        return mu;
    }
    
    public User getUserByFbId(String token)
    throws Exception{
        String query="SELECT id,access_token,user_id,name,age,location,profile_pic_url,profile_id,gender,gender_interested,min_age_interested,max_age_interested FROM users WHERE user_id='"+token+"'";
        System.out.println(query);
        Statement stmt=connection.createStatement();
        ResultSet rs=stmt.executeQuery(query);
        User mu=null;
        if(rs.next()){
            mu=new User();
            mu.id=rs.getLong("id");
            mu.accessToken=rs.getString("access_token");
            mu.facebook_user_id=rs.getString("user_id");
            mu.age=rs.getInt("age");
            mu.name=rs.getString("name");
            mu.profile_pic_url=rs.getString("profile_pic_url");
            mu.profile_id=rs.getString("profile_id");
            mu.gender=rs.getString("gender");
            mu.gender_interested=rs.getString("gender_interested");
            mu.max_age_interested=rs.getInt("max_age_interested");
            mu.min_age_interested=rs.getInt("min_age_interested");
        }
        rs.close();
        stmt.close();
        return mu;
    }
}
