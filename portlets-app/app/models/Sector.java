package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Sector extends Model {
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	@Required
	private String name;
	private String pictureUrl;
	private String notes;
	private Date createdOn;
	private int portletCount;
	private Date portletsCountedOn;
	public static Finder<Long, Sector> find = new Finder<Long, Sector>(Long.class, Sector.class);

	public static int updatePortletCount(Sector sector) {
		int portletCount = Portlet.find.where().eq("sector", sector).findRowCount();
		sector.setPortletCount(portletCount);
		sector.setPortletsCountedOn(new Date());
		sector.save();
		return portletCount;
	}
	
	public Sector(){}
	public Sector(String name) {
		super();
		this.name = name;
		this.createdOn = new Date();
	}
	public static Sector findByName(String name) {
		//TODO must cache
		if(name == null)
			return null;
		List<Sector> list = find.where().eq("name", name).findList();
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}
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
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getPortletCount() {
		return portletCount;
	}
	public void setPortletCount(int portletCount) {
		this.portletCount = portletCount;
	}
	public Date getPortletsCountedOn() {
		return portletsCountedOn;
	}
	public void setPortletsCountedOn(Date portletsCountedOn) {
		this.portletsCountedOn = portletsCountedOn;
	}
}
