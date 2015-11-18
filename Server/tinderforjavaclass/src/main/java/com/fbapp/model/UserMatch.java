/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp.model;

/**
 * UserMatch is the data model to hold the user matched by current user
 *
 * @author Yaara Shoham
 */
public class UserMatch {
    public User user;
    public long id;
    public boolean match_announced;
    public boolean match_viewed;
}
