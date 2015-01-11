package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class UserPortletStock extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	//@Required
	@ManyToOne
	private User user;
	//@Required
	@ManyToOne
	private Portlet portlet;
	//@Required
	private String stock;
	//@Required
	private double qty;
	//@Required
	private long buyPrice;
	//@Required
	private long buyEpoch;

	public static Finder<Long, UserPortletStock> find = new Finder<Long, UserPortletStock>(Long.class, UserPortletStock.class);

	/* Boiler-plates */
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public long getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(long buyPrice) {
		this.buyPrice = buyPrice;
	}
	public long getBuyEpoch() {
		return buyEpoch;
	}
	public void setBuyEpoch(long buyEpoch) {
		this.buyEpoch = buyEpoch;
	}
	@Override
	public String toString() {
		return "UserPortletStock [id=" + id + ", user=" + user + ", portlet="
				+ portlet + ", stock=" + stock + ", qty=" + qty + ", buyPrice="
				+ buyPrice + ", buyEpoch=" + buyEpoch + "]";
	}
}
