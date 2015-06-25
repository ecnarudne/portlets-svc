package data;

public enum RedisKey {
    SEPARATOR(":"),
    LAST_MARKET_PRICE_STATS("PxS:"),
    LAST_MARKET_PRICE_STATS_JSON("PSJ:"),
    LAST_MARKET_PRICE_FLOAT("PxF:")
    ;

	private final String shortName;

	private RedisKey(final String text) {
		this.shortName = text;
	}

	@Override
	public String toString() {
		return shortName;
	}
}