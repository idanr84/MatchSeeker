/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp.model;

/**
 *
 * @author Yaara Shoham
 */
public class User {
    public long id;
    public String facebook_user_id;
    public String accessToken;
    public String fbAccessToken;
    public Integer age;
    public String name;
    public String location;
    public String profile_id;
    public String profile_url;
    public String profile_pic_url;
    public java.util.Date birthday_as_date;
    public java.util.List<UserImage> images;
    public String gender;
    public String gender_interested;
    public Integer min_age_interested=null;
    public Integer max_age_interested=null;
}