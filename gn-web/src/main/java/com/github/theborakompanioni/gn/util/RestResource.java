package com.github.theborakompanioni.gn.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import org.springframework.hateoas.Link;

import javax.xml.bind.annotation.XmlElement;
import java.util.Map;
import java.util.Objects;

/**
 * Created by void on 20.09.15.
 */
public abstract class RestResource<T> {
    private Map<String, Link> links = Maps.newHashMap();

    public void add(Link link) {
        Objects.requireNonNull(link, "Link must not be null!");
        this.links.put(link.getRel(), link);
    }

    @XmlElement(
            name = "link",
            namespace = "http://www.w3.org/2005/Atom"
    )
    @JsonProperty("_links")
    public Map<String, Link> getLinks() {
        return this.links;
    }

    @JsonProperty("resource")
    public abstract T getResource();
}
