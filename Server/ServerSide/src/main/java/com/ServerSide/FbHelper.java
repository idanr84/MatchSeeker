
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ServerSide;

import com.restfb.Connection;
import com.restfb.types.Page;
import com.restfb.types.User;

import java.util.List;

/**
 *
 * @author yaarashoham
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
    
    public com.restfb.types.User getMe(){
        //User user = facebookClient.fetchObject("me",User.class);
        com.restfb.Parameter pm=com.restfb.Parameter.with("fields","id,name,birthday,picture,first_name,last_name,location,link");
        User user=facebookClient.fetchObject("me",User.class,pm);
        return user;
    }
}
