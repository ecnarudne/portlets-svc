package stats;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import models.PortletStats;
import models.PortletStock;
import models.Stock;
import models.StockStats;
import models.UserPortletStock;

import org.joda.time.LocalDate;

import play.Logger;

public class Calculations {

	public static double calcReturnFromStats(StockStats currentStockStats, StockStats previousStockStats) {
		if(previousStockStats == null) {
			Logger.warn("Missing previous stats to calculate returns");
			return 0;
		}
		return calcReturnFromPrice(currentStockStats.getClosePrice(), previousStockStats.getClosePrice());
	}

	public static double calcReturnFromPrice(double newPrice, double oldPrice) {
		//return round((100*(newPrice - oldPrice))/oldPrice, 2);
		if(oldPrice == 0) {
			Logger.error("Wrong data to calculate Return for oldPrice: " + oldPrice);
			return 0;
		}
		return (100*(newPrice - oldPrice))/oldPrice;
	}

	public static double calcPortletValue(List<PortletStock> psl, LocalDate onDate) {
		double portfolioValue = 0;
		try {
			for (PortletStock ps : psl) {
				Stock stock = Stock.findBySymbol(ps.getStock());//TODO must cache
				StockStats stats = StockStats.findStockStatsOnDate(stock, onDate);
				if(stats != null) {
					//if(Logger.isDebugEnabled()) Logger.debug(" ClosePrice: " + stats.getClosePrice() + " Stock: " + ps.getStock() + " onDate: " + onDate);
					portfolioValue += (stats.getClosePrice() * ps.getWeightage())/100;
				} else {
					Logger.error("For calc Portlet Value, No price history found for stock: " + stock.getName() + " on date: " + onDate.toString());
				}
			}
		} catch (Exception e) {
			Logger.error("Failed to calculate Portlet Value for PortletStocks: " + psl, e);
		}
		return portfolioValue;
	}

	public static double calcPortfolioValue(List<UserPortletStock> ups, LocalDate onDate) {
		double portfolioValue = 0;
		//if(Logger.isDebugEnabled()) Logger.debug("ups size: " + ups.size() + " onDate: " + onDate);
		for (UserPortletStock u : ups) {
			//if(u.getBuyEpoch() >= onDate.toDate().getTime()) {//TODO check if portfolio value to be calculated before creation
				Stock stock = Stock.findBySymbol(u.getStock());
				StockStats stats = StockStats.findStockStatsOnDate(stock, onDate);
				//if(Logger.isDebugEnabled()) Logger.debug("Stock: " + u.getStock() + " ClosePrice: " + stats.getClosePrice() + " Qty: " + u.getQty() + " Item Value: " + (stats.getClosePrice() * u.getQty()));
				if(stats != null) {
					portfolioValue += stats.getClosePrice() * u.getQty();
					//if(Logger.isDebugEnabled()) Logger.debug("Portfolio Value: " + portfolioValue + "Stock: " + u.getStock() + " ClosePrice: " + stats.getClosePrice() + " Qty: " + u.getQty() + " Item Value: " + (stats.getClosePrice() * u.getQty()));
				} else {
					Logger.error("For calc Portfolio Value, No price history found for stock: " + stock.getName() + " on date: " + onDate.toString());
				}
			//}
		}
		//if(Logger.isDebugEnabled()) Logger.debug("portfolioValue: " + portfolioValue + " onDate: " + onDate);
		return portfolioValue;
	}

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException("Decimal places should be positive");
	    return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
	}

	public static double calcPortfolioVolatility(List<UserPortletStock> upsList) {
		double portfolioValue = 0;
		for (UserPortletStock ups : upsList) {
			StockStats stats = StockStats.findLatestBySymbol(ups.getStock());//TODO must cache
			if(stats != null)
				portfolioValue += ups.getQty() * stats.getClosePrice();
			else
				Logger.error("Missing stats for stock: " + ups.getStock());
		}
		if(portfolioValue == 0) {
			Logger.error("Missing data to calculate Portfolio Value for UserPortletStock List: " + upsList);
			return 0;
		}
		double volatilityPrincipal = 0;
		for (UserPortletStock ups : upsList) {
			Stock stock = Stock.findBySymbol(ups.getStock());//TODO must cache
			StockStats stats = StockStats.findLatestBySymbol(ups.getStock());//TODO must cache
			Double dailyVolatility = StockStats.calcDailyVolatility(stock.getId(), null, null);
			if(dailyVolatility != null && stats != null) {
				volatilityPrincipal  += dailyVolatility.doubleValue()*ups.getQty()*stats.getClosePrice();
			} else
				Logger.error("Missing stats for stock: " + ups.getStock());
		}
		return volatilityPrincipal/portfolioValue;
	}

	public static double calcPortletVolatility(Long portletId) {
		Double volatility = PortletStats.calcDailyVolatility(portletId, null, null);
		if(volatility != null)
			return (volatility.doubleValue() * 100);
		else {
			Logger.error("Failed to calculate volatility for portletId: " + portletId);
			return 0;
		}
	}
}
