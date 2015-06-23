package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

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
	private Category category;
	private String pictureUrl;
	private String notes;
	private PortletValidityState validity;
	private boolean visibleToAll;
	private Date lastRebalancedOn;
	private long followerCount;
	private double volatility;
	private Date createdOn;

	public static Finder<Long, Portlet> find = new Finder<Long, Portlet>(Long.class, Portlet.class);

	public Portlet(){}
	public Portlet(String name, User owner, String pictureUrl, Category category) {
		super();
		this.name = name;
		this.owner = owner;
		this.pictureUrl = pictureUrl;
		this.category = category;
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
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
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
}
