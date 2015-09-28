package gn.elastic.repository;

import model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by void on 22.09.15.
 */
public interface UserElasticRepository extends ElasticsearchCrudRepository<User, String> {
    List<User> findByEmail(String email);

    List<User> findByEmailAndActive(String email, boolean active);


    /* Workaround bug that Spring Data Elasticsearch has when returning non-list */
    default Optional<User> findByEmail_(String email) {
        return findByEmail(email).stream().findFirst();
    }

    default Optional<User> findByEmailAndActive_(String email, boolean active) {
        return findByEmailAndActive(email, active).stream().findFirst();
    }
}
