package models.api;

import java.util.List;

import models.Portlet;
import models.User;
import models.UserPortletStock;

import org.joda.time.LocalDate;

import play.Logger;
import stats.Calculations;

public class PortfolioAPI {
	private User owner;
	private long portletCount;
	private long followedPortletCount;
	private int portletCreatedCount;
	private double portfolioValue;
	private double dailyReturn;
	private double annualReturn;
	private double volatility;

	public PortfolioAPI(User owner) {
		super();
		this.owner = owner;
		try {
			List<Portlet> created = Portlet.findByOwner(owner);
			if(created != null)
				this.portletCreatedCount = created.size();
			List<UserPortletStock> ups = UserPortletStock.findByUser(owner);
			if(ups != null && ups.size() > 0) {
				this.portfolioValue = Calculations.calcPortfolioValue(ups, LocalDate.now());
				double portfolioValueDayBefore = Calculations.calcPortfolioValue(ups, LocalDate.now().minusDays(1));
				this.dailyReturn = Calculations.calcReturnFromPrice(portfolioValue, portfolioValueDayBefore);
				double portfolioValueYearBefore = Calculations.calcPortfolioValue(ups, LocalDate.now().minusYears(1));
				this.annualReturn = Calculations.calcReturnFromPrice(portfolioValue, portfolioValueYearBefore);
				double volatility = Calculations.calcPortfolioVolatility(ups);
				this.volatility = volatility;
			} else {
				Logger.info("No portlet subscriptions found to calculate Portfolio value");
			}
		} catch (Exception e) {
			Logger.error("Portfolio information unavailable. ", e);
		}
	}
	public String getOwnerName() {
		return owner.getFullName();
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public long getPortletCount() {
		return portletCount;
	}
	public long getFollowedPortletCount() {
		return followedPortletCount;
	}
	public void setFollowedPortletCount(long followedPortletCount) {
		this.followedPortletCount = followedPortletCount;
	}
	public int getPortletCreatedCount() {
		return portletCreatedCount;
	}
	public double getPortfolioValue() {
		return portfolioValue;
	}
	public double getDailyReturn() {
		return dailyReturn;
	}
	public double getAnnualReturn() {
		return annualReturn;
	}
	public double getVolatility() {
		return volatility;
	}
}
