/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp.model;

import java.util.List;

/**
 * SubmitModel is the data model to hold the list of users whom were liked / disliked / matched by the
 * current user
 *
 * @author Yaara Shoham
 */
public class SubmitModel {
    public List<Long> liked_users;
    public List<Long> disliked_users;
    public List<SubmitMatchUser> matched_users;
}
