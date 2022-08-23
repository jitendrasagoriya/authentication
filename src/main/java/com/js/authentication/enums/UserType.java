package com.js.authentication.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserType {

	SUPERADMIN(2001, "SUPERADMIN"), APPADMIN(2002, "APPADMIN"), APPUSER(2003, "APPUSER");

	private static final Map<Integer, UserType> byId = new HashMap<>();
	private static final Map<String, UserType> byValue = new HashMap<>();

	static {
		for (UserType e : UserType.values()) {
			if (byId.put(e.getId(), e) != null) {
				throw new IllegalArgumentException("duplicate id: " + e.getId());
			}

			if (byValue.put(e.getValue(), e) != null) {
				throw new IllegalArgumentException("duplicate value: " + e.getValue());
			}
		}
	}

	public static UserType getById(int id) {
		return byId.get(id);
	}

	public static UserType getByValue(String value) {
		return byValue.get(value);
	}

	private int id;

	private String value;

	private UserType(int id, String value) {
		this.id = id;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

}
