package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Exchange;
import models.Follows;
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
import models.api.PortfolioAPI;
import models.api.PortletAPI;
import models.api.PortletStockAPI;
import models.api.StockStatsAPI;
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

public class Application extends Controller {

    public static final String FLASH_ERROR_KEY = "FLASH_ERROR";
    public static final String FLASH_SUCCESS_KEY = "FLASH_SUCCESS";
	private static final int LIST_DEFAULT_LIMIT = 10;
	private static final int LIST_MAX_LIMIT = 100;

	public static Result portfolioPriceHistory(){
    	User currentUser = getLocalUser(request(), session());
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
		if(Logger.isDebugEnabled()) Logger.debug("Price History dates size: " + dates.size());
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
    	User currentUser = getLocalUser(request(), session());
    	if(currentUser == null)
    		return forbidden("Login Required");
    	PortfolioAPI portfolio = new PortfolioAPI(currentUser);
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
		//printSession();
        //return ok(index.render(getLocalUser(request(), session())));
		return ok(index.render());
    }

    public static Result users() {
        return ok(users.render(getLocalUser(request(), session())));
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

	public static boolean isTokenValid(String[] headers) {
        if(headers != null && headers.length > 0) {
        	try {
				String token = headers[0];
				UserToken userToken = UserToken.findByToken(token);
				if(userToken != null) {
					return true;
				} else {
					Logger.warn("Token not found");
		        	return false;
				}
			} catch (Exception e) {
	        	Logger.warn("Token not valid");
	        	return false;
			}
        } else {
        	Logger.warn("No Token in Header");
        	return false;
        }
    }

	public static UserToken findUserToken(String[] headers) {
        if(headers != null && headers.length > 0) {
        	try {
				String token = headers[0];
				UserToken userToken = UserToken.findByToken(token);
				if(userToken != null) {
					return userToken;
				} else {
					Logger.warn("Token not found");
					return null;
				}
			} catch (Exception e) {
	        	Logger.warn("Token not valid");
				return null;
			}
        } else {
        	Logger.warn("No Token in Header");
        	return null;
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
        return ok(sectors.render(getLocalUser(request(), session())));
    }
    
    
    public static Result listSectors() {
    	List<Sector> list = Sector.find.all();
    	return ok(Json.toJson(list));
    }

    public static Result addSector() {
    	Sector newSector = Form.form(Sector.class).bindFromRequest().get();
    	if(!isLoggedIn(request(), session()))
			return forbidden();
		newSector.setCreatedOn(new Date());
    	newSector.save();
    	return redirect(routes.Application.sectors());
    }

    public static Result definePortletJson() {
    	if(!isLoggedIn(request(), session()))
			return forbidden();
        JsonNode root = request().body().asJson();
        if(root != null) {
        	Portlet newPortlet = Portlet.fromJson(root);
        	newPortlet.setCreatedOn(new Date());
	    	newPortlet.save();
	    	return ok();
        } else {
            return badRequest("No JSON Data found");
        }
    }

    public static Result followPortlet(Long portletId) {
    	if(!isLoggedIn(request(), session()))
			return forbidden();
    	User user = getLocalUser(request(), session());
    	Portlet portlet = Portlet.find.byId(portletId);
        if(portlet != null) {
        	Follows existing = Follows.findByUserAndPortlet(user, portlet);
        	if(existing == null) {
	        	Follows follows = new Follows(user, portlet);
	        	follows.save();
        	}
        	return ok();
        } else {
            return badRequest("Portlet not found by ID: " + portletId);
        }
    }

    public static Result unfollowPortlet(Long portletId) {
    	if(!isLoggedIn(request(), session()))
			return forbidden();
    	User user = getLocalUser(request(), session());
    	Portlet portlet = Portlet.find.byId(portletId);
        if(portlet != null) {
        	Follows existing = Follows.findByUserAndPortlet(user, portlet);
        	existing.delete();
        	return ok();
        } else {
            return badRequest("Portlet not found by ID: " + portletId);
        }
    }

    public static Result updateProfileText() {
    	if(!isLoggedIn(request(), session()))
			return forbidden();
    	User user = getLocalUser(request(), session());
        JsonNode json = request().body().asJson();
        if(json != null) {
            user.setNameTitle(json.findPath("nametitle").asText());
            user.setProfileTitle(json.findPath("profiletitle").asText());
            user.setProfileDescription(json.findPath("profiledescription").asText());
            return ok();
        } else {
        	return badRequest("No JSON Data found");
        }
    }

    public static Result subscribeToPortletJson(Long portletId) {
    	if(!isLoggedIn(request(), session()))
			return forbidden();
        JsonNode json = request().body().asJson();
        if(json != null) {
            double amount = json.findPath("amount").asDouble();
            if(amount != 0) {
            	Portlet portlet = Portlet.find.byId(portletId);
            	List<PortletStock> stocks = PortletStock.findByPortlet(portlet);
            	double remainingAmount = amount;
            	for (PortletStock ps : stocks) {
            		double currentPx = StockStats.findLatestBySymbol(ps.getStock()).getClosePrice();

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
        		    	newUserPortletStock.setUser(getLocalUser(request(), session()));
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
    	if(!isLoggedIn(request(), session()))
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
    	if(!isLoggedIn(request(), session()))
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
		//StockStats stats = CsvMarketDataLoader.loadStockStatsBySymbol(symbol);
    	Stock stock = Stock.findBySymbol(symbol);
    	StockStats stats = StockStats.findLatestByStock(stock);
    	return ok(Json.toJson(stats));
    }

    public static Result listMyStockStatsByPortlet(Long portletId) {
    	List<UserPortletStock> stocks = UserPortletStock.findByUserAndPortlet(getLocalUser(request(), session()), portletId);
    	Logger.debug("stocks.size(): " + stocks.size());
    	ArrayList<UserPortletStockAPI> list = new ArrayList<UserPortletStockAPI>(stocks.size());
    	for (UserPortletStock ups : stocks) {
    		StockStats stats = StockStats.findLatestBySymbol(ups.getStock());//TODO must cache
    		UserPortletStockAPI api = new UserPortletStockAPI(ups, stats);
			list.add(api );
		}
    	return ok(Json.toJson(list));
    }

    public static Result listStockStatsByPortlet(Long portletId) {
    	List<PortletStock> stocks = PortletStock.findByPortletId(portletId);
    	Logger.debug("stocks.size(): " + stocks.size());
    	ArrayList<StockStatsAPI> list = new ArrayList<StockStatsAPI>(stocks.size());
    	for (PortletStock ps : stocks) {
    		StockStats stats = StockStats.findLatestBySymbol(ps.getStock());//TODO must cache
			list.add(new StockStatsAPI(stats));
		}
    	return ok(Json.toJson(list));
    }

    public static Result portlets() {
        return ok(portlets.render(getLocalUser(request(), session())));
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
    	List<Portlet> portlets = Portlet.findBySector(sectorId);
    	List<PortletAPI> list = new ArrayList<PortletAPI>();
    	for (Portlet portlet : portlets) {
			list.add(new PortletAPI(portlet));
		}
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
    	List<UserPortletStock> list = UserPortletStock.findByUser(getLocalUser(request(), session()));
    	return ok(Json.toJson(list));
    }

    public static Result myStocks() {
        return ok(mystocks.render(getLocalUser(request(), session())));
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
		if(Logger.isDebugEnabled()) Logger.debug("Subscribing for amount: " + amount + " on portletId: " + portletId);

    	for (PortletStock ps : stocks) {
        	UserPortletStock newUserPortletStock = new UserPortletStock();
    		newUserPortletStock.setPortlet(portlet);
	    	newUserPortletStock.setStock(ps.getStock());
    		double buyPrice = StockStats.findLatestBySymbol(ps.getStock()).getClosePrice();
    		newUserPortletStock.setBuyPrice(buyPrice);
	    	newUserPortletStock.setQty((amount*ps.getWeightage())/(buyPrice*100));
	    	newUserPortletStock.setBuyWeight(ps.getWeightage());
	    	newUserPortletStock.setUser(getLocalUser(request(), session()));
	    	newUserPortletStock.setBuyEpoch(System.currentTimeMillis());
	    	Logger.info("Saving: " + newUserPortletStock);
	    	newUserPortletStock.save();//TODO Transaction
    	}
    	return redirect(routes.Application.myStocks());
    }

    public static Result stocksInPortlet(Long portletId) {
        return ok(stocksinportlet.render(getLocalUser(request(), session()), portletId));
    }

    public static Result addPortlet() {
    	Portlet newPortlet = Form.form(Portlet.class).bindFromRequest().get();
    	newPortlet.setCreatedOn(new Date());
    	
    	final User localUser = getLocalUser(request(), session());
    	if(localUser != null) {
        	try {
        		//TODO start transaction
		        final JsonNode json = request().body().asJson();
		        if(json != null) {
					newPortlet.setOwner(localUser);
					newPortlet.setValidity(PortletValidityState.PENDING);
		        	JsonNode sectorId = json.findPath("sectorId");
		        	if(sectorId != null) {
		        		Sector sector = Sector.find.byId(sectorId.asLong());
		        		newPortlet.addSector(sector);
		        	}
			    	newPortlet.save();
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
				Logger.error("Failed to Create Portlet for request: " + request().body(), e);
			}
		    	
    	} else {
            flash(FLASH_ERROR_KEY, "Please login first");
    		Logger.error("Please login first");
    	}
    	return ok(Json.toJson(newPortlet));
    }

    public static Result portfolio() {
        return ok(portfolio.render(getLocalUser(request(), session())));
    }

    public static Result listOwnedPortlets() {
    	List<Portlet> portlets = Portlet.findByOwner(getLocalUser(request(), session()));
    	List<PortletAPI> portletAPIs = new ArrayList<PortletAPI>();
	    if(portlets != null) {
	    	for (Portlet p : portlets) {
	    		portletAPIs.add(new PortletAPI(p));
			}
	    }
    	return ok(Json.toJson(portletAPIs));
    }

    public static Result listMyPortlets() {
    	List<UserPortletStock> portletStocks = UserPortletStock.findByUser(getLocalUser(request(), session()));
    	Set<PortletAPI> portlets = new HashSet<PortletAPI>();
	    if(portletStocks != null) {
	    	for (UserPortletStock ups : portletStocks) {
	    		portlets.add(new PortletAPI(ups.getPortlet()));
			}
	    }
    	return ok(Json.toJson(portlets));
    }

    public static Result listMyPortletStocks() {
    	List<UserPortletStock> list = UserPortletStock.findByUser(getLocalUser(request(), session()));
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
    	newUserPortletStock.setUser(getLocalUser(request(), session()));
    	newUserPortletStock.setBuyEpoch(System.currentTimeMillis());
    	Logger.info("Saving: " + newUserPortletStock);
    	newUserPortletStock.save();
    	return redirect(routes.Application.portfolio());
    }

    public static User getLocalUser(final Request request, final Session session) {
        User localUser = User.findByAuthUserIdentity(PlayAuthenticate.getUser(session));
        if(localUser == null) {
        	UserToken userToken = findUserToken(request().headers().get("token"));
        	localUser = userToken.getUser();
        }
        return localUser;
    }

    private static boolean isLoggedIn(Request request, Session session) {
    	final User localUser = getLocalUser(request, session);
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
