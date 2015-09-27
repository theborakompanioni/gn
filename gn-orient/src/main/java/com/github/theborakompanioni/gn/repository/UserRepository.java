package com.github.theborakompanioni.gn.repository;

import model.User;
import org.springframework.data.orient.object.repository.OrientObjectRepository;

/**
 * DAO for {@link User}.
 */
public interface UserRepository extends OrientObjectRepository<User> {

    User findByEmail(String email);

    User findByEmailAndActive(String email, boolean active);

}
