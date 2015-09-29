package com.github.theborakompanioni.gn.shiro;

import gn.elastic.repository.UserElasticRepository;
import model.Permission;
import model.Role;
import model.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Shiro authentication & authorization realm that relies on
 * ElasticSearch as datastore.
 */
public class ElasticsearchRealm extends AuthorizingRealm {

    private UserElasticRepository userRepository;

    public ElasticsearchRealm(UserElasticRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            final AuthenticationToken token)
            throws AuthenticationException {
        final UsernamePasswordToken credentials = (UsernamePasswordToken) token;
        final String email = credentials.getUsername();
        if (email == null) {
            throw new UnknownAccountException("Email not provided");
        }
        final User user = userRepository.findByEmailAndActive_(email, true)
                .orElseThrow(() -> new UnknownAccountException("Account does not exist"));

        return new SimpleAuthenticationInfo(email, user.getPassword().toCharArray(),
                ByteSource.Util.bytes(email), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            final PrincipalCollection principals) {
        // retrieve role names and permission names
        final String email = (String) principals.getPrimaryPrincipal();
        final User user = userRepository.findByEmailAndActive_(email, true)
                .orElseThrow(() -> new UnknownAccountException("Account does not exist"));

        final Set<String> roleNames = getRoleNames(user);
        final Set<String> permissionNames = getPermissionNames(user);

        final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissionNames);
        return info;
    }


    private Set<String> getRoleNames(User user) {
        final Set<String> roleNames = new HashSet<>();

        for (Role role : user.getRoles()) {
            roleNames.add(role.getName());
        }

        return roleNames;
    }

    /*
    Following code still throws an NPE.. investigate later.
    final Set<String> permissionNames = user.getRoles().stream()
            .map(Role::getPermissions)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .map(Permission::getName)
            .filter(Objects::nonNull)
            .collect(toSet());*/
    private Set<String> getPermissionNames(User user) {
        final List<Role> roles = user.getRoles();
        final Set<String> permissionNames = new HashSet<>();

        for (Role role : roles) {
            for (Permission permission : role.getPermissions()) {
                permissionNames.add(permission.getName());
            }
        }

        return permissionNames;
    }
}
