package models;

import java.io.Serializable;


public class StockStats implements Serializable {
	private static final long serialVersionUID = 1L;
	private Stock stock;
/*	private double openPrice;
	private double closePrice;
	private double highPrice;
	private double lowPrice;
	private double volume;
	private double avgVol;
	private double mktcap;
*/
	private String openPrice;
	private String closePrice;
	private String highPrice;
	private String lowPrice;
	private String volume;
	private String avgVol;
	private String mktcap;
	private String date;
	private String activity;

	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	/**
	 * Format: YYYYMMDD
	 */
	public String getDate() {
		return date;
	}
	/**
	 * Format: YYYYMMDD
	 */
	public void setDate(String date) {
		this.date = date;
	}
	public String getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(String openPrice) {
		this.openPrice = openPrice;
	}
	public String getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(String closePrice) {
		this.closePrice = closePrice;
	}
	public String getHighPrice() {
		return highPrice;
	}
	public void setHighPrice(String highPrice) {
		this.highPrice = highPrice;
	}
	public String getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getAvgVol() {
		return avgVol;
	}
	public void setAvgVol(String avgVol) {
		this.avgVol = avgVol;
	}
	public String getMktcap() {
		return mktcap;
	}
	public void setMktcap(String mktcap) {
		this.mktcap = mktcap;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
}
