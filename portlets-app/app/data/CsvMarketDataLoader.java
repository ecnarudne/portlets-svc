package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import models.Stock;
import models.StockStats;
import play.Logger;
import play.cache.Cache;

public class CsvMarketDataLoader implements MarketDataLoader {
	public static final String FILE_PATH_DEFAULT = "/var/tmp/NASDAQ_20150417.txt";
	public static final String CSV_SPLIT_BY = ",";

	@Override
	public boolean loadToRedis(String filepath, String exchange, String date) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filepath));
			String line;
			while ((line = br.readLine()) != null) {
				StockStats s = formStockStatsFromCSV(line);
				updateMarketPriceCache(s);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Logger.debug("Done Import " + filepath);
		return true;
	}
	private void updateMarketPriceCache(StockStats s) {
		String lastPxKey = RedisKey.LAST_MARKET_PRICE_FLOAT + s.getStock().getSymbol();
		Cache.set(lastPxKey, s.getClosePrice());
		if(Logger.isDebugEnabled())
			Logger.debug("Fetched Price: " + ((String) Cache.get(lastPxKey)) + " for: " + s.getStock().getSymbol());

		String pxStatsKey = RedisKey.LAST_MARKET_PRICE_STATS + s.getStock().getSymbol();
		Cache.set(pxStatsKey, s);
		if(Logger.isDebugEnabled()) {
			Logger.debug("Fetched Stats: " + ((StockStats) Cache.get(pxStatsKey)) + " for: " + s.getStock().getSymbol());
			Logger.debug("Fetched Stock: " + ((StockStats) Cache.get(pxStatsKey)).getStock() + " for: " + s.getStock().getSymbol());
		}
	}
	public static StockStats loadStockStatsBySymbol(String symbol) {
	    	String pxStatsKey = RedisKey.LAST_MARKET_PRICE_STATS + symbol;
	    	StockStats s = (StockStats) Cache.get(pxStatsKey);
			return s;
	}
	public static StockStats formStockStatsFromCSV(String csv) {
		String[] split = csv.split(CSV_SPLIT_BY);
		//CSV Columns Assumed: symbol,date,open,high,low,close,volume[,openinterest]
		Logger.debug(
				"-symbol=" + split[0] + 
				"-date=" + split[1] + 
				"-open=" + split[2] + 
				"-high=" + split[3] + 
				"-low=" + split[4] + 
				"-close=" + split[5] + 
				"-volume=" + split[6]
		);
		String symbol = split[0];
		StockStats stats = new StockStats();
		Stock stock = Stock.findBySymbol(symbol);
		//Add to Stocks static data, till we don't have a better source
		if(stock == null)
			stock = addMissingStock(symbol, stats, stock);
		stats.setStock(stock);
		/*		
		stats.setClosePrice(parseWithDefault(split[5], 0));
		stats.setOpenPrice(parseWithDefault(split[2], 0));
		stats.setHighPrice(parseWithDefault(split[3], 0));
		stats.setLowPrice(parseWithDefault(split[4], 0));
		stats.setAvgVol(parseWithDefault(split[6], 0));
		*/
		stats.setClosePrice(split[5]);
		stats.setOpenPrice(split[2]);
		stats.setHighPrice(split[3]);
		stats.setLowPrice(split[4]);
		stats.setAvgVol(split[6]);
		stats.setDate(split[1]);
		return stats;
	}
	/**
	 * A workaround till we have static data for securities
	 * @return 
	 */
	public static Stock addMissingStock(String symbol, StockStats stats, Stock stock) {
		Stock s = new Stock(symbol);
		s.setName(symbol);
		s.save();
		Logger.info("Added to Stocks: " + s);
		return Stock.findBySymbol(symbol);
	}
	static double parseWithDefault(String s, double defaultNumber) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			Logger.warn("Failed to parse double: " + s);
			return defaultNumber;
		}
	}
}
