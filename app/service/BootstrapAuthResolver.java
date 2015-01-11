package service;
import play.mvc.Call;

import com.feth.play.module.pa.PlayAuthenticate.Resolver;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;

import controllers.routes;

public class BootstrapAuthResolver extends Resolver {

    @Override
    public Call login() {
        // login page
        return routes.Application.index();
    }

    @Override
    public Call afterAuth() {
        // The user will be redirected to index page after authentication
        // if no original URL was saved
        return routes.Application.index();
    }

    @Override
    public Call afterLogout() {
        return routes.Application.index();
    }

    @Override
    public Call auth(final String provider) {
        return com.feth.play.module.pa.controllers.routes.Authenticate.authenticate(provider);
    }

    @Override
    public Call onException(final AuthException e) {
        if (e instanceof AccessDeniedException) {
            return routes.Application
                    .oAuthDenied(((AccessDeniedException) e).getProviderKey());
        }

        //TODO handle auth denied

        return super.onException(e);
    }

    @Override
    public Call askLink() {
        // TODO handle link
        return null;
    }

    @Override
    public Call askMerge() {
        // TODO handle merge
        return null;
    }


}
