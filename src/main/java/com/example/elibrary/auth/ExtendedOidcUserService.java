package com.example.elibrary.auth;

import com.example.elibrary.dao.RoleRepo;
import com.example.elibrary.dao.UserRepo;
import com.example.elibrary.dao.entity.Role;
import com.example.elibrary.dao.entity.User;
import com.example.elibrary.help.ERole;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ExtendedOidcUserService extends OidcUserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        updateUser(oidcUser);
        return oidcUser;
    }

    private void updateUser(OidcUser oidcUser) {

        Map googleUserInfo = oidcUser.getAttributes();
        Optional<User> optionalUser = userRepo.findByUsername((String)googleUserInfo.get("name"));
        User user;
        if(optionalUser.isEmpty())
            user = new User();
        else
            user = optionalUser.get();

        Set<Role> roles = new HashSet<Role>();
        Role userRole = roleRepo.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setUsername((String)googleUserInfo.get("name"));
        user.setRoles(roles);
//        user.setGoogleUser(true);
        userRepo.save(user);
    }
}
