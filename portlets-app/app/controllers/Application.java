package controllers;

import java.util.Date;
import java.util.List;

import models.Portlet;
import models.PortletStock;
import models.Stock;
import models.User;
import models.UserPortletStock;
import models.UserValidityState;

import org.json.JSONException;
import org.json.JSONObject;

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
import service.CorsComposition;
import views.html.index;
import views.html.mystocks;
import views.html.portfolio;
import views.html.portlets;
import views.html.stocksinportlet;
import views.html.users;

import com.fasterxml.jackson.databind.JsonNode;
import com.feth.play.module.pa.PlayAuthenticate;

@CorsComposition.Cors
public class Application extends Controller {

    public static final String FLASH_ERROR_KEY = "FLASH_ERROR";
    public static final String FLASH_SUCCESS_KEY = "FLASH_SUCCESS";
    
    public static Result getPortfoliodetail(){
    	System.out.println("get portfolio deat method is called");
    	JSONObject obj = new JSONObject();

        try {
        	obj.put("portletCreated", new Integer(38));
			obj.put("follower", new Integer(105));
			obj.put("following", new Integer(380));
	        obj.put("followers", new Integer(100));
	        obj.put("portfolioValue", new Double(2.49));
	        obj.put("dailyReturn", new Double(0.56));
	        obj.put("yearlyReturn", new Double(2.53));
		
        } catch (JSONException e) {
			
			e.printStackTrace();
		}
        
        
    	return ok(obj.toString());
    }
    public static Result preflight(String path) {
		return ok("");
	}
    
	public static Result index() {
		printSession();
        return ok(index.render());
    }

    public static Result users() {
        return ok(users.render(getLocalUser(session())));
    }
    
    public static Result login() {    	
    	JsonNode json = request().body().asJson();
    	System.out.println("Json data got is "+ json);
    	
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

    public static Result portlets() {
        return ok(portlets.render(getLocalUser(session())));
    }
    
    public static Result listPortlets() {
    	List<Portlet> list = Portlet.find.all();
    	return ok(Json.toJson(list));
    }

    public static Result addStockToPortlet() {
    	PortletStock newPortletStock = Form.form(PortletStock.class).bindFromRequest().get();
    	newPortletStock.setLastUpdatedOn(new Date());
    	newPortletStock.save();
    	return redirect(routes.Application.stocksInPortlet(newPortletStock.getPortlet().getId()));
    }

    public static Result listPortletStocks() {
    	List<PortletStock> list = PortletStock.find.all();
    	return ok(Json.toJson(list));
    }

    public static Result listMyStocks() {
    	List<UserPortletStock> list = UserPortletStock.findByUser(getLocalUser(session()));
    	return ok(Json.toJson(list));
    }

    public static Result myStocks() {
        return ok(mystocks.render(getLocalUser(session())));
    }

    public static Result subscribeToPortlet() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String portletId = requestData.get("portlet");
    	Logger.debug("portlet ID: " + portletId);
        Portlet portlet = Portlet.find.byId(Long.parseLong(portletId));
    	double amount;
		try {
			amount = Double.parseDouble(requestData.get("amount").trim());
		} catch (Exception e) {
			flash("Invalid Amount: " + requestData.get("amount"));
			Logger.error("Invalid Amount: " + requestData.get("amount"));
	    	return redirect(routes.Application.myStocks());
		}
    	List<PortletStock> stocks = PortletStock.findByPortlet(portlet);

    	if(stocks == null || stocks.isEmpty()) {
			flash("No Stocks found for Portlet: " + portletId);
			Logger.error("No Stocks found for Portlet: " + portletId);
	    	return redirect(routes.Application.myStocks());
    	}
    	for (PortletStock stock : stocks) {
        	UserPortletStock newUserPortletStock = new UserPortletStock();
    		newUserPortletStock.setPortlet(portlet);
	    	newUserPortletStock.setStock(stock.getStock());
    		double buyPrice = Stock.currentPrice(stock.getStock());
    		newUserPortletStock.setBuyPrice(buyPrice);
	    	newUserPortletStock.setQty(Math.round((amount*stock.getPercent())/buyPrice)/100);
	    	newUserPortletStock.setUser(getLocalUser(session()));
	    	newUserPortletStock.setBuyEpoch(System.currentTimeMillis());
	    	Logger.info("Saving: " + newUserPortletStock);
	    	newUserPortletStock.save();//TODO Transaction
    	}
    	return redirect(routes.Application.myStocks());
    }

    public static Result stocksInPortlet(Long portletId) {
        return ok(stocksinportlet.render(getLocalUser(session()), portletId));
    }

    public static Result addPortlet() {
    	/*Portlet newPortlet = Form.form(Portlet.class).bindFromRequest().get();
    	newPortlet.setCreatedOn(new Date());
    	
    	final User localUser = getLocalUser(session());
    	if(localUser != null) {
			newPortlet.setOwner(localUser);
			newPortlet.setValidity(PortletValidityState.PENDING);
	    	newPortlet.save();
    	} else {
            flash(FLASH_ERROR_KEY, "Please login first");
    		Logger.error("Please login first");
    	}*/
    	JsonNode json = request().body().asJson();
    	System.out.println("Json data got is "+ json);
    	return ok("Hello");
    	//return redirect(routes.Application.portlets());
    }

    public static Result portfolio() {
        return ok(portfolio.render(getLocalUser(session())));
    }

    public static Result listMyPortlets() {
    	List<UserPortletStock> list = UserPortletStock.findByUser(getLocalUser(session()));
    	return ok(Json.toJson(list));
    }

    public static Result buyPortlet() {
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
