package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Follows extends Model {
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	@Required
	@ManyToOne
	private User user;
	@Required
	@ManyToOne
	private Portlet portlet;
	private Date createdOn;

	public Follows(){}

	public Follows(User user, Portlet portlet) {
		super();
		this.user = user;
		this.portlet = portlet;
		this.createdOn = new Date();
	}

	public static Finder<Long, Follows> find = new Finder<Long, Follows>(Long.class, Follows.class);

	public static List<Follows> findByUser(User user) {
		if(user == null)
			return null;
		return find.where().eq("user", user).findList();
	}

	public static int countByUser(Long userId) {
		return find.where().eq("user_id", userId).findRowCount();
	}

	public static int countByPortlet(Long portletId) {
		return find.where().eq("portlet_id", portletId).findRowCount();
	}

	public static List<Follows> findByPortlet(Portlet portlet) {
		//TODO must cache
		if(portlet == null)
			return null;
		return find.where().eq("portlet", portlet).findList();
	}

	public static Follows findByUserAndPortlet(User user, Portlet portlet) {
		if(user == null || portlet == null)
			return null;
		return find.where().eq("user", user).eq("portlet", portlet).findUnique();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Portlet getPortlet() {
		return portlet;
	}

	public void setPortlet(Portlet portlet) {
		this.portlet = portlet;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
}