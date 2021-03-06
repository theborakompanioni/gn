package gn.elastic.repository;

import model.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 * Created by void on 22.09.15.
 */
public interface ArticleElasticRepository extends ElasticsearchCrudRepository<Article, String> {
}
