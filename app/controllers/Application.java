package controllers;

import java.util.List;

import models.User;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.users;

public class Application extends Controller {

    private static final String FLASH_ERROR_KEY = "FLASH_ERROR_KEY";

	public static Result index() {
        return ok(index.render("MyPortlets"));
    }

    public static Result users() {
        return ok(users.render("MyPortlets - Users Module"));
    }

    public static Result addUser() {
    	User newUser = Form.form(User.class).bindFromRequest().get();
    	newUser.save();
    	return redirect(routes.Application.users());
    }
    
    public static Result listUsers() {
    	List<User> users = User.find.all();
    	return ok(Json.toJson(users));
    }

    public static Result oAuthDenied(final String providerKey) {
        flash(FLASH_ERROR_KEY, "You need to accept the OAuth connection in order to use this website!");
        return redirect(routes.Application.index());
    }
}
