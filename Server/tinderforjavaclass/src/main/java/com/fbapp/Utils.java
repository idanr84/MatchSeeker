package com.fbapp;

import com.fbapp.model.User;
import com.fbapp.model.UserImage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

/**
 * @author Yaara Shoham
 */
public abstract class Utils {

    private final static long secondsInYear = 31536000;

    public static int getAge(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }
        long currentDateTime = new Date().getTime();
        long birthDateTime = birthDate.getTime();
        long age = currentDateTime - birthDateTime;
        if (age > 0) {
            age = age / 1000;
            age = age / (secondsInYear);
            if (age > 0) {
                System.out.println("Age: " + String.valueOf(age));
            } else {
                System.out.println("Invalid values for age");
            }
        } else {
            System.out.println("Invalid values for age");
        }
        return (int) age;
    }

    public static User getCommonUser(User user, ResultSet resultSet) throws Exception {
        try {
            user.id = resultSet.getLong("uid");
        } catch (Exception ex) {
            user.id = resultSet.getLong("id");
        }
        user.facebook_user_id = resultSet.getString("user_id");
        user.name = resultSet.getString("name");
        user.location = resultSet.getString("location");
        user.profile_pic_url = resultSet.getString("profile_pic_url");
        user.profile_url = resultSet.getString("profile_url");
        user.profile_id = resultSet.getString("profile_id");
        user.age = resultSet.getInt("age");

        return user;
    }
}
