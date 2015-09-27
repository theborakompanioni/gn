package com.github.theborakompanioni.gn.app;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/auth")
public class AuthRestCtrl {
    private static final Logger log = LoggerFactory.
            getLogger(AuthRestCtrl.class);

    @RequestMapping(value = "/login", method = POST)
    public ResponseEntity<Void> login(@RequestBody final UsernamePasswordToken credentials) {
        log.info("Authenticating {}", credentials.getUsername());
        final Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(credentials);
            // set attribute that will allow session querying
            subject.getSession().setAttribute("email", credentials.getUsername());

            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("X-Status-Reason", "Authentication failed for token submission.")
                    .build();
        }
    }

    @RequestMapping(value = "/logout", method = POST)
    public ResponseEntity<Void> logout() {
        final Subject subject = SecurityUtils.getSubject();
        subject.logout();

        return ResponseEntity.ok().build();
    }
}
