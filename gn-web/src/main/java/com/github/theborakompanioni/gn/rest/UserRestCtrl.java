package com.github.theborakompanioni.gn.rest;

import gn.elastic.repository.UserElasticRepository;
import model.User;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/users")
public class UserRestCtrl {
    private static final Logger log = LoggerFactory.
            getLogger(UserRestCtrl.class);

    @Autowired
    private UserElasticRepository userRepo;

    @RequestMapping(method = GET)
    @RequiresAuthentication
    @RequiresRoles("administrator")
    public ResponseEntity<Iterable<User>> findAll() {
        return ResponseEntity.ok(userRepo.findAll());
    }
}
