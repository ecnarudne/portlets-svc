package models.api;

import play.Logger;
import models.StockStats;
import models.UserPortletStock;

public class UserPortletStockAPI {
	private Long id;
	private Long userId;
	private Long portletid;
	private StockStats stats;
	private double qty;
	private double buyPrice;
	private long buyEpoch;
	private double totalReturn;
	private double dailyReturn;

	public UserPortletStockAPI(){}
	public UserPortletStockAPI(UserPortletStock ups, StockStats stats) {
		super();
		this.id = ups.getId();
		this.userId = ups.getUser().getId();
		this.portletid = ups.getPortlet().getId();
		this.stats = stats;
		this.qty = ups.getQty();
		this.buyPrice = ups.getBuyPrice();
		this.buyEpoch = ups.getBuyEpoch();
		try {
			this.totalReturn = ((100*(ups.getBuyPrice() - Double.parseDouble(stats.getClosePrice())))/ups.getBuyPrice());
		} catch (Exception e) {
			Logger.error("Couldn't find totalReturn for ups Id: " + ups.getId(), e);
		}
		try {
			//TODO calculate against previous day's price
			this.dailyReturn = ((100*(ups.getBuyPrice() - Double.parseDouble(stats.getClosePrice())))/ups.getBuyPrice());
		} catch (Exception e) {
			Logger.error("Couldn't find totalReturn for ups Id: " + ups.getId(), e);
		}
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getPortletid() {
		return portletid;
	}
	public void setPortletid(Long portletid) {
		this.portletid = portletid;
	}
	public StockStats getStats() {
		return stats;
	}
	public void setStats(StockStats stats) {
		this.stats = stats;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public double getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}
	public long getBuyEpoch() {
		return buyEpoch;
	}
	public void setBuyEpoch(long buyEpoch) {
		this.buyEpoch = buyEpoch;
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
}
