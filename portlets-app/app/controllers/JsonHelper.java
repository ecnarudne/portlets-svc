package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import play.mvc.Controller;
import play.mvc.Result;
import service.CorsComposition;
import service.JsonTester;

@CorsComposition.Cors
public class JsonHelper extends Controller{
	 	public static Result getPortfoliodetail(){
	    	JsonNode json=	JsonTester.getJsonObject("public/jsonFiles/portfolio.json");
	    	return ok(json);
	    }
	 	
	    public static Result getDiscoverPageDetails(){    	    		    	
	    	JsonNode portletInfoData = JsonTester.getJsonObject("public/jsonFiles/discover.json");    	
	    	return ok(portletInfoData);
	    }
	    
	    public static Result getPortletPageDetails(){    	    		    	
	    	JsonNode portletInfoData = JsonTester.getJsonObject("public/jsonFiles/pagePortlet.json");    	
	    	return ok(portletInfoData);
	    }
}
