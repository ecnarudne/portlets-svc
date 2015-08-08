package models.api;

import org.joda.time.LocalDate;

import play.Logger;
import models.Stock;
import models.StockStats;
import models.UserPortletStock;

public class UserPortletStockAPI {
	private UserPortletStock userPortletStock;
	private StockStats stockStats;
	private double totalReturn;
	private double dailyReturn;
	private double annualReturn;

	public UserPortletStockAPI(){}
	public UserPortletStockAPI(UserPortletStock ups, StockStats stats) {
		super();
		this.userPortletStock = ups;
		this.stockStats = stats;
		Stock stock = Stock.findBySymbol(ups.getStock());
		try {
			this.totalReturn = ((100*(ups.getBuyPrice() - stats.getClosePrice()))/ups.getBuyPrice());
		} catch (Exception e) {
			Logger.error("Couldn't find totalReturn for ups Id: " + ups.getId(), e);
		}
		try {
			StockStats statsDayBefore = StockStats.findStockStatsOnDate(stock, LocalDate.now().minusDays(1));
			this.dailyReturn = ((100*(stats.getClosePrice() - statsDayBefore.getClosePrice()))/statsDayBefore.getClosePrice());
		} catch (Exception e) {
			Logger.error("Couldn't find dailyReturn for ups Id: " + ups.getId(), e);
		}
		try {
			StockStats statsYearBefore = StockStats.findStockStatsOnDate(stock, LocalDate.now().minusYears(1));
			this.annualReturn = ((100*(stats.getClosePrice() - statsYearBefore.getClosePrice()))/statsYearBefore.getClosePrice());
		} catch (Exception e) {
			Logger.error("Couldn't find annualReturn for ups Id: " + ups.getId(), e);
		}
	}
	public double getTotalReturn() {
		return totalReturn;
	}
	public void setTotalReturn(double totalReturn) {
		this.totalReturn = totalReturn;
	}
	public double getDailyReturn() {
		return dailyReturn;
	}
	public void setDailyReturn(double dailyReturn) {
		this.dailyReturn = dailyReturn;
	}
	public UserPortletStock getUserPortletStock() {
		return userPortletStock;
	}
	public void setUserPortletStock(UserPortletStock userPortletStock) {
		this.userPortletStock = userPortletStock;
	}
	public StockStats getStockStats() {
		return stockStats;
	}
	public void setStockStats(StockStats stockStats) {
		this.stockStats = stockStats;
	}
	public Long getId() {
		return userPortletStock.getId();
	}
	public Long getUserId() {
		return userPortletStock.getUser().getId();
	}
	public Long getPortletid() {
		return userPortletStock.getPortlet().getId();
	}
	public double getQty() {
		return userPortletStock.getQty();
	}
	public double getBuyPrice() {
		return userPortletStock.getBuyPrice();
	}
	public long getBuyEpoch() {
		return userPortletStock.getBuyEpoch();
	}
	public double getBuyWeight() {
		return userPortletStock.getBuyWeight();
	}
	public double getAnnualReturn() {
		return annualReturn;
	}
	public void setAnnualReturn(double annualReturn) {
		this.annualReturn = annualReturn;
	}
}
