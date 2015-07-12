package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import models.Stock;
import models.StockStats;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import play.Logger;
import play.cache.Cache;

import com.google.common.io.PatternFilenameFilter;

public class CsvMarketDataLoader implements MarketDataLoader {
	public static final String MKT_FILE_PATH_DEFAULT = "/var/tmp/data/NASDAQ_20150710.csv";
	public static final String MKT_DIR_PATH_DEFAULT = "/var/tmp/data/";
	public static final String CSV_SPLIT_BY = ",";

	// @Override
	public boolean loadMarketDataHistory(String dirpath, String exchange) {
		File dir = new File(dirpath);
		File[] files = dir.listFiles(new PatternFilenameFilter(".*_.*\\.csv"));
		Logger.info(files.length + " market data files found.");
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				// Names (e.g. NASDAQ_20150710.csv) will sort by exchange and then date
				return f1.getName().compareToIgnoreCase(f2.getName());
			}
		});
		for (File file : files) {
            int indexOfDelimeter = file.getName().indexOf('_');
            int indexOfDot = file.getName().indexOf('.');
            String dateString = file.getName().substring(indexOfDelimeter + 1, indexOfDot);
            LocalDate date = LocalDate.parse(dateString, DateTimeFormat.forPattern("yyyyMMdd"));
			if(exchange == null || exchange.isEmpty()) {
				exchange = file.getName().substring(0, indexOfDelimeter);
			}
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(file));
				String line;
				while ((line = br.readLine()) != null) {
					StockStats s = formStockStatsFromCSV(exchange, date, line);
					int count = StockStats.find.where()
							.ieq("exchange", exchange)
							.ieq("date", s.getDate())
							.eq("stock_id", s.getStock().getId()).findRowCount();
					if(count == 0)
						s.save();
					updateMarketPriceCache(s);
				}
				Logger.debug("Successful Import " + file.getAbsolutePath());
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
		}
		Logger.debug("Done Import " + dirpath);
		return true;
	}

	@Override
	public boolean loadMarketDataFile(String filepath, String exchange, String dateString) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filepath));
			String line;
			while ((line = br.readLine()) != null) {
	            LocalDate date = LocalDate.parse(dateString, DateTimeFormat.forPattern("yyyyMMdd"));
				StockStats s = formStockStatsFromCSV(exchange, date, line);
				int count = StockStats.find.where()
						.ieq("exchange", exchange)
						.ieq("date", s.getDate())
						.eq("stock_id", s.getStock().getId()).findRowCount();
				if(count < 1)
					s.save();
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
		String lastPxKey = RedisKey.LAST_MARKET_PRICE_FLOAT
				+ s.getStock().getSymbol();
		Cache.set(lastPxKey, s.getClosePrice());
		if (Logger.isDebugEnabled())
			Logger.debug("Fetched Price: " + ((String) Cache.get(lastPxKey))
					+ " for: " + s.getStock().getSymbol());

		String pxStatsKey = RedisKey.LAST_MARKET_PRICE_STATS
				+ s.getStock().getSymbol();
		Cache.set(pxStatsKey, s);
		if (Logger.isDebugEnabled()) {
			Logger.debug("Fetched Stats: "
					+ ((StockStats) Cache.get(pxStatsKey)) + " for: "
					+ s.getStock().getSymbol());
			Logger.debug("Fetched Stock: "
					+ ((StockStats) Cache.get(pxStatsKey)).getStock()
					+ " for: " + s.getStock().getSymbol());
		}
	}

	public static StockStats loadStockStatsBySymbol(String symbol) {
		String pxStatsKey = RedisKey.LAST_MARKET_PRICE_STATS + symbol;
		StockStats s = (StockStats) Cache.get(pxStatsKey);
		if(s == null) {
			s = StockStats.findLatestBySymbol(symbol);
		}
		return s;
	}

	public static StockStats formStockStatsFromCSV(String exchange, LocalDate localDate, String csv) {
		String[] split = csv.split(CSV_SPLIT_BY);
		// CSV Columns Assumed:
		// symbol,date,open,high,low,close,volume[,openinterest]
		Logger.debug("-symbol=" + split[0] + "-date=" + split[1] + "-open="
				+ split[2] + "-high=" + split[3] + "-low=" + split[4]
				+ "-close=" + split[5] + "-volume=" + split[6]);
		String symbol = split[0];
		StockStats stats = new StockStats();
		Stock stock = Stock.findBySymbol(symbol);
		// Add to Stocks static data, till we don't have a better source
		if (stock == null)
			stock = addMissingStock(symbol, stats, stock);
		stats.setExchange(exchange);
		stats.setStock(stock);
		if(localDate != null) {
			stats.setLocalDate(localDate);
		} else {
	        try {
				stats.setLocalDate(LocalDate.parse(split[1], DateTimeFormat.forPattern("yyyy-MMM-dd")));
			} catch (Exception e) {
				Logger.warn("Failed to parse date: " + split[1]);
			}
		}
		/*
		 * stats.setClosePrice(parseWithDefault(split[5], 0));
		 * stats.setOpenPrice(parseWithDefault(split[2], 0));
		 * stats.setHighPrice(parseWithDefault(split[3], 0));
		 * stats.setLowPrice(parseWithDefault(split[4], 0));
		 * stats.setAvgVol(parseWithDefault(split[6], 0));
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
	 * 
	 * @return
	 */
	public static Stock addMissingStock(String symbol, StockStats stats,
			Stock stock) {
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
