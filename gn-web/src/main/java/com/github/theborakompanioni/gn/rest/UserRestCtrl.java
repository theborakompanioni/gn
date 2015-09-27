package com.github.theborakompanioni.gn.rest;

import com.github.theborakompanioni.gn.repository.UserRepository;
import model.User;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/users")
public class UserRestCtrl {
    private static final Logger log = LoggerFactory.
            getLogger(UserRestCtrl.class);

    @Autowired
    private UserRepository userRepo;

    @RequestMapping(method = GET)
    @RequiresAuthentication
    @RequiresRoles("administrator")
    public List<User> getAll() {
        return userRepo.findAll();
    }
}
