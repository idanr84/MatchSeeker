
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Album;
import com.restfb.types.Page;
import com.restfb.types.Photo;
import com.restfb.types.User;

import java.util.List;
import java.util.Vector;
/**
 * FbHelper is the object of facebook client
 * This class interacts with facebook Graph API
 *
 * @see com.restfb
 * @author Yaara Shoham
 */
public class FbHelper {
    FacebookClient facebookClient;
    String accessToken;

    public FbHelper(String accessToken){
        this.accessToken = accessToken;
        facebookClient = new DefaultFacebookClient(accessToken,com.restfb.Version.LATEST);
    }
    
    public List<Page> getAllLikes(){
        List<Page> pagesList = new Vector<Page>();
        Connection<Page> pages = this.getLikes();
        pagesList.addAll(pages.getData());
        while(pages.hasNext()){
            pages = this.getMoreLikes(pages.getNextPageUrl());
            pagesList.addAll(pages.getData());
        }
        return pagesList;
    }
    
    public Connection<Page> getMoreLikes(String nextUrl){
        //com.restfb.Parameter pm=com.restfb.Parameter.with("fields","id,name,category");
        Connection<Page> pages = facebookClient.fetchConnectionPage(nextUrl,Page.class);
        return pages;
    }
    
    public Connection<Page> getLikes(){
        Parameter parameter = Parameter.with("fields","id,name,category");
        Connection<Page> pages = facebookClient.fetchConnection("me/likes",Page.class,parameter);
        return pages;
    }
    
    private static com.fbapp.model.User createFromFacebookUser(User user){
        com.fbapp.model.User user1 = new com.fbapp.model.User();
        user1.birthday_as_date = user.getBirthdayAsDate();
        user1.location = user.getLocation() == null? null : user.getLocation().getName();
        user1.name = user.getName();
        user1.profile_pic_url = user.getPicture() == null ? null : user.getPicture().getUrl();
        user1.profile_id = user.getId();
        user1.profile_url = user.getLink();
        user1.gender = user.getGender() == null ? "" : user.getGender();
        return user1;
    }
    
    public com.fbapp.model.User getMe(){
        //User user = facebookClient.fetchObject("me",User.class);
        Parameter parameter = Parameter.with("fields","id,name,birthday,picture.type(large),first_name,last_name,location,link");
        User user = facebookClient.fetchObject("me",User.class,parameter);
        return createFromFacebookUser(user);
    }
    
    public List<Album> getAlbums(){
        //User user = facebookClient.fetchObject("me",User.class);
        Parameter parameter = Parameter.with("fields","id,name,type");
        Connection<Album> albums = facebookClient.fetchConnection("me/albums",Album.class,parameter);
        List<Album> pagesList = new Vector<Album>();
        pagesList.addAll(albums.getData());
        return pagesList;
    }
    
    public List<Photo> getPictures(String albumid){
        //User user = facebookClient.fetchObject("me",User.class);
        Parameter parameter = Parameter.with("fields","id,picture.type(large)");
        Connection<Photo> albums = facebookClient.fetchConnection(albumid + "/photos", Photo.class,parameter);
        List<Photo> pagesList = new Vector<Photo>();
        pagesList.addAll(albums.getData());
        return pagesList;
    }
}
