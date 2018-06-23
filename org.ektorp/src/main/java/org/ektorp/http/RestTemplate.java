package org.ektorp.http;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.ektorp.util.Exceptions;
/**
 * 
 * @author Henrik Lundgren
 * 
 */
public class RestTemplate {

	private final HttpClient client;

	public RestTemplate(final HttpClient client) {
		this.client = client;
	}

	public <T> T get(final String path, final ResponseCallback<T> callback) {
		final HttpResponse hr = client.get(path);
		return handleResponse(callback, hr);
	}

	public <T> T getUncached(final String path, final ResponseCallback<T> callback) {
		final HttpResponse hr = client.getUncached(path);
		return handleResponse(callback, hr);
	}
	
	public HttpResponse get(final String path) {
		return handleRawResponse(client.get(path));
	}
	
	public HttpResponse getUncached(final String path) {
		return handleRawResponse(client.getUncached(path));
	}

	public void put(final String path) {
		handleVoidResponse(client.put(path));
	}

	public <T> T put(final String path, final ResponseCallback<T> callback) {
		return handleResponse(callback, client.put(path));
	}

	public <T> T put(final String path, final String content, final ResponseCallback<T> callback) {
		return handleResponse(callback, client.put(path, content));
	}

	public <T> T put(final String path, final HttpEntity httpEntity, final ResponseCallback<T> callback) {
		return handleResponse(callback, client.put(path, httpEntity));
	}

	public <T> T copy(final String path, final String destinationUri, final ResponseCallback<T> callback) {
		return handleResponse(callback, client.copy(path, destinationUri));
	}
	
	public void put(final String path, final String content) {
		handleVoidResponse(client.put(path, content));
	}

	public void put(final String path, final HttpEntity httpEntity) {
		handleVoidResponse(client.put(path, httpEntity));
	}

	public void put(final String path, final InputStream data, final String contentType, long contentLength) {
		handleVoidResponse(client.put(path, data, contentType, contentLength));
	}

	public <T> T put(String path, InputStream data, String contentType,
			long contentLength, ResponseCallback<T> callback) {
		return handleResponse(callback, client.put(path, data, contentType, contentLength));
	}

	public <T> T post(final String path, final String content, final ResponseCallback<T> callback) {
		return handleResponse(callback, client.post(path, content));
	}

	public <T> T post(final String path, final HttpEntity httpEntity, final ResponseCallback<T> callback) {
		return handleResponse(callback, client.post(path, httpEntity));
	}

	public <T> T post(final String path, final InputStream content, final ResponseCallback<T> callback) {
		return handleResponse(callback, client.post(path, content));
	}
	
	public <T> T postUncached(final String path, final String content, ResponseCallback<T> callback) {
		return handleResponse(callback, client.postUncached(path, content));
	}
	
	public HttpResponse post(final String path, final String content ) {
		return handleRawResponse(client.post(path,content));
	}
	
	public HttpResponse postUncached(final String path, final String content ) {
		return handleRawResponse(client.postUncached(path,content));
	}

	public <T> T delete(final String path, ResponseCallback<T> callback) {
		return handleResponse(callback, client.delete(path));
	}
	
	public void delete(final String path) {
		handleVoidResponse(client.delete(path));
	}

	public <T> T head(final String path, final ResponseCallback<T> callback) {
		return handleResponse(callback, client.head(path));
	}
	
	private void handleVoidResponse(final HttpResponse hr) {
		if (hr == null)
			return;
		try {
			if (!hr.isSuccessful()) {
				new StdResponseHandler<Void>().error(hr);
			}
		} finally {
			hr.releaseConnection();
		}
	}
	
	private <T> T handleResponse(final ResponseCallback<T> callback, final HttpResponse hr) {
		try {
			return hr.isSuccessful() ? callback.success(hr) : callback.error(hr);
		} catch (Exception e) {
			throw Exceptions.propagate(e);
		} finally {
			hr.releaseConnection();
		}
	}
	
	private HttpResponse handleRawResponse(final HttpResponse hr) {
		try {
			if (!hr.isSuccessful()) {
				throw StdResponseHandler.createDbAccessException(hr);
			}
			return hr;
		} catch (Exception e) {
			hr.releaseConnection();
			throw Exceptions.propagate(e);
		}
	}
}
