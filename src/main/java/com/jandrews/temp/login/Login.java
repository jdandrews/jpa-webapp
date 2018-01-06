package com.jandrews.temp.login;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Named
@SessionScoped
public class Login implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    Credentials credentials;

    @Inject
    EntityManager userDatabase;

    private User user;

    public void login() {
        List<User> results = userDatabase.createQuery(
                "select u from User u where u.username = :username and u.password = :password")
                .setParameter("username", credentials.getUsername())
                .setParameter("password", credentials.getPassword())
                .getResultList();

        if (!results.isEmpty()) {
            user = results.get(0);
        }
        else {
            // perhaps add code here to report a failed login
        }
    }

    public void logout() {
        user = null;
    }

    public boolean isLoggedIn() {

        return user != null;

    }

    public String getUsername() {
        return user.getUsername();
    }

    @Produces
    @LoggedIn
    User getCurrentUser() {
        return user;
    }
}
