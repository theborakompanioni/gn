package gn.elastic.repository;

import model.Role;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 * Created by void on 22.09.15.
 */
public interface RoleElasticRepository extends ElasticsearchCrudRepository<Role, String> {
}
