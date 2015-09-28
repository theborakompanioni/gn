package com.github.theborakompanioni.gn.article;

import com.github.theborakompanioni.gn.util.RestResource;
import model.Article;
import model.Permissions;
import org.apache.shiro.SecurityUtils;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by void on 20.09.15.
 */
public class ArticleResource extends RestResource<Article> {
    private Article article;

    public ArticleResource(Article article) {
        this.article = article;

        this.add(linkTo(methodOn(ArticleRestCtrl.class).getOne(article.getId())).withSelfRel());
        this.add(linkTo(methodOn(ArticleCtrl.class).view(article.getId())).withRel("view"));

        this.add(linkTo(methodOn(ArticleRestCtrl.class).heart(article.getId())).withRel("heart"));

        if (SecurityUtils.getSubject().isPermitted(Permissions.ARTICLE_DELETE.getName())) {
            this.add(linkTo(methodOn(ArticleRestCtrl.class).deleteOne(article.getId())).withRel("remove"));
        }

        if (SecurityUtils.getSubject().isPermitted(Permissions.ARTICLE_VERIFY.getName()) && !article.isVerified()) {
            this.add(linkTo(methodOn(ArticleRestCtrl.class).verify(article.getId())).withRel("verify"));
        }
    }

    public Article getResource() {
        return article;
    }
}
