package service;

import models.User;
import play.Application;

import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import com.feth.play.module.pa.service.UserServicePlugin;

public class UserAuthenticationPlugin extends UserServicePlugin {

    public UserAuthenticationPlugin(final Application app) {
        super(app);
    }

    @Override
    public String save(final AuthUser authUser) {
        final boolean isLinked = User.existsByAuthUserIdentity(authUser);
        if (!isLinked) {
            return User.create(authUser).getId().toString();
        } else {
            // User already registered
            return null;
        }
    }

    @Override
    public String getLocalIdentity(final AuthUserIdentity identity) {
        // TODO Caching. Sync the cache when users get deactivated/deleted
        final User u = User.findByAuthUserIdentity(identity);
        if(u != null) {
            return u.getId().toString();
        } else {
            return null;
        }
    }

    @Override
    public AuthUser merge(final AuthUser newUser, final AuthUser oldUser) {
        if (!oldUser.equals(newUser)) {
            User.merge(oldUser, newUser);
        }
        return oldUser;
    }

    @Override
    public AuthUser link(final AuthUser oldUser, final AuthUser newUser) {
        User.addLinkedAccount(oldUser, newUser);
        return null;
    }

}