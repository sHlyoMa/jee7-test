package com.enya.jee.test.dao;

import com.enay.jee.test.model.User;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
@RolesAllowed("user")
public class UserDao {

    @PersistenceContext(name = "jee-test-pu")
    private EntityManager entityManager;

    public User getUser() {
        return new User();
    }

    public List<User> getAllUsers() {
        return entityManager.createNamedQuery("User.findAll", User.class).getResultList();
    }

    public void addUser(User user) {
        entityManager.persist(user);
    }
}
