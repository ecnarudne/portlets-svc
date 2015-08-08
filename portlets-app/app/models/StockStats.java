package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.joda.time.LocalDate;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class StockStats extends Model {
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	@Required
	private String exchange;
	@ManyToOne
	private Stock stock;
	//private String date;
	private LocalDate localDate;

	private double openPrice;
	private double closePrice;
	private double highPrice;
	private double lowPrice;
	private double avgVol;
	private double mktcap;
	private String activity;

	public static Finder<Long, StockStats> find = new Finder<Long, StockStats>(Long.class, StockStats.class);

	public static NavigableMap<LocalDate, Map<Long, StockStats>> buildDateMapByStockIds(Collection<Long> stockIds) {
		//TODO must cache
		TreeMap<LocalDate, Map<Long, StockStats>> dateMap = new TreeMap<LocalDate, Map<Long, StockStats>>();
		List<StockStats> list = StockStats.find.where().in("stock_id", stockIds).findList();
		for (StockStats s : list) {
			if(dateMap.containsKey(s.getLocalDate())) {
				dateMap.get(s.getLocalDate()).put(s.getStock().getId(), s);
			} else {
				Map<Long, StockStats> statsMap = new HashMap<Long, StockStats>();
				statsMap.put(s.getStock().getId(), s);
				dateMap.put(s.getLocalDate(), statsMap);
			}
		}
		return dateMap;
	}

	public static List<StockStats> findByStocks(List<Stock> stocks) {
		List<Long> ids = new ArrayList<Long>();
		for (Stock s : stocks) {
			ids.add(s.getId());
		}
		return findByStockIds(ids);
	}

	public static List<StockStats> findByStockIds(List<Long> stockIds) {
		return StockStats.find.where().in("stock_id", stockIds).orderBy("local_date").findList();
	}

	public static List<StockStats> findByStock(Stock stock) {
		return StockStats.find.where().eq("stock_id", stock.getId()).orderBy("local_date").findList();
	}

	public static StockStats findLatestByStock(Stock stock) {
		return StockStats.find.where().eq("stock_id", stock.getId()).orderBy("local_date").setMaxRows(1).findUnique();
	}

	public static List<StockStats> findBySymbol(String symbol) {
		return findByStock(Stock.findBySymbol(symbol));
	}

	public static StockStats findLatestBySymbol(String symbol) {
		return findLatestByStock(Stock.findBySymbol(symbol));
	}

	public static StockStats findStockStatsOnDate(Stock stock, LocalDate onDate) {
		return StockStats.find.where().eq("stock_id", stock.getId()).le("local_date", onDate).orderBy("local_date").setMaxRows(1).findUnique();
	}

	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public LocalDate getLocalDate() {
		return localDate;
	}
	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}
	public double getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}
	public double getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}
	public double getHighPrice() {
		return highPrice;
	}
	public void setHighPrice(double highPrice) {
		this.highPrice = highPrice;
	}
	public double getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(double lowPrice) {
		this.lowPrice = lowPrice;
	}
	public double getAvgVol() {
		return avgVol;
	}
	public void setAvgVol(double avgVol) {
		this.avgVol = avgVol;
	}
	public double getMktcap() {
		return mktcap;
	}
	public void setMktcap(double mktcap) {
		this.mktcap = mktcap;
	}
}
