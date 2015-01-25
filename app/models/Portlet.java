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
	private String pictureUrl;
	private String notes;
	private PortletValidityState validity;
	private boolean visibleToAll;
	private Date createdOn;

	public static Finder<Long, Portlet> find = new Finder<Long, Portlet>(Long.class, Portlet.class);

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
}
