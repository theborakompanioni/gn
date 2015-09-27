package model;

/**
 * Created by void on 25.09.15.
 */
public enum Permissions {
    ARTICLE_DELETE("article:delete"),
    ARTICLE_VERIFY("article:verify");

    private String name;

    Permissions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
