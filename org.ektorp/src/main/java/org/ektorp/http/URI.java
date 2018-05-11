package org.ektorp.http;

import java.io.*;
import java.net.*;
import java.util.Map;

import org.ektorp.util.*;

public class URI {

	private final StringBuilder path;
	private final boolean prototype;
	private StringBuilder params;
	private String uri;
	
	private URI(final String path) {
		this.path = new StringBuilder(path);
		prototype = false;
	}
	
	private URI(final String path, final boolean prototype) {
		this.path = new StringBuilder(path);
		this.prototype = prototype;
	}
	
	private URI(final StringBuilder path, final StringBuilder params) {
		this.path = path;
		this.params = params;
		prototype = false;
	}
	
	private URI(final StringBuilder path) {
		this(path, null);
	}
	
	public static URI of(final String path) {
		return new URI(path);
	}
	
	public static URI prototype(final String path) {
		return new URI(path, true);
	}

	public URI copy() {
		return params != null ? new URI(new StringBuilder(path), new StringBuilder(params)) : new URI(new StringBuilder(path));
	}
	
	public URI append(String pathElement) {
		if (prototype) {
			return copy().append(pathElement);
		}
		if (path.charAt(path.length()-1) != '/') {
			path.append("/");	
		}
		try {
			if (!pathElement.startsWith("_")) {
				pathElement = URLEncoder.encode(pathElement, "UTF-8"); 
			}
			path.append(pathElement);
		} catch (UnsupportedEncodingException e) {
			throw Exceptions.propagate(e);
		}
		uri = null;
		return this;
	}
	
	public URI param( String name, String value) {
		if (prototype) {
			return copy().param(name, value);
		}
		if (params != null) {
			params().append("&");
		} else {
			params().append("?");
		}
		try {
			params().append(name).append("=").append(URLEncoder.encode(value, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw Exceptions.propagate(e);
		}
		uri = null;
		return this;
	}

	public URI param(final String name, final int value) {
		return param(name, Integer.toString(value));
	}
	
	public URI param(final String name, final long value) {
		return param(name, Long.toString(value));
	}
	
	private StringBuilder params() {
		if (params == null) {
			params = new StringBuilder();
		}
		return params;
	}
	
	@Override
	public String toString() {
		if (uri == null) {
			uri = params != null ? path.append(params).toString() : path.toString(); 
		}
		return uri;
	}

	public void params(Map<String, String> params) { 
		for (Map.Entry<String, String> e : params.entrySet()) {
			this.param(e.getKey(), e.getValue());
		}
	}
}
