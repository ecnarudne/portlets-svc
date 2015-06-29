package models.api;

public enum VolatilityClass {
	LOW("Low"), MEDIUM("Medium"), HIGH("High");

	private String displayName;

	private VolatilityClass(String d) {
		displayName = d;
	}

	public String getDisplayName() {
		return displayName;
	}
}
