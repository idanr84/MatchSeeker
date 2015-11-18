/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp.model;

import java.util.List;

/**
 * MeModel is the data model to hold the the response details for <code>/me</code> servlet
 *
 * @author Yaara Shoham
 */
public class MeModel {
    public User user;
    public List<PotentialMatchUser> potential_matches;
    public List<UserMatch> matched_users;
}
