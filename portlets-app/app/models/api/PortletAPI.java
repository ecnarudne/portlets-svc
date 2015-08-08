package models.api;

import java.util.Date;
import java.util.List;

import models.Portlet;
import models.PortletStock;
import models.PortletValidityState;
import models.Sector;

import org.joda.time.LocalDate;

import play.Logger;
import stats.Calculations;

public class PortletAPI {
	private Portlet portlet;
	private long followerCount;
	private double volatility;
	private double totalReturn;
	private double dailyReturn;
	private double annualReturn;
	private double lastValue;
	public PortletAPI(Portlet p) {
		super();
		this.portlet = p;
		try {
			List<PortletStock> psl = PortletStock.findByPortlet(portlet);
			LocalDate inceptionDate = new LocalDate(portlet.getCreatedOn());
			double portletValueInception = Calculations.calcPortletValue(psl, inceptionDate);
			double portletValue = Calculations.calcPortletValue(psl, LocalDate.now());
			this.setLastValue(portletValue);
			this.setTotalReturn(Calculations.calcReturnFromPrice(portletValue, portletValueInception));
			double portletValueDayBefore = Calculations.calcPortletValue(psl, LocalDate.now().minusDays(1));
			this.setDailyReturn(Calculations.calcReturnFromPrice(portletValue, portletValueDayBefore));
			double portletValueYearBefore = Calculations.calcPortletValue(psl, LocalDate.now().minusYears(1));
			this.setAnnualReturn(Calculations.calcReturnFromPrice(portletValue, portletValueYearBefore));
		} catch (Exception e) {
			Logger.error("Couldn't find stats and returns for Portlet Id: " + p.getId(), e);
		}
	}
	public String getVolatilityClass() {
		return portlet.getVolatilityClass();
	}
	public Long getId() {
		return portlet.getId();
	}
	public String getName() {
		return portlet.getName();
	}
	public Long getOwnerId() {
		if(portlet.getOwner() == null)
			return null;
		return portlet.getOwner().getId();
	}
	public String getOwnerName() {
		if(portlet.getOwner() == null)
			return null;
		return portlet.getOwner().getFullName();
	}
	public String getPictureUrl() {
		return portlet.getPictureUrl();
	}
	public List<Sector> getSectors() {
		return portlet.getSectors();
	}
	public String getNotes() {
		return portlet.getNotes();
	}
	public boolean isVisibleToAll() {
		return portlet.isVisibleToAll();
	}
	public Date getCreatedOn() {
		return portlet.getCreatedOn();
	}
	public PortletValidityState getValidity() {
		return portlet.getValidity();
	}
	public Date getLastRebalancedOn() {
		return portlet.getLastRebalancedOn();
	}
	public String getPrimaryExchange() {
		return portlet.getPrimaryExchange();
	}
	public void setPortlet(Portlet portlet) {
		this.portlet = portlet;
	}
	public long getFollowerCount() {
		return followerCount;
	}
	public void setFollowerCount(long followerCount) {
		this.followerCount = followerCount;
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
	public double getLastValue() {
		return lastValue;
	}
	public void setLastValue(double lastValue) {
		this.lastValue = lastValue;
	}
}
