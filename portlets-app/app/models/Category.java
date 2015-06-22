package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Category extends Model {
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	@Required
	private String name;
	private String pictureUrl;
	private String notes;
	private Category parent;
	private Date createdOn;
	public static Finder<Long, Category> find = new Finder<Long, Category>(Long.class, Category.class);

	public Category(){}
	public Category(String name) {
		super();
		this.name = name;
		this.createdOn = new Date();
	}
	public static Category findByName(String name) {
		//TODO must cache
		if(name == null)
			return null;
		List<Category> list = find.where().eq("name", name).findList();
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
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
}
