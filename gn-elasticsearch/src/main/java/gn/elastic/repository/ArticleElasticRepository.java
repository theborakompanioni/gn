package gn.elastic.repository;

import model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by void on 22.09.15.
 */
@RepositoryRestResource(collectionResourceRel = "articless", path = "articless")
public interface ArticleElasticRepository extends ElasticsearchCrudRepository<Article, String> {
}
