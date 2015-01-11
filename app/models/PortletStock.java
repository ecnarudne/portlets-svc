package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class PortletStock extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	@Required
	@ManyToOne
	private Portlet portlet;
	@Required
	private String stock;
	@Required
	private double qty;

	public static Finder<Long, PortletStock> find = new Finder<Long, PortletStock>(Long.class, PortletStock.class);

	/* Boiler-plates */
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
}
