package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import models.Sector;

@Entity
public class Portlet extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	@Required
	private String name;
	@ManyToOne
	private User owner;
	@ManyToOne
	private Sector sector;
	private String pictureUrl;
	private String notes;
	private PortletValidityState validity;
	private boolean visibleToAll;
	private Date lastRebalancedOn;
	private Date createdOn;

	/* Calculated attributes */
	private long followerCount;
	private double volatility;
	private double totalReturn;
	private double dailyReturn;
	private double annualReturn;

	public static Finder<Long, Portlet> find = new Finder<Long, Portlet>(Long.class, Portlet.class);

	public Portlet(){}
	public Portlet(String name, User owner, String pictureUrl, Sector sector) {
		super();
		this.name = name;
		this.owner = owner;
		this.pictureUrl = pictureUrl;
		this.sector = sector;
		this.createdOn = new Date();
	}

	public static Portlet findByName(String name) {
		//TODO must cache
		if(name == null)
			return null;
		List<Portlet> list = find.where().eq("name", name).findList();
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	public static List<Portlet> findBySector(Sector sector) {
		//TODO must cache
		if(sector == null)
			return null;
		List<Portlet> list = find.where().eq("sector", sector).findList();
		if(list != null && !list.isEmpty())
			return list;
		return null;
	}

	/* Boiler-plates */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public Sector getSector() {
		return sector;
	}
	public void setSector(Sector sector) {
		this.sector = sector;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public boolean isVisibleToAll() {
		return visibleToAll;
	}
	public void setVisibleToAll(boolean visibleToAll) {
		this.visibleToAll = visibleToAll;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public PortletValidityState getValidity() {
		return validity;
	}
	public void setValidity(PortletValidityState validity) {
		this.validity = validity;
	}
	public Date getLastRebalancedOn() {
		return lastRebalancedOn;
	}
	public void setLastRebalancedOn(Date lastRebalancedOn) {
		this.lastRebalancedOn = lastRebalancedOn;
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
}
