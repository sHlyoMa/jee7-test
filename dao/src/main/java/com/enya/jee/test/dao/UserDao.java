package com.enya.jee.test.dao;

import com.enay.jee.test.model.User;

import javax.ejb.Stateless;

/**
 * Created by Enya on 6/18/2017.
 */
@Stateless
public class UserDao {

    public User getUser() {
        return new User();
    }
}
