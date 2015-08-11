package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Exchange;
import models.Portfolio;
import models.Portlet;
import models.PortletStock;
import models.PortletValidityState;
import models.PriceImportHistory;
import models.Sector;
import models.Stock;
import models.StockStats;
import models.User;
import models.UserPortletStock;
import models.UserToken;
import models.UserValidityState;
import models.api.PortletAPI;
import models.api.PortletStockAPI;
import models.api.UserPortletStockAPI;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import play.mvc.Http.Cookies;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Results;
import secure.Authentication;
import stats.Calculations;
import views.html.index;
import views.html.mystocks;
import views.html.portfolio;
import views.html.portlets;
import views.html.sectors;
import views.html.stocksinportlet;
import views.html.users;

import com.fasterxml.jackson.databind.JsonNode;
import com.feth.play.module.pa.PlayAuthenticate;

import data.CsvMarketDataLoader;

public class Application extends Controller {

    public static final String FLASH_ERROR_KEY = "FLASH_ERROR";
    public static final String FLASH_SUCCESS_KEY = "FLASH_SUCCESS";
	private static final int LIST_DEFAULT_LIMIT = 10;
	private static final int LIST_MAX_LIMIT = 100;

	public static Result portfolioPriceHistory(){
    	User currentUser = getLocalUser(session());
    	if(currentUser == null)
    		forbidden("Login Required");
		List<UserPortletStock> ups = UserPortletStock.findByUser(currentUser);
		try {
			Number[][] data = getPriceHistoryAsChartArray(ups, LocalDate.now().minusYears(1));//TODO get time as param
			return ok(Json.toJson(data));
		} catch (Exception e) {
			String error = "Portfolio price information unavailable due to some issues. ";
			Logger.error(error, e);
			return Results.internalServerError(error);
		}
	}

	private static Number[][] getPriceHistoryAsChartArray(List<UserPortletStock> ups, LocalDate afterDate) {
		//LocalDate afterDate = LocalDate.now().withFieldAdded(DurationFieldType.days(), days*-1);
		List<PriceImportHistory> dates = PriceImportHistory.findDatesAfter(Exchange.NASDAQ, afterDate);//TODO remove exchange hardcode
		Number[][] data = new Number[dates.size()][2];
		int i = 0;
		for (PriceImportHistory date : dates) {
			double portfolioValue = Calculations.calcPortfolioValue(ups, date.getLocalDate());
			data[i][0] = date.getLocalDate().toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis();
			data[i][1] = portfolioValue;
			i++;
		}
		return data;
	}

	public static Result myPortfolio() {
    	User currentUser = getLocalUser(session());
    	if(currentUser == null)
    		return forbidden("Login Required");
    	Portfolio portfolio = new Portfolio();
    	List<Portlet> created = Portlet.findByOwner(currentUser);
    	int portletCreatedCount = 0;
    	if(created != null)
    		portletCreatedCount = created.size();
		currentUser.setPortletCreatedCount(portletCreatedCount);
		portfolio.setOwner(currentUser);
		//portfolio.setPortletCount(portletCreatedCount);//fetch subcribed portlet count
		try {
			List<UserPortletStock> ups = UserPortletStock.findByUser(currentUser);
			Set<Long> stockIdSet = new HashSet<Long>();
			for (UserPortletStock u : ups) {
				Long id = Stock.findBySymbol(u.getStock()).getId();//TODO store Stock object in UserPortletStock
				stockIdSet.add(id);
			}
			double portfolioValueLast = Calculations.calcPortfolioValue(ups, LocalDate.now());
			portfolio.setPortfolioValue(portfolioValueLast);
			double portfolioValueDayBefore = Calculations.calcPortfolioValue(ups, LocalDate.now().minusDays(1));
			portfolio.setDailyReturn(Calculations.calcReturnFromPrice(portfolioValueLast, portfolioValueDayBefore));
			double portfolioValueYearBefore = Calculations.calcPortfolioValue(ups, LocalDate.now().minusYears(1));
			portfolio.setAnnualReturn(Calculations.calcReturnFromPrice(portfolioValueLast, portfolioValueYearBefore));
		} catch (Exception e) {
			Logger.error("Portfolio information unavailable due to some issues. ", e);
		}
    	return ok(Json.toJson(portfolio));
    }

	public static Result stockPriceHistory(String symbol) {
		Stock stock = Stock.findBySymbol(symbol);
		if(stock == null)
			return Results.notFound("Symbol not found");
		try {
			List<StockStats> stats = StockStats.findByStock(stock);
			Number[][] data = new Number[stats.size()][2];
			int i = 0;
			for (StockStats s : stats) {
				data[i][0] = s.getLocalDate().toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis();
				data[i][1] = s.getClosePrice();
				i++;
			}
			return ok(Json.toJson(data));
		} catch (Exception e) {
			String error = "Stock Price information unavailable due to some issues. ";
			Logger.error(error, e);
			return Results.internalServerError(error);
		}
	}

	public static Result searchSectorsJson() {
        JsonNode json = request().body().asJson();
        if(json != null) {
        	String partName = json.findPath("partname").asText();
        	if(partName == null)
        		return Results.badRequest("Missing JSON field partname");
    		return searchSectors(partName);
        }
        return Results.badRequest("Bad JSON input");
	}

	public static Result searchPortletsJson() {
        JsonNode json = request().body().asJson();
        if(json != null) {
        	String partName = json.findPath("partname").asText();
        	if(partName == null)
        		return Results.badRequest("Missing JSON field partname");
    		return searchPortlets(partName);
        }
        return Results.badRequest("Bad JSON input");
	}

	public static Result searchSectors(String partName) {
		if(partName == null || partName.length() < 2)
			return Results.notFound(partName);
		Collection<Sector> sectors = Portlet.findSectorsByPartName(partName);
		return ok(Json.toJson(sectors));
	}

	public static Result searchPortlets(String partName) {
		if(partName == null || partName.length() < 2)
			return Results.notFound(partName);
		Collection<Portlet> portlets = Portlet.findByPartName(partName);
		return ok(Json.toJson(portlets));
	}

	public static Result index() {
		printSession();
        //return ok(index.render(getLocalUser(session())));
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

	public static Result authByToken() {
        String[] headers = request().headers().get("token");
        if(headers != null && headers.length > 0) {
        	try {
				String token = headers[0];
				Authentication.registerAuthToken(token);
				return ok();
			} catch (Exception e) {//TODO Narrow down
				return forbidden("Token not valid");
			}
        } else {
        	return forbidden("No Token in Header");
        }
    }

	public static Result validateToken() {
        String[] headers = request().headers().get("token");
        if(headers != null && headers.length > 0) {
        	try {
				String token = headers[0];
				UserToken userToken = UserToken.findByToken(token);
				if(userToken != null) {
					return ok("Valid token found");
				} else {
					return forbidden("Token not found");
				}
			} catch (Exception e) {
				return forbidden("Token not valid");
			}
        } else {
        	return forbidden("No Token in Header");
        }
    }

    public static Result registerAuthToken(String token) {
    	if(Logger.isDebugEnabled()) Logger.debug("token: " + token);
    	try {
			Authentication.registerAuthToken(token);
			return ok();
		} catch (Exception e) {
			Logger.error("Error in validating token", e);
			return forbidden("Token not valid");
		}
    }
    
    public static Result listUsers() {
    	List<User> list = User.find.all();
    	return ok(Json.toJson(list));
    }
    
    public static Result sectors() {
        return ok(sectors.render(getLocalUser(session())));
    }
    
    
    public static Result listSectors() {
    	List<Sector> list = Sector.find.all();
    	return ok(Json.toJson(list));
    }

    public static Result addSector() {
    	Sector newSector = Form.form(Sector.class).bindFromRequest().get();
    	if(!isLoggedIn(session()))
			return forbidden();
		newSector.setCreatedOn(new Date());
    	newSector.save();
    	return redirect(routes.Application.sectors());
    }

    public static Result definePortletJson() {
    	if(!isLoggedIn(session()))
			return forbidden();
        JsonNode root = request().body().asJson();
        if(root != null) {
        	Portlet newPortlet = Portlet.fromJson(root);
        	newPortlet.setCreatedOn(new Date());
	    	newPortlet.save();
	    	return ok();
        } else {
            return badRequest("Expecting Json data");
        }
    }
    public static Result subscribeToPortletJson(Long portletId) {
    	if(!isLoggedIn(session()))
			return forbidden();
        JsonNode json = request().body().asJson();
        if(json != null) {
            double amount = json.findPath("amount").asDouble();
            if(amount != 0) {
            	Portlet portlet = Portlet.find.byId(portletId);
            	List<PortletStock> stocks = PortletStock.findByPortlet(portlet);
            	double remainingAmount = amount;
            	for (PortletStock ps : stocks) {
            		double currentPx = Stock.currentPrice(ps.getStock());

            		//long shares = Math.round((amount * ps.getWeightage())/(100 * currentPx));
            		double shares = (amount * ps.getWeightage())/(100 * currentPx);
            		double spent = shares * currentPx;
/*        			if(shares > 0 && remainingAmount < spent) {
        				shares -= 1;
        				spent = shares * currentPx;
        			}
*/
            		if(shares > 0) {
        	        	UserPortletStock newUserPortletStock = new UserPortletStock();
        	    		newUserPortletStock.setPortlet(portlet);
        		    	newUserPortletStock.setStock(ps.getStock());
        		    	newUserPortletStock.setBuyPrice(currentPx);
        		    	newUserPortletStock.setBuyWeight(ps.getWeightage());
        		    	newUserPortletStock.setQty(shares);
        		    	newUserPortletStock.setUser(getLocalUser(session()));
        		    	newUserPortletStock.save();
                		remainingAmount -= spent;
            		}
				}
            	Logger.info("Remaining Amount: " + remainingAmount);
            	return ok();
            } else {
                return badRequest("Please provide amount");
            }
        } else {
            return badRequest("Expecting Json data");
        }
    }

	public static Result addSectorJson() {
    	if(!isLoggedIn(session()))
			return forbidden();
        JsonNode json = request().body().asJson();
        if(json != null) {
            String name = json.findPath("name").textValue();
            if(name != null) {
            	Sector newSector = new Sector();
    			newSector.setName(name);
    			newSector.setCreatedOn(new Date());
    	    	newSector.save();
            	return ok();
            } else {
                return badRequest("Missing parameter [name]");
            }
        } else {
            return badRequest("Expecting Json data");
        }
    }

	public static Result setMyProfileJson() {//TODO implement
    	if(!isLoggedIn(session()))
			return forbidden();
        JsonNode json = request().body().asJson();
        if(json != null) {
            return ok();
        } else {
            return badRequest("Expecting Json data");
        }
    }

    public static Result listExchanges() {
    	List<Exchange> list = new ArrayList<Exchange>(Arrays.asList(new Exchange(Exchange.NASDAQ)));
    	return ok(Json.toJson(list));
    }

    public static Result findByExchange(String exchange) {
    	List<Stock> list = Stock.findByExchange(exchange);
    	return ok(Json.toJson(list));
    }
    
    public static Result stockStats(String symbol) {
    	StockStats stats = CsvMarketDataLoader.loadStockStatsBySymbol(symbol);
    	return ok(Json.toJson(stats));
    }
    
    
    public static Result listMyStockStatsByPortlet(Long portletId) {
    	List<UserPortletStock> stocks = UserPortletStock.findByUserAndPortlet(getLocalUser(session()), portletId);
    	Logger.debug("stocks.size(): " + stocks.size());
    	ArrayList<UserPortletStockAPI> list = new ArrayList<UserPortletStockAPI>(stocks.size());
    	for (UserPortletStock ups : stocks) {
    		StockStats stats = CsvMarketDataLoader.loadStockStatsBySymbol(ups.getStock());
    		UserPortletStockAPI api = new UserPortletStockAPI(ups, stats);
			list.add(api );
		}
    	return ok(Json.toJson(list));
    }

    public static Result portlets() {
        return ok(portlets.render(getLocalUser(session())));
    }
    
    public static Result listPortlets() {
    	List<Portlet> portlets = Portlet.find.all();
		if(portlets != null) {
			List<PortletAPI> list = new ArrayList<PortletAPI>(portlets.size());
			for (Portlet p : portlets) {
				list.add(new PortletAPI(p));
			}
	    	return ok(Json.toJson(list));
		} else {
			return notFound("No Portlets Found");
		}
    }

    public static Result listRecentPortlets(Integer limit) {
    	if(limit == null || limit == 0)
    		limit = LIST_DEFAULT_LIMIT;
    	else if (limit > LIST_MAX_LIMIT)
    		limit = LIST_MAX_LIMIT;
    	List<Portlet> list = Portlet.findRecent(limit);
    	return ok(Json.toJson(list));
    }
    
    public static Result listPortletsBySector(Long sectorId) {
    	List<Portlet> list = Portlet.findBySector(sectorId);
    	return ok(Json.toJson(list));
    }

    public static Result listTopPerformingPortlets(Integer limit) {
    	//TODO track performance and return portlets by rating
    	if(limit == null || limit == 0)
    		limit = LIST_DEFAULT_LIMIT;
    	else if (limit > LIST_MAX_LIMIT)
    		limit = LIST_MAX_LIMIT;
    	List<Portlet> list = Portlet.findRecent(limit);
    	return ok(Json.toJson(list));
    }

    public static Result addStockToPortlet() {
    	PortletStock newPortletStock = Form.form(PortletStock.class).bindFromRequest().get();
    	newPortletStock.setLastUpdatedOn(new Date());
    	newPortletStock.save();
    	return redirect(routes.Application.stocksInPortlet(newPortletStock.getPortlet().getId()));
    }

    public static Result stock(Long stockId) {
    	Stock stock = Stock.find.byId(stockId);
    	return ok(Json.toJson(stock));
    }

    public static Result portlet(Long portletId) {
    	Portlet portlet = Portlet.find.byId(portletId);
    	return ok(Json.toJson(new PortletAPI(portlet)));
    }

    public static Result listPortletStocks(Long portletId) {
    	List<PortletStock> list = PortletStock.findByPortletId(portletId);
    	return ok(Json.toJson(list));
    }

    public static Result listPortletStockStats(Long portletId) {
    	List<PortletStock> stocks = PortletStock.findByPortletId(portletId);
    	ArrayList<PortletStockAPI> list = new ArrayList<PortletStockAPI>(stocks.size());
    	for (PortletStock portletStock : stocks) {
    		PortletStockAPI api = new PortletStockAPI(portletStock);
			list.add(api);
		}
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
	    	newUserPortletStock.setQty(Math.round((amount*stock.getWeightage())/buyPrice)/100);
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
    	Portlet newPortlet = Form.form(Portlet.class).bindFromRequest().get();
    	newPortlet.setCreatedOn(new Date());
    	
    	final User localUser = getLocalUser(session());
    	if(localUser != null) {
        	try {
        		//TODO start transaction
				newPortlet.setOwner(localUser);
				newPortlet.setValidity(PortletValidityState.PENDING);
		    	newPortlet.save();
		        final JsonNode json = request().body().asJson();
		        if(json != null) {
		        	JsonNode stockArray = json.findPath("stocks");
		        	if(stockArray == null)
		        		return Results.badRequest("Missing JSON field stocks");
/*					ObjectMapper mapper = new ObjectMapper();
					TypeReference<List<PortletStock>> typeRef = new TypeReference<List<PortletStock>>(){};
					List<PortletStock> list = mapper.readValue(stockArray.traverse(), typeRef);*/
		        	for (JsonNode jsonNode : stockArray) {
		        		PortletStock portletStock = new PortletStock();
		        		portletStock.setStock(jsonNode.findPath("stock").asText());
		        		portletStock.setWeightage(jsonNode.findPath("weightage").asDouble());
						portletStock.setLastUpdatedOn(new Date());
						portletStock.setPortlet(newPortlet);
						portletStock.save();
					}
		        }
			} catch (Exception e) {
				//TODO revert transaction
				e.printStackTrace();
			}
		    	
    	} else {
            flash(FLASH_ERROR_KEY, "Please login first");
    		Logger.error("Please login first");
    	}
    	return ok(Json.toJson(newPortlet));
    }

    public static Result portfolio() {
        return ok(portfolio.render(getLocalUser(session())));
    }

   
    public static Result listMyPortlets() {
    	List<UserPortletStock> portletStocks = UserPortletStock.findByUser(getLocalUser(session()));
    	Set<Portlet> portlets = new HashSet<Portlet>();
	    if(portletStocks != null) {
	    	for (UserPortletStock ups : portletStocks) {
	    		portlets.add(ups.getPortlet());
			}
	    }
    	return ok(Json.toJson(portlets));
    }

    public static Result listMyPortletStocks() {
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

    private static boolean isLoggedIn(Session session) {
    	final User localUser = getLocalUser(session);
    	if(localUser == null) {
            flash(FLASH_ERROR_KEY, "Please login first");
    		Logger.error("Please login first");
    		return false;
    	}
    	return true;
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
	
	public static Result returnCorsOptions(String path) {
		Response response = response();
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, x-cookie-value, Content-Type, Accept, Authorization, X-Auth-Token");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		return ok();
	}
}
