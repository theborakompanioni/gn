package gn.elastic.repository;

import model.Permission;
import model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 * Created by void on 22.09.15.
 */
public interface PermissionElasticRepository extends ElasticsearchCrudRepository<Permission, String> {
}
