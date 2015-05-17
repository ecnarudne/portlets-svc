package data;

public enum RedisKey {
    SEPARATOR(":"),
    LAST_MARKET_PRICE_HASH("PxH:"),
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