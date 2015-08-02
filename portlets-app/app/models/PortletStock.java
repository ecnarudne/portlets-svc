package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class PortletStock extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	//@Required
	@ManyToOne
	private Portlet portlet;
	//@Required
	private String stock;
	//@Required
	private double weightage;
	private Date lastUpdatedOn;

	public static Finder<Long, PortletStock> find = new Finder<Long, PortletStock>(Long.class, PortletStock.class);

	public PortletStock(){}
	public PortletStock(Portlet portlet, String stock, double weightage) {
		super();
		this.portlet = portlet;
		this.stock = stock;
		this.weightage = weightage;
	}

	@Override
	public void save() {
		super.save();
		PortletDefinitionAudit audit = new PortletDefinitionAudit(this, System.currentTimeMillis());//TODO get single epoch for all audits for same changefor 
		audit.save();
	}
	
	public static List<PortletStock> findByPortlet(Portlet portlet) {
		if(portlet == null)
			return null;
		return find.where().eq("portlet", portlet).findList();
	}

	public static List<PortletStock> findByPortletAndStock(Portlet portlet, String stock) {
		if(portlet == null || stock == null)
			return null;
		return find.where().eq("portlet", portlet).eq("stock", stock).findList();
	}

	/* Boiler-plates */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Portlet getPortlet() {
		return portlet;
	}
	public void setPortlet(Portlet portlet) {
		this.portlet = portlet;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public double getWeightage() {
		return weightage;
	}
	public void setWeightage(double weightage) {
		this.weightage = weightage;
	}
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
}
