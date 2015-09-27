package com.github.theborakompanioni.gn.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppRestCtrl {
    private static final Logger log = LoggerFactory.
            getLogger(AppRestCtrl.class);

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<AppResource> appEntry() {
        AppResource appResource = new AppResource();
        return ResponseEntity.ok(appResource);
    }
}
