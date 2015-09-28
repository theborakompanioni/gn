package com.github.theborakompanioni.gn;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import gn.elastic.repository.PermissionElasticRepository;
import gn.elastic.repository.RoleElasticRepository;
import gn.elastic.repository.UserElasticRepository;
import model.Permission;
import model.Permissions;
import model.Role;
import model.User;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by void on 22.08.15.
 */
@Configuration
public class InitializeUserRepositoryModule {
    private static final Logger log = LoggerFactory.
            getLogger(InitializeUserRepositoryModule.class);

    @Autowired
    private DefaultPasswordService passwordService;

    @Autowired
    UserElasticRepository userRepo;

    @Autowired
    RoleElasticRepository roleRepo;

    @Autowired
    PermissionElasticRepository permissionRepo;

    @Bean
    public InitializingBean initializeUserRepository() {
        return () -> {
            log.info("Initializing user scenario..");

            // clean-up users, roles and permissions
            userRepo.delete(userRepo.findAll()); // faster than deleteAll()
            roleRepo.delete(roleRepo.findAll()); // faster than deleteAll()
            permissionRepo.delete(permissionRepo.findAll()); // faster than deleteAll()

            // define permissions
            final Permission p1 = new Permission();
            p1.setName(Permissions.ARTICLE_DELETE.getName());
            final Permission p2 = new Permission();
            p2.setName(Permissions.ARTICLE_VERIFY.getName());

            List<Permission> permissions = Arrays.asList(p1, p2);
            permissions.forEach(permissionRepo::save);

            // define roles
            final Role roleAdmin = new Role();
            roleAdmin.setName("administrator");
            roleAdmin.setPermissions(permissions);

            final Role roleUser = new Role();
            roleUser.setName("user");

            Arrays.asList(roleAdmin, roleUser).forEach(roleRepo::save);

            // define users
            final User user = new User();
            user.setActive(true);
            user.setCreated(System.currentTimeMillis());
            user.setEmail("user@example.com");
            user.setName("John Doe");
            user.setPassword(passwordService.encryptPassword("password"));
            user.setRoles(Collections.singletonList(roleUser));

            final User admin = new User();
            admin.setActive(true);
            admin.setCreated(System.currentTimeMillis());
            admin.setEmail("admin@example.com");
            admin.setName("Mr. Admin");
            admin.setPassword(passwordService.encryptPassword("admin"));
            admin.setRoles(Collections.singletonList(roleAdmin));

            Arrays.asList(admin, user).forEach(userRepo::save);

            log.info("User scenario initiated.");

            sanityCheck();
        };
    }


    private void sanityCheck() throws RuntimeException {
        final Iterable<User> users = userRepo.findAll();
        Preconditions.checkArgument(Iterables.size(users) == 2);

        final Iterable<Role> roles = roleRepo.findAll();
        Preconditions.checkArgument(Iterables.size(roles) == 2);

        final Iterable<Permission> permissions = permissionRepo.findAll();
        Preconditions.checkArgument(Iterables.size(permissions) == 2);
    }

}
