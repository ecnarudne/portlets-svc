package models;


public class Portfolio {
	private User owner;
	/* Calculated attributes */
	private long portletCount;
	private long followedPortletCount;
	private double portfolioValue;
	private double volatility;
	private double totalReturn;
	private double dailyReturn;
	private double annualReturn;
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public long getPortletCount() {
		return portletCount;
	}
	public void setPortletCount(long portletCount) {
		this.portletCount = portletCount;
	}
	public long getFollowedPortletCount() {
		return followedPortletCount;
	}
	public void setFollowedPortletCount(long followedPortletCount) {
		this.followedPortletCount = followedPortletCount;
	}
	public double getPortfolioValue() {
		return portfolioValue;
	}
	public void setPortfolioValue(double portfolioValue) {
		this.portfolioValue = portfolioValue;
	}
	public double getVolatility() {
		return volatility;
	}
	public void setVolatility(double volatility) {
		this.volatility = volatility;
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
	public double getAnnualReturn() {
		return annualReturn;
	}
	public void setAnnualReturn(double annualReturn) {
		this.annualReturn = annualReturn;
	}
}
