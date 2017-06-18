package com.enya.jee.test.service;

import com.enay.jee.test.model.User;
import com.enya.jee.test.dao.UserDao;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class UserService {

    @EJB
    private UserDao userDao;

    public User getUser() {
        User user = userDao.getUser();
        user.setAge(33);
        user.setName("Enya");
        return user;
    }

    public void addUser(final String name, final int age) {
        User user = new User();
        user.setName(name);
        user.setAge(age);
        userDao.addUser(user);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}
