package models;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.databind.JsonNode;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.libs.Json;
import models.Sector;
import models.api.VolatilityClass;

@Entity
public class Portlet extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	@Required
	private String name;
	@ManyToOne
	private User owner;
	@ManyToMany
	private List<Sector> sectors;
	private String pictureUrl;
	private String notes;
	private PortletValidityState validity;
	private boolean visibleToAll;
	private String primaryExchange = Exchange.NASDAQ;
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
	public Portlet(String name, User owner, String pictureUrl, List<Sector> sectors) {
		super();
		this.name = name;
		this.owner = owner;
		this.pictureUrl = pictureUrl;
		this.sectors = sectors;
		this.createdOn = new Date();
	}
	public static Portlet fromJson(JsonNode root) {
		Portlet instance = Json.fromJson(root, Portlet.class);
/* If Sectors do not get populated
		Iterator<JsonNode> sectors = root.path("sectors").elements();
		while (sectors.hasNext()) {
			Sector sector = Json.fromJson(sectors.next(), Sector.class);
			instance.getSectors().add(sector);
		}
*/
		return instance;
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

	public static List<Portlet> findByOwner(User owner) {
		//TODO must cache
		if(owner == null)
			return null;
		return find.where().eq("owner", owner).findList();
	}

	public static List<Portlet> findBySector(Long sectorId) {
		//TODO must cache
		if(sectorId == null)
			return null;
	    return find.fetch("sectors").where().eq("sectors.id", sectorId).findList();
	}

	public static List<Portlet> findRecent(int limit) {
		//TODO must cache
		List<Portlet> list = find.orderBy("createdOn").setMaxRows(limit).findList();
		if(list != null && !list.isEmpty())
			return list;
		return null;
	}

	public static Collection<Sector> findByPartName(String partName) {
		List<Portlet> list = find.where().like("name", "%"+partName+"%").findList();
		Set<Sector> sectors = new HashSet<Sector>();
		for (Portlet portlet : list) {
			sectors.addAll(portlet.getSectors());
		}
		return sectors;
	}

	public String getVolatilityClass() {
		if(volatility < 2) {//Daily
			return VolatilityClass.LOW.getDisplayName();
		} else if(volatility < 4) {
			return VolatilityClass.MEDIUM.getDisplayName();
		} else {
			return VolatilityClass.HIGH.getDisplayName();
		}
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
	public List<Sector> getSectors() {
		return sectors;
	}
	public void setSectors(List<Sector> sectors) {
		this.sectors = sectors;
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
	public String getPrimaryExchange() {
		return primaryExchange;
	}
	public void setPrimaryExchange(String primaryExchange) {
		this.primaryExchange = primaryExchange;
	}
}
