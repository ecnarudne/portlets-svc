package controllers;

import java.util.Date;
import java.util.List;

import models.Portlet;
import models.PortletStats;
import models.PortletStock;
import models.PortletValidityState;
import models.User;
import models.UserValidityState;
import models.mock.MockSet;
import models.mock.MockSets;

import org.joda.time.LocalDate;

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
import views.html.index;
import views.html.users;
import views.html.admin.importpage;

import com.feth.play.module.pa.PlayAuthenticate;

import data.CsvMarketDataLoader;

public class Admin extends Controller {

    public static final String FLASH_ERROR_KEY = "ADMIN_ERROR";
    public static final String FLASH_SUCCESS_KEY = "ADMIN_SUCCESS";

	public static Result index() {
		//printSession();
        return ok(index.render());
    }

    public static Result users() {
        return ok(users.render(getLocalUser(session())));
    }

    public static Result addUser() {
    	User newUser = Form.form(User.class).bindFromRequest().get();
    	newUser.setEmailVerified(false);
    	newUser.setValidity(UserValidityState.PENDING);
    	newUser.save();
    	return redirect(routes.Application.users());
    }
    
    public static Result listUsers() {
    	List<User> list = User.find.all();
    	return ok(Json.toJson(list));
    }

    public static Result importDataPage() {
        return ok(importpage.render(getLocalUser(session())));
    }

    public static Result importMarketDataFile() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String filepath = requestData.get("filepath");
        String exchange = requestData.get("exchange");
        String date = requestData.get("date");
    	Logger.debug("filepath: " + filepath);
    	CsvMarketDataLoader loader = new CsvMarketDataLoader();
    	if(filepath != null && filepath.trim().isEmpty())
    		filepath = CsvMarketDataLoader.MKT_FILE_PATH_DEFAULT;
    	loader.loadMarketDataFile(filepath, exchange, date);
    	return redirect(routes.Admin.importDataPage());
    }
    
    public static Result importMarketDataHistory() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String dirpath = requestData.get("dirpath");
    	Logger.debug("dirpath: " + dirpath);
    	CsvMarketDataLoader loader = new CsvMarketDataLoader();
    	if(dirpath != null && dirpath.trim().isEmpty())
    		dirpath = CsvMarketDataLoader.MKT_DIR_PATH_DEFAULT;
    	loader.loadMarketDataHistory(dirpath);
    	return redirect(routes.Admin.importDataPage());
    }

    public static Result addSectors() {
    	(new MockSets()).persistSectors();
    	Logger.debug("Persisted sectors");
    	return redirect(routes.Admin.importDataPage());
    }
    
    public static Result overwritePortletStats() {
        try {
			DynamicForm requestData = Form.form().bindFromRequest();
			Long portletId = Long.parseLong(requestData.get("portletid"));
			Portlet portlet = Portlet.find.byId(portletId);
			LocalDate startDate = new LocalDate(requestData.get("start"));//yyyy-MM-dd
			LocalDate endDate = new LocalDate(requestData.get("end"));//yyyy-MM-dd
			PortletStats.overwriteDuring(portlet , startDate, endDate);
			return ok();
		} catch (Exception e) {
			Logger.error("Failed to overwrite PortletStats", e);
			return badRequest("Failed to overwrite PortletStats. " + e.getMessage());
		}
    }

    public static Result addMockSet() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String nick = requestData.get("nick");
    	Logger.debug("nick: " + nick);
    	MockSet mockSet = new MockSets().persist(nick, getLocalUser(session()));
    	Logger.debug("Persisted mockSet: " + mockSet);
    	return redirect(routes.Admin.importDataPage());
    }

    public static Result listPortletStocks() {
    	List<PortletStock> list = PortletStock.find.all();
    	return ok(Json.toJson(list));
    }

    public static Result addPortlet() {
    	Portlet newPortlet = Form.form(Portlet.class).bindFromRequest().get();
    	newPortlet.setCreatedOn(new Date());
    	
    	final User localUser = getLocalUser(session());
    	if(localUser != null) {
			newPortlet.setOwner(localUser);
			newPortlet.setValidity(PortletValidityState.PENDING);
	    	newPortlet.save();
    	} else {
            flash(FLASH_ERROR_KEY, "Please login first");
    		Logger.error("Please login first");
    	}
    	return redirect(routes.Application.portlets());
    }

    public static User getLocalUser(final Session session) {
        final User localUser = User.findByAuthUserIdentity(PlayAuthenticate.getUser(session));
        return localUser;
    }

    public static Result oAuthDenied(final String providerKey) {
        flash(FLASH_ERROR_KEY, "You need to accept the OAuth connection in order to use this website!");
        return redirect(routes.Application.index());
    }

	protected static void printSession() {
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
