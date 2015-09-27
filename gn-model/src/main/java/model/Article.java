package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;
import javax.persistence.Version;

@Document(indexName = "article")
@JsonIgnoreProperties(value = {"handler"})
public class Article {

    @Id
    private String id;
    @Version
    @JsonIgnore
    private Long version;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Long createdAt;

    private String url;

    private String title;
    private String description;
    private String language;


    private long hearts;
    @JsonIgnore
    private long views;

    private boolean fetched;

    private boolean verified;

    @JsonIgnore
    private String fetchedTitle;
    @JsonIgnore
    private String fetchedDescription;
    @JsonIgnore
    private String fetchedText;
    @JsonIgnore
    private String fetchedVideoUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public long getHearts() {
        return hearts;
    }

    public void setHearts(long hearts) {
        this.hearts = hearts;
    }

    public String getFetchedVideoUrl() {
        return fetchedVideoUrl;
    }

    public void setFetchedVideoUrl(String fetchedVideoUrl) {
        this.fetchedVideoUrl = fetchedVideoUrl;
    }

    public String getFetchedText() {
        return fetchedText;
    }

    public void setFetchedText(String fetchedText) {
        this.fetchedText = fetchedText;
    }

    public String getFetchedTitle() {
        return fetchedTitle;
    }

    public void setFetchedTitle(String fetchedTitle) {
        this.fetchedTitle = fetchedTitle;
    }

    public boolean isFetched() {
        return fetched;
    }

    public void setFetched(boolean fetched) {
        this.fetched = fetched;
    }

    public String getFetchedDescription() {
        return fetchedDescription;
    }

    public void setFetchedDescription(String fetchedDescription) {
        this.fetchedDescription = fetchedDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
