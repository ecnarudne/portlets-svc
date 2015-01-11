package controllers;

import java.util.List;

import com.feth.play.module.pa.PlayAuthenticate;

import models.Portlet;
import models.User;
import models.UserPortletStock;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import play.mvc.Http.Cookies;
import play.mvc.Http.Request;
import play.mvc.Http.Session;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {

    private static final String FLASH_ERROR_KEY = "FLASH_ERROR_KEY";

	public static Result index() {
		printSession();
        return ok(index.render(getLocalUser(session())));
    }

    public static Result users() {
        return ok(users.render(getLocalUser(session())));
    }

    public static Result addUser() {
    	User newUser = Form.form(User.class).bindFromRequest().get();
    	newUser.save();
    	return redirect(routes.Application.users());
    }
    
    public static Result listUsers() {
    	List<User> list = User.find.all();
    	return ok(Json.toJson(list));
    }

    public static Result portlets() {
        return ok(portlets.render(getLocalUser(session())));
    }
    
    public static Result allportlets() {
    	List<Portlet> list = Portlet.find.all();
    	return ok(Json.toJson(list));
    }

    public static Result addPortlet() {
    	Portlet newPortlet = Form.form(Portlet.class).bindFromRequest().get();
    	newPortlet.save();
    	return redirect(routes.Application.portlets());
    }

    public static Result portfolio() {
        return ok(portfolio.render(getLocalUser(session())));
    }
    
    public static Result myportlets() {
    	List<UserPortletStock> list = UserPortletStock.find.all();
    	return ok(Json.toJson(list));
    }

    public static Result buyPortlet() {
    	//UserPortletStock newUserPortletStock = Form.form(UserPortletStock.class).bindFromRequest().get();
        DynamicForm requestData = Form.form().bindFromRequest();
    	UserPortletStock newUserPortletStock = new UserPortletStock();
        String portletName = requestData.get("portlet");
    	Logger.debug("portletName: " + portletName);
        Portlet portlet = Portlet.findByName(portletName);
    	newUserPortletStock.setPortlet(portlet);
    	String buyPrice = requestData.get("buyPrice");
    	if(buyPrice != null && !buyPrice.trim().isEmpty())
    		newUserPortletStock.setBuyPrice(Long.parseLong(buyPrice));
    	String qty = requestData.get("qty");
    	if(qty != null && !qty.trim().isEmpty())
    		newUserPortletStock.setQty(Double.parseDouble(qty));
    	newUserPortletStock.setStock(requestData.get("stock"));
    	newUserPortletStock.setUser(getLocalUser(session()));
    	newUserPortletStock.setBuyEpoch(System.currentTimeMillis());
    	Logger.info("Saving: " + newUserPortletStock);
    	newUserPortletStock.save();
    	return redirect(routes.Application.portfolio());
    }

    public static User getLocalUser(final Session session) {
        final User localUser = User.findByAuthUserIdentity(PlayAuthenticate.getUser(session));
        return localUser;
    }

    public static Result oAuthDenied(final String providerKey) {
        flash(FLASH_ERROR_KEY, "You need to accept the OAuth connection in order to use this website!");
        return redirect(routes.Application.index());
    }

	private static void printSession() {
		Session s = session();
		Request r = request();
		Cookies c = request().cookies();
    	Logger.debug("deb session: " + s);
    	Logger.debug("deb request: " + r);
    	Logger.debug("deb Cookies: " + c);
    	for (Cookie cookie : c) {
    		Logger.debug("cookie Name: " + cookie.name() + " -cookie Value: " + cookie.value());
		}
    	for (String k : s.keySet()) {
    		Logger.debug("session key: " + k + " -session Value: " + s.get(k));
		}
    	Logger.debug("deb session: " + s.keySet());
	}
}
