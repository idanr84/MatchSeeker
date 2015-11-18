/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp.model;

/**
 * SubmitMatchUser is the data model to hold a user choosing matched users from the PotentialMatchUser list
 * who liked a user previously
 *
 * @author Yaara Shoham
 */
public class SubmitMatchUser {
    public long user_id;
    public boolean match_announced;
    public boolean match_viewed;
}
