package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class PortletDefinitionAudit extends Model {
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private long portletId;
	private String stock;
	private long epoch;
	private double weightage;
	private Date lastUpdatedOn;
	public static Finder<Long, PortletDefinitionAudit> find = new Finder<Long, PortletDefinitionAudit>(Long.class, PortletDefinitionAudit.class);

	public PortletDefinitionAudit(PortletStock portletStock, long epoch) {
		super();
		this.portletId = portletStock.getPortlet().getId();
		this.stock = portletStock.getStock();
		this.weightage = portletStock.getWeightage();
		this.lastUpdatedOn = new Date();
		this.epoch = epoch;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getPortletId() {
		return portletId;
	}
	public void setPortletId(long portletId) {
		this.portletId = portletId;
	}
	public long getEpoch() {
		return epoch;
	}
	public void setEpoch(long epoch) {
		this.epoch = epoch;
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
