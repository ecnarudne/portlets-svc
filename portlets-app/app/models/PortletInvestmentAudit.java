package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class PortletInvestmentAudit extends Model {
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private long portletId;
	private long userId;
	private String stock;
	private long epoch;
	private double qty;
	private double price;
	private double weight;
	public static Finder<Long, PortletInvestmentAudit> find = new Finder<Long, PortletInvestmentAudit>(Long.class, PortletInvestmentAudit.class);

	public PortletInvestmentAudit(UserPortletStock userPortletStock, long epoch) {
		super();
		this.portletId = userPortletStock.getPortlet().getId();
		this.userId = userPortletStock.getUser().getId();
		this.stock = userPortletStock.getStock();
		this.qty = userPortletStock.getQty();
		this.price = userPortletStock.getBuyPrice();
		this.weight = userPortletStock.getBuyWeight();
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
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public long getEpoch() {
		return epoch;
	}
	public void setEpoch(long epoch) {
		this.epoch = epoch;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
}
