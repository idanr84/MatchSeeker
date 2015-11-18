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
import com.restfb.types.Page;

import java.sql.*;
import java.util.List;
import java.util.Vector;

/**
 * @author Yaara Shoham
 */
public class SqlQueries extends DBHandler{

    private PreparedStatement preparedStatement;
    private String queryString;

    public SqlQueries() throws Exception {
       super();
        this.open();
    }

    public void removeUser(long userId) throws Exception {
        queryString = "DELETE FROM users " +
                "WHERE id=?";
        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setLong(1, userId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void removeUserByFbId(String userId) throws Exception {
        queryString = "DELETE FROM users " +
                "WHERE user_id=?";
        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, userId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public List<UserMatch> getMatchedUsers(long userId) throws Exception {
       /* String query="SELECT ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,m.id,m.match_viewed,m.match_announced
         FROM users ui,(SELECT m2.user_id2 AS user_id2 FROM matches m2 WHERE m2.user_id1='"+userId+"') AS m3
         INNER JOIN matches m ON m.user_id1=m3.user_id2
         WHERE ui.user_id=m.user_id1";*/
        queryString = "SELECT ui.id AS uid,ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,m.id,m.match_viewed,m.match_announced " +
                "FROM users ui,matches m " +
                "WHERE ui.id=m.user_id2 AND m.user_id1=" + String.valueOf(userId);
        System.out.println(queryString);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(queryString);
        List<UserMatch> users = new Vector<UserMatch>();

        while (resultSet.next()) {
            User matchedUser = new User();
            Utils.getCommonUser(matchedUser, resultSet);
            UserMatch userMatch = new UserMatch();
            userMatch.user = matchedUser;
            userMatch.id = resultSet.getLong("id");
            userMatch.match_viewed = resultSet.getInt("match_viewed") == 1;
            userMatch.match_announced = resultSet.getInt("match_announced") == 1;

            users.add(userMatch);
        }
        resultSet.close();
        statement.close();
        return users;
    }

    public List<UserImage> getUserImages(long userId) throws Exception {
        /*String query="SELECT ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,m.id,m.match_viewed,m.match_announced
         FROM users ui,(SELECT m2.user_id2 AS user_id2
                      FROM matches m2 WHERE m2.user_id1='"+userId+"') AS m3
                      INNER JOIN matches m ON m.user_id1=m3.user_id2
                      WHERE ui.user_id=m.user_id1";*/
        queryString = "SELECT img.* " +
                "FROM images img " +
                "WHERE img.user_id=" + String.valueOf(userId);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(queryString);
        List<UserImage> images = new Vector<UserImage>();

        while (resultSet.next()) {
            UserImage userImage = new UserImage();
            userImage.id = resultSet.getLong("id");
            userImage.user_id = resultSet.getLong("user_id");
            userImage.picture_url = resultSet.getString("picture_url");
            images.add(userImage);
        }
        resultSet.close();
        statement.close();
        return images;
    }

    public void saveUserImage(UserImage image) throws Exception {
        queryString = "INSERT INTO images(user_id, picture_url)" +
                " VALUES(?, ?)";

        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setLong(1, image.user_id);
        preparedStatement.setString(2, image.picture_url);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void updateInterested(User user) throws Exception {
        queryString = "UPDATE users " +
                "SET gender_interested=?, min_age_interested=?, max_age_interested=? " +
                "WHERE id=?";

        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, user.gender_interested);
        preparedStatement.setInt(2, user.min_age_interested);
        preparedStatement.setInt(3, user.max_age_interested);
        preparedStatement.setLong(4, user.id);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void saveUser(User user) throws Exception {
        queryString = "INSERT INTO users(user_id, access_token, profile_pic_url, location, name, profile_url, age, profile_id,fb_access_token, gender_interested, min_age_interested, max_age_interested, gender) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, user.facebook_user_id);
        preparedStatement.setString(2, user.accessToken);
        preparedStatement.setString(3, user.profile_pic_url == null ? "" : user.profile_pic_url);
        preparedStatement.setString(4, user.location == null ? "" : user.location);
        preparedStatement.setString(5, user.name == null ? "" : user.name);
        preparedStatement.setString(6, user.profile_url);
        preparedStatement.setInt(7, user.age);
        preparedStatement.setString(8, user.profile_id);
        preparedStatement.setString(9, user.fbAccessToken);
        preparedStatement.setString(10, user.gender_interested);
        preparedStatement.setInt(11, user.min_age_interested);
        preparedStatement.setInt(12, user.min_age_interested);
        preparedStatement.setString(13, user.gender);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void saveMatch(long userId1, UserMatch um) throws Exception {
        queryString = "INSERT INTO matches(user_id1, user_id2, match_viewed, match_announced) " +
                "VALUES(?,?,?,?)";
        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setLong(1, userId1);
        preparedStatement.setLong(2, um.user.id);
        preparedStatement.setInt(3, um.match_viewed ? 1 : 0);
        preparedStatement.setInt(4, um.match_announced ? 1 : 0);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void removeMatch(long userId1, long userId2) throws Exception {
        queryString = "DELETE FROM matches " +
                "WHERE user_id1=? AND user_id2=?";
        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setLong(1, userId1);
        preparedStatement.setLong(2, userId2);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void saveLike(long userId1, long userId2) throws Exception {
        queryString = "INSERT INTO likes(user_id1,user_id2) " +
                "VALUES(?,?)";

        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setLong(1, userId1);
        preparedStatement.setLong(2, userId2);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void removeLike(long userId1, long userId2) throws Exception {
        queryString = "DELETE FROM likes " +
                "WHERE user_id1=? AND user_id2=?";

        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setLong(1, userId1);
        preparedStatement.setLong(2, userId2);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void saveDislike(long userId1, long userId2) throws Exception {
        queryString = "INSERT INTO dislikes(user_id1,user_id2) " +
                "VALUES(?,?)";

        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setLong(1, userId1);
        preparedStatement.setLong(2, userId2);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void removeDislike(long userId1, long userId2) throws Exception {
        queryString = "DELETE FROM dislikes " +
                "WHERE user_id1=? AND user_id2=?";

        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setLong(1, userId1);
        preparedStatement.setLong(2, userId2);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void removeUserImages(long userId) throws Exception {
        queryString = "DELETE FROM images " +
                "WHERE user_id=?";

        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setLong(1, userId);

        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

    public void removeUserLikes(long userId) throws Exception {
        queryString = "DELETE FROM interests " +
                "WHERE user_id=?";
        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setLong(1, userId);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void saveUserImages(List<UserImage> images) throws Exception {
        queryString = "INSERT INTO images(user_id,picture_url)" +
                 "VALUES(?,?)";

        preparedStatement = connection.prepareStatement(queryString);
        for (UserImage image : images) {
            saveUserImage(image);
        }

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }



    public void savePages(long userId, List<Page> pagesList) throws Exception {
        queryString = "INSERT INTO interests(user_id, page_id, name, category) " +
                "VALUES(?,?,?,?)";

        preparedStatement = connection.prepareStatement(queryString);
        for (Page page : pagesList) {
            savePage(userId, page);
        }

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void savePage(long userId, Page page) throws Exception {
        queryString = "INSERT INTO interests(user_id, page_id, name, category)" +
                " VALUES(?,?,?,?)";

        preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setLong(1, userId);
        preparedStatement.setString(2, page.getId());
        preparedStatement.setString(3, page.getName());
        preparedStatement.setString(4, page.getCategory());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public List<PotentialMatchUser> getPotentialMatchingUsers(long userId) throws Exception {
        /*String query="SELECT ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,COUNT(ul.user_id) AS cnt FROM users ui,(SELECT u2.user_id AS user_id,u2.category AS category,ui2.location AS location FROM interests u2,users ui2 WHERE u2.user_id='"+userId+"' AND ui2.user_id=u2.user_id GROUP BY u2.category) AS u3 INNER JOIN interests ul ON ul.category=u3.category WHERE ul.user_id<>u3.user_id AND ui.location<>u3.location AND ui.user_id=ul.user_id";
        String query="SELECT ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,COUNT(ul.user_id) AS cnt FROM users ui,(SELECT u2.user_id AS user_id,u2.category AS category,ui2.location AS location FROM interests u2,users ui2 WHERE u2.user_id='"+userId+"' AND ui2.user_id=u2.user_id GROUP BY u2.category) AS u3 INNER JOIN interests ul ON ul.category=u3.category WHERE ul.user_id<>u3.user_id AND ui.location<>u3.location AND ui.user_id=ul.user_id";
        String query="SELECT ui.id,ui.user_id,ui.name,ui.location,ui.profile_pic_url,ui.profile_url,ui.profile_id,ui.age,COUNT(ul.user_id) AS cnt,(SELECT COUNT(l.user_id1) FROM likes l WHERE l.user_id1=ui.id AND l.user_id2="+String.valueOf(userId)+") AS like_count,(SELECT COUNT(l.user_id1) FROM dislikes l WHERE l.user_id1=ui.id AND l.user_id2="+String.valueOf(userId)+") AS dislike_count FROM users ui,(SELECT u2.user_id AS user_id,u2.category AS category,ui2.location AS location FROM interests u2,users ui2 WHERE u2.user_id="+String.valueOf(userId)+" AND ui2.id=u2.user_id GROUP BY u2.category) AS u3 INNER JOIN interests ul ON ul.category=u3.category WHERE ul.user_id<>u3.user_id AND ui.location<>u3.location AND ui.id=ul.user_id";*/
        queryString = "SELECT ui.id,ui.user_id, ui.name,ui.location, ui.profile_pic_url, ui.profile_url, ui.profile_id,ui.age, COUNT(ul.user_id) AS cnt, (SELECT COUNT(l.user_id1) FROM likes l WHERE l.user_id1=ui.id AND l.user_id2=" + String.valueOf(userId) + ") AS like_count,(SELECT COUNT(l.user_id1) FROM dislikes l WHERE l.user_id1=ui.id AND l.user_id2=" + String.valueOf(userId) + ") AS dislike_count FROM users ui,(SELECT u2.user_id AS user_id,u2.category AS category,ui2.location AS location,ui2.gender_interested AS gender_interested,ui2.max_age_interested AS max_age_interested,ui2.min_age_interested AS min_age_interested FROM interests u2,users ui2 WHERE u2.user_id=" + String.valueOf(userId) + " AND ui2.id=u2.user_id GROUP BY u2.category) AS u3 INNER JOIN interests ul ON ul.category=u3.category WHERE ul.user_id<>u3.user_id AND ui.location<>u3.location AND ui.id=ul.user_id" + " AND ui.gender=u3.gender_interested AND ui.age>=u3.min_age_interested AND ui.age<=u3.max_age_interested AND (SELECT COUNT(*) FROM likes WHERE user_id1=u3.user_id AND user_id2=ui.id)=0 AND (SELECT COUNT(*) FROM dislikes WHERE user_id1=u3.user_id AND user_id2=ui.id)=0 AND (SELECT COUNT(*) FROM matches WHERE user_id1=u3.user_id AND user_id2=ui.id)=0 GROUP BY ul.user_id ORDER BY cnt DESC";

        System.out.println(queryString);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(queryString);
        List<PotentialMatchUser> users = new Vector<PotentialMatchUser>();

        while (resultSet.next()) {
            PotentialMatchUser potentialMatchUser = new PotentialMatchUser();
            Utils.getCommonUser(potentialMatchUser, resultSet);
            potentialMatchUser.count = resultSet.getInt("cnt");
            int like_count = resultSet.getInt("like_count");
            int dislike_count = resultSet.getInt("dislike_count");

            if (like_count > 0) {
                potentialMatchUser.like_state = "true";
                users.add(potentialMatchUser);
            } else if (dislike_count > 0) {
                potentialMatchUser.like_state = "false";
            } else {
                potentialMatchUser.like_state = "none";
                users.add(potentialMatchUser);
            }
        }
        resultSet.close();
        statement.close();
        return users;
    }

    public User getUserByToken(String token) throws Exception {
        queryString = "SELECT id,user_id,name,age,location,profile_pic_url,profile_id,gender,gender_interested,min_age_interested,max_age_interested " +
                "FROM users " +
                "WHERE access_token='" + token + "'";
        System.out.println(queryString);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(queryString);
        User user = null;

        if (resultSet.next()) {
            user = new User();
            user.id = resultSet.getLong("id");
            user.facebook_user_id = resultSet.getString("user_id");
            user.age = resultSet.getInt("age");
            user.name = resultSet.getString("name");
            user.profile_pic_url = resultSet.getString("profile_pic_url");
            user.profile_id = resultSet.getString("profile_id");
            user.gender = resultSet.getString("gender");
            user.gender_interested = resultSet.getString("gender_interested");
            user.max_age_interested = resultSet.getInt("max_age_interested");
            user.min_age_interested = resultSet.getInt("min_age_interested");
        }
        resultSet.close();
        statement.close();
        return user;
    }

    public User getUserByFbId(String token) throws Exception {
        queryString = "SELECT id,access_token,user_id,name,age,location,profile_pic_url,profile_id,gender,gender_interested,min_age_interested,max_age_interested " +
                "FROM users " +
                "WHERE user_id='" + token + "'";
        System.out.println(queryString);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(queryString);
        User user = null;

        if (resultSet.next()) {
            user = new User();
            user.id = resultSet.getLong("id");
            user.accessToken = resultSet.getString("access_token");
            user.facebook_user_id = resultSet.getString("user_id");
            user.age = resultSet.getInt("age");
            user.name = resultSet.getString("name");
            user.profile_pic_url = resultSet.getString("profile_pic_url");
            user.profile_id = resultSet.getString("profile_id");
            user.gender = resultSet.getString("gender");
            user.gender_interested = resultSet.getString("gender_interested");
            user.max_age_interested = resultSet.getInt("max_age_interested");
            user.min_age_interested = resultSet.getInt("min_age_interested");
        }
        resultSet.close();
        statement.close();
        return user;
    }
}
