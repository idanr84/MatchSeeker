
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp;

import com.restfb.types.User;
import com.restfb.types.Page;
import com.restfb.types.Album;
import com.restfb.types.Photo;
import com.restfb.Connection;
import java.util.List;

/**
 *
 * @author Rameez Usmani
 */
public class FbHelper {
    com.restfb.FacebookClient facebookClient=null;
    String accessToken;

    public FbHelper(String accessToken){
        this.accessToken=accessToken;
        facebookClient=new com.restfb.DefaultFacebookClient(accessToken,com.restfb.Version.LATEST);
    }
    
    public List<Page> getAllLikes(){
        List<Page> pagesList=new java.util.Vector<Page>();
        Connection<Page> pages=this.getLikes();
        pagesList.addAll(pages.getData());
        while(pages.hasNext()){
            pages=this.getMoreLikes(pages.getNextPageUrl());
            pagesList.addAll(pages.getData());
        }
        return pagesList;
    }
    
    public Connection<Page> getMoreLikes(String nextUrl){
        //com.restfb.Parameter pm=com.restfb.Parameter.with("fields","id,name,category");
        Connection<Page> pages=facebookClient.fetchConnectionPage(nextUrl,Page.class);
        return pages;
    }
    
    public Connection<Page> getLikes(){
        com.restfb.Parameter pm=com.restfb.Parameter.with("fields","id,name,category");
        Connection<Page> pages=facebookClient.fetchConnection("me/likes",Page.class,pm);
        return pages;
    }
    
    private static com.fbapp.model.User createFromFbUser(User user){
        com.fbapp.model.User u=new com.fbapp.model.User();
        u.birthday_as_date=user.getBirthdayAsDate();
        u.location=user.getLocation()==null?null:user.getLocation().getName();
        u.name=user.getName();
        u.profile_pic_url=user.getPicture()==null?null:user.getPicture().getUrl();
        u.profile_id=user.getId();
        u.profile_url=user.getLink();
        u.gender=user.getGender()==null?"":user.getGender();
        return u;
    }
    
    public com.fbapp.model.User getMe(){
        //User user = facebookClient.fetchObject("me",User.class);
        com.restfb.Parameter pm=com.restfb.Parameter.with("fields","id,name,birthday,picture.type(large),first_name,last_name,location,link");
        User user=facebookClient.fetchObject("me",User.class,pm);
        return createFromFbUser(user);
    }
    
    public List<com.restfb.types.Album> getAlbums(){
        //User user = facebookClient.fetchObject("me",User.class);
        com.restfb.Parameter pm=com.restfb.Parameter.with("fields","id,name,type");
        Connection<Album> albums=facebookClient.fetchConnection("me/albums",Album.class,pm);
        List<Album> pagesList=new java.util.Vector<Album>();
        pagesList.addAll(albums.getData());
        return pagesList;
    }
    
    public List<com.restfb.types.Photo> getPictures(String albumid){
        //User user = facebookClient.fetchObject("me",User.class);
        com.restfb.Parameter pm=com.restfb.Parameter.with("fields","id,picture.type(large)");
        Connection<Photo> albums=facebookClient.fetchConnection(albumid+"/photos",Photo.class,pm);
        List<Photo> pagesList=new java.util.Vector<Photo>();
        pagesList.addAll(albums.getData());
        return pagesList;
    }
}
