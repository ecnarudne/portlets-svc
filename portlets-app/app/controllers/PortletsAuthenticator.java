package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticator;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;

public class PortletsAuthenticator extends Authenticator {
	@Override
	public String getUsername(Context context) {
		AuthUser user = PlayAuthenticate.getUser(context);
		return user == null ? null : user.getId();
	}

	@Override
	public Result onUnauthorized(Context context) {
		return unauthorized();
	}
}
