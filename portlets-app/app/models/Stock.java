package models;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Stock extends Model {

	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	@Required
	private String symbol;
	private String name;
	private String primaryExchange = Exchange.NASDAQ;
	private String type = "Equity";
	private Set<String> labels;

	public Stock() {}
	public Stock(String symbol) {
		super();
		this.symbol = symbol;
	}

	public static Finder<Long, Stock> find = new Finder<Long, Stock>(Long.class, Stock.class);

	public static Stock findBySymbol(String symbol) {
		//TODO must cache
		if(symbol == null)
			return null;
		List<Stock> list = find.where().eq("symbol", symbol).findList();
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}
	public static List<Stock> findByExchange(String primaryExchange) {
		if(primaryExchange == null)
			return null;
		List<Stock> list = find.where().eq("primaryExchange", primaryExchange).findList();
		return list;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrimaryExchange() {
		return primaryExchange;
	}
	public void setPrimaryExchange(String primaryExchange) {
		this.primaryExchange = primaryExchange;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Set<String> getLabels() {
		return labels;
	}
	public void setLabels(Set<String> labels) {
		this.labels = labels;
	}
	@Override
	public String toString() {
		return "Stock [id=" + id + ", symbol=" + symbol + ", name=" + name
				+ ", primaryExchange=" + primaryExchange + ", type=" + type
				+ ", labels=" + labels + "]";
	}
}
