package data;

public interface MarketDataLoader {
	//boolean assignInputStream();
	boolean loadMarketDataFile(String filepath, String exchange, String date);
}
