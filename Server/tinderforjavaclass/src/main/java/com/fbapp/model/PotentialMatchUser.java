/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp.model;

/**
 * PotentialMatchUser is the data model to hold the list of users who belong in the interest gender &amp; age range
 * of the current user.
 * <code>count</code> resembles the number of page categories in common.
 * <code>like_state</code> if PotentialMatchUser liked current user or not.
 *
 * @author Yaara Shoham
 */
public class PotentialMatchUser extends User {
    public int count;
    public String like_state;
}
