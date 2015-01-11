package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Portlet extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	@Required
	private String name;
	private String pictureUrl;
	private String notes;

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
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
}
