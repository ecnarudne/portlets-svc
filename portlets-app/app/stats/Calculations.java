package stats;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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
		for (UserPortletStock u : ups) {
			Stock stock = Stock.findBySymbol(u.getStock());
			StockStats stats = StockStats.findStockStatsOnDate(stock, onDate);
			if(stats != null) {
				portfolioValue += stats.getClosePrice() * u.getQty();
				//if(Logger.isDebugEnabled()) Logger.debug("Stock: " + u.getStock() + " ClosePrice: " + stats.getClosePrice() + " Qty: " + u.getQty() + " Item Value: " + (stats.getClosePrice() * u.getQty()));
			} else {
				Logger.error("For calc Portfolio Value, No price history found for stock: " + stock.getName() + " on date: " + onDate.toString());
			}
		}
		//if(Logger.isDebugEnabled()) Logger.debug("portfolioValue: " + portfolioValue + " onDate: " + onDate);
		return portfolioValue;
	}

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException("Decimal places should be positive");
	    return new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
	}
}
