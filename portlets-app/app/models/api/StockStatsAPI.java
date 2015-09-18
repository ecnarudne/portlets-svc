package models.api;

import models.StockStats;

public class StockStatsAPI {
	private StockStats stockStats;

	public StockStatsAPI(StockStats stockStats) {
		super();
		this.stockStats = stockStats;
	}

	public String getStock() {
		if(stockStats.getStock() == null)
			return null;
		return stockStats.getStock().getName();
	}

	public String getActivity() {
		return stockStats.getActivity();
	}

	public Long getId() {
		return stockStats.getId();
	}

	public String getExchange() {
		return stockStats.getExchange();
	}

	public String getLocalDate() {
		if(stockStats.getLocalDate() == null)
			return null;
		return stockStats.getLocalDate().toString();
	}

	public double getOpenPrice() {
		return stockStats.getOpenPrice();
	}

	public double getClosePrice() {
		return stockStats.getClosePrice();
	}

	public double getHighPrice() {
		return stockStats.getHighPrice();
	}

	public double getLowPrice() {
		return stockStats.getLowPrice();
	}

	public double getAvgVol() {
		return stockStats.getAvgVol();
	}

	public double getMktcap() {
		return stockStats.getMktcap();
	}
}
