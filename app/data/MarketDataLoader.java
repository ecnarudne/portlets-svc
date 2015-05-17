package data;

public interface MarketDataLoader {
	//boolean assignInputStream();
	boolean loadToRedis(String filepath);
}
