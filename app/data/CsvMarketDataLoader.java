package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import play.Logger;

public class CsvMarketDataLoader implements MarketDataLoader {
	public static final String FILE_PATH_DEFAULT = "/var/tmp/NASDAQ_20150417.txt";
	public static final String CSV_SPLIT_BY = ",";

	@Override
	public boolean loadToRedis(String filepath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filepath));
			String line;
			while ((line = br.readLine()) != null) {
				//symbol,date,open,high,low,close,volume[,openinterest]
				String[] split = line.split(CSV_SPLIT_BY);
				Logger.debug(
						"-symbol=" + split[0] + 
						"-date=" + split[1] + 
						"-open=" + split[2] + 
						"-high=" + split[3] + 
						"-low=" + split[4] + 
						"-close=" + split[5] + 
						"-volume=" + split[6]
						);

				String key = RedisKey.LAST_MARKET_PRICE_FLOAT + split[0];
				play.cache.Cache.set(key, split[5]);
				Logger.debug("Fetched: " + ((String) play.cache.Cache.get(key)));
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
	public static void main(String[] args) {
		CsvMarketDataLoader n = new CsvMarketDataLoader();
		n.loadToRedis(null);
	}
}
