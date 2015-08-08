package models.api;

import java.util.Date;

import org.joda.time.LocalDate;

import play.Logger;
import stats.Calculations;
import models.Portlet;
import models.PortletStock;
import models.Stock;
import models.StockStats;

public class PortletStockAPI {
	private PortletStock portletStock;
	private StockStats stockStats;
	private double dailyReturn;
	private double annualReturn;
	public PortletStockAPI(PortletStock ps) {
		super();
		this.portletStock = ps;
		Stock stock = Stock.findBySymbol(ps.getStock());
		try {
			this.stockStats = StockStats.findStockStatsOnDate(stock, LocalDate.now());
			StockStats statsDayBefore = StockStats.findStockStatsOnDate(stock, LocalDate.now().minusDays(1));
			this.dailyReturn = Calculations.calcReturnFromStats(this.stockStats, statsDayBefore);
			StockStats statsYearBefore = StockStats.findStockStatsOnDate(stock, LocalDate.now().minusYears(1));
			this.annualReturn = Calculations.calcReturnFromStats(this.stockStats, statsYearBefore);
		} catch (Exception e) {
			Logger.error("Couldn't find stats and returns for PortletStock Id: " + ps.getId(), e);
		}
	}
	public Long getId() {
		return portletStock.getId();
	}
	public PortletAPI getPortlet() {
		if(portletStock.getPortlet() == null)
			return null;
		return new PortletAPI(portletStock.getPortlet());
	}
	public String getStock() {
		return portletStock.getStock();
	}
	public double getWeightage() {
		return portletStock.getWeightage();
	}
	public Date getLastUpdatedOn() {
		return portletStock.getLastUpdatedOn();
	}
	public void setPortletStock(PortletStock portletStock) {
		this.portletStock = portletStock;
	}
	public StockStats getStockStats() {
		return stockStats;
	}
	public void setStockStats(StockStats stockStats) {
		this.stockStats = stockStats;
	}
	public double getDailyReturn() {
		return dailyReturn;
	}
	public void setDailyReturn(double dailyReturn) {
		this.dailyReturn = dailyReturn;
	}
	public double getAnnualReturn() {
		return annualReturn;
	}
	public void setAnnualReturn(double annualReturn) {
		this.annualReturn = annualReturn;
	}
}
