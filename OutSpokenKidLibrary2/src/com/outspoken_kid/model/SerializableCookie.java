package com.outspoken_kid.model;

import java.io.Serializable;

import org.apache.http.impl.cookie.BasicClientCookie;

public class SerializableCookie extends BasicClientCookie implements Serializable {

	private static final long serialVersionUID = -6235197009762156627L;

	public SerializableCookie(String name, String value) {
		super(name, value);
		// TODO Auto-generated constructor stub
	}
}
