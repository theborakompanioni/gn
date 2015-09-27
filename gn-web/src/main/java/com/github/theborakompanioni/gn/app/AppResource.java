package com.github.theborakompanioni.gn.app;

import com.github.theborakompanioni.gn.article.ArticleRestCtrl;
import com.github.theborakompanioni.gn.util.RestResource;
import org.apache.shiro.SecurityUtils;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by void on 20.09.15.
 */
public class AppResource extends RestResource<Object> {

    public AppResource() {
        this.add(linkTo(methodOn(ArticleRestCtrl.class).popular(null)).withRel("popular"));
        this.add(linkTo(methodOn(ArticleRestCtrl.class).newest(null)).withRel("newest"));
        this.add(linkTo(methodOn(ArticleRestCtrl.class).popular(null)).withRel("trending"));
        this.add(linkTo(methodOn(ArticleRestCtrl.class).verification(null)).withRel("verification"));

        this.add(linkTo(methodOn(ArticleRestCtrl.class).create(null)).withRel("create"));

        if (!SecurityUtils.getSubject().isAuthenticated()) {
            this.add(linkTo(methodOn(AuthRestCtrl.class).login(null)).withRel("login"));
        }

        if (SecurityUtils.getSubject().isAuthenticated()) {
            this.add(linkTo(methodOn(AuthRestCtrl.class).logout()).withRel("logout"));
        }
    }

    public Object getResource() {
        return "";
    }
}
