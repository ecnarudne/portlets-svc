package models.api;

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
			//if(Logger.isDebugEnabled()) Logger.debug("portletValueInception: " + portletValueInception);
			double portletValue = Calculations.calcPortletValue(psl, LocalDate.now());
			if(Logger.isDebugEnabled()) Logger.debug("portletValue: " + portletValue);
			this.setLastValue(portletValue);
			this.setTotalReturn(Calculations.calcReturnFromPrice(portletValue, portletValueInception));
			//if(Logger.isDebugEnabled()) Logger.debug("TotalReturn: " + this.getTotalReturn());
			double portletValueDayBefore = Calculations.calcPortletValue(psl, LocalDate.now().minusDays(1));
			//if(Logger.isDebugEnabled()) Logger.debug("portletValueDayBefore: " + portletValueDayBefore);
			this.setDailyReturn(Calculations.calcReturnFromPrice(portletValue, portletValueDayBefore));
			//if(Logger.isDebugEnabled()) Logger.debug("DailyReturn: " + this.getDailyReturn());
			double portletValueYearBefore = Calculations.calcPortletValue(psl, LocalDate.now().minusYears(1));
			//if(Logger.isDebugEnabled()) Logger.debug("portletValueYearBefore: " + portletValueYearBefore);
			this.setAnnualReturn(Calculations.calcReturnFromPrice(portletValue, portletValueYearBefore));
			//if(Logger.isDebugEnabled()) Logger.debug("AnnualReturn: " + this.getAnnualReturn());
			this.setVolatility(Calculations.calcPortletVolatility(portlet.getId()));
			if(Logger.isDebugEnabled()) Logger.debug("Volatility: " + this.getVolatility());
		} catch (Exception e) {
			Logger.error("Couldn't find stats and returns for Portlet Id: " + p.getId(), e);
		}
	}
	public VolatilityClass getVolatilityClass() {
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
	public String getCreatedOn() {
		if(portlet.getCreatedOn() == null)
			return null;
		return portlet.getCreatedOn().toString();
	}
	public PortletValidityState getValidity() {
		return portlet.getValidity();
	}
	public String getLastRebalancedOn() {
		if(portlet.getLastRebalancedOn() == null)
			return null;
		return portlet.getLastRebalancedOn().toString();
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
