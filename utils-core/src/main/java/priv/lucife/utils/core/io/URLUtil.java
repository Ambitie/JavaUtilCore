/**
 * Copyright (C) 2017 Lucifer Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package priv.lucife.utils.core.io;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import priv.lucife.utils.core.annotation.UBTCompatible;
import priv.lucife.utils.core.base.StringUtil;
import priv.lucife.utils.core.base.ValidatorUtil;
import priv.lucife.utils.core.date.DateUtil;
import priv.lucife.utils.core.file.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Useful utilities for working with UrlConnections and IO.
 *
 * Anyone using the deprecated api calls for proxying to urls should update to
 * use the new suggested calls. To let the jvm proxy for you automatically, use
 * the following -D parameters:
 *
 * http.proxyHost http.proxyPort (default: 80) http.nonProxyHosts (should always
 * include localhost) https.proxyHost https.proxyPort
 *
 * Example: -Dhttp.proxyHost=proxy.example.org -Dhttp.proxyPort=8080
 * -Dhttps.proxyHost=proxy.example.org -Dhttps.proxyPort=8080
 * -Dhttp.nonProxyHosts=*.foo.com|localhost|*.td.afg
 *
 * @author Lucifer Wong
 */
@UBTCompatible
public abstract class URLUtil {

	public static final String COOKIE = "Cookie";
	public static final String COOKIE_VALUE_DELIMITER = ";";
	public static final char DOT = '.';
	public static final String EXPIRES = "expires";
	public static final TrustManager[] NAIVE_TRUST_MANAGER = new TrustManager[] { new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	} };

	public static final HostnameVerifier NAIVE_VERIFIER = new HostnameVerifier() {
		public boolean verify(String s, SSLSession sslSession) {
			return true;
		}
	};

	public static final char NAME_VALUE_SEPARATOR = '=';

	public static final String PATH = "path";

	public static final ThreadLocal<String> referrer = new ThreadLocal<>();

	public static final String SET_COOKIE = "Set-Cookie";

	public static final String SET_COOKIE_SEPARATOR = "; ";

	public static final ThreadLocal<String> userAgent = new ThreadLocal<>();

	private static final char AND_SIGN = '&';

	private static int BUFFER_SIZE = 1024 * 4; // 4K

	private static String charSet = "GBK";

	private static final char EQUALS_SIGN = '=';

	private static String globalReferrer = null;

	private static String globalUserAgent = null;

	private static final char POINT_SIGN = '.';

	private static final char QUESTION_MARK = '?';

	private static final Pattern resPattern = Pattern.compile("^res\\:\\/\\/", Pattern.CASE_INSENSITIVE);

	private static final char SEPARATOR_SIGN = '/';

	protected static SSLSocketFactory naiveSSLSocketFactory;

	static {
		try {
			// Default new HTTP connections to follow redirects
			HttpURLConnection.setFollowRedirects(true);
		} catch (Exception ignored) {
		}

		try {
			// could be other algorithms (prob need to calculate this another
			// way.
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, NAIVE_TRUST_MANAGER, new SecureRandom());
			naiveSSLSocketFactory = sslContext.getSocketFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拼接URL，参数从obj中通过反射取得
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL
	 * @param obj
	 *            对象
	 * @param names
	 *            参数名数组
	 * @return 拼接后的URL
	 */
	@SuppressWarnings("rawtypes")
	public static String addInnerQueryString(String url, Object obj, String[] names) {

		Class objClass = obj.getClass();

		Object[] values = new Object[names.length];
		for (int i = 0; i < names.length; i++) {
			try {
				Field field = objClass.getDeclaredField(names[i]);
				field.setAccessible(true);
				values[i] = field.get(obj);
			} catch (Exception e) {
				// Ignore
			}
		}

		return addQueryString(url, names, values);
	}

	/**
	 * 拼接URL
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL
	 * @param queryString
	 *            查询字符串，比如：id=1
	 * @return 拼接后的URL
	 */
	public static String addQueryString(String url, String queryString) {
		if (ValidatorUtil.isEmpty(queryString)) {
			return url;
		}

		if (url.indexOf(QUESTION_MARK) == -1) {
			url = url + QUESTION_MARK + queryString;
		} else {
			url = url + AND_SIGN + queryString;
		}
		return url;
	}

	/**
	 * 拼接URL
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL
	 * @param name
	 *            参数的名称
	 * @param value
	 *            参数的值
	 * @return 拼接后的URL
	 */
	public static String addQueryString(String url, String name, Object value) {
		return addQueryString(url, new String[] { name }, new Object[] { value });
	}

	/**
	 * 拼接 URL。
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL
	 * @param names
	 *            参数的名称数组
	 * @param values
	 *            参数的值数组
	 * @return 拼接后的URL
	 */
	public static String addQueryString(String url, String[] names, Object[] values) {
		if (names.length != values.length) {
			throw new IllegalArgumentException("Length of array must be equal");
		}

		StringBuilder queryString = new StringBuilder();
		boolean isFirst = true;
		for (int i = 0; i < names.length; i++) {
			Object value = values[i];
			if (value != null) {
				if (!isFirst) {
					queryString.append(AND_SIGN);
				} else {
					isFirst = false;
				}

				if (value instanceof Object[]) {
					Object[] array = (Object[]) value;
					for (int j = 0; j < array.length; j++) {
						if (j > 0) {
							queryString.append(AND_SIGN);
						}
						appendParameter(queryString, names[i], array[j]);
					}
				} else if (value instanceof Collection<?>) {
					int j = 0;
					Collection<?> clc = (Collection<?>) value;
					Iterator<?> iterator = clc.iterator();
					while (iterator.hasNext()) {
						if (j++ > 0) {
							queryString.append(AND_SIGN);
						}
						appendParameter(queryString, names[i], iterator.next());
					}
				} else {
					appendParameter(queryString, names[i], value);
				}
			}
		}

		return addQueryString(url, queryString.toString());
	}

	public static void clearGlobalReferrer() {
		globalReferrer = null;
	}

	public static void clearGlobalUserAgent() {
		globalUserAgent = null;
	}

	/**
	 * 对 url 按照指定编码方式解码。
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            url
	 * @param encoding
	 *            解码格式
	 * @return 解码后的字符串
	 */
	public static String decode(String url, String encoding) {
		try {
			return URLDecoder.decode(url, encoding);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static void disconnect(HttpURLConnection c) {
		if (c != null) {
			try {
				c.disconnect();
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * 使 URL 成为动态 URL，如果没有问号就在最后添加问号。
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL
	 * @return 动态 URL
	 */
	public static String dynamicURL(String url) {
		if (url.indexOf(QUESTION_MARK) == -1) {
			url = url + QUESTION_MARK;
		}
		return url;
	}

	/**
	 * 对 url 按照指定编码方式编码。
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL
	 * @param encoding
	 *            编码格式
	 * @return 编码后的字符串
	 */
	public static String encode(String url, String encoding) {
		try {
			return URLEncoder.encode(url, encoding);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 通过servletPath取得action的名称
	 * 
	 * @author Lucifer Wong
	 * @param servletPath
	 *            servletPath
	 * @return action的名称
	 */
	public static String getActionName(String servletPath) {
		return servletPath.substring(servletPath.lastIndexOf(SEPARATOR_SIGN) + 1, servletPath.lastIndexOf(POINT_SIGN));
	}

	public static URL getActualUrl(String url) throws MalformedURLException {
		Matcher m = resPattern.matcher(url);
		return m.find() ? URLUtil.class.getClassLoader().getResource(url.substring(m.end())) : new URL(url);
	}

	/**
	 * @author Lucifer Wong
	 * @param url
	 *            url
	 * @param input
	 *            boolean indicating whether this connection will be used for
	 *            input
	 * @param output
	 *            boolean indicating whether this connection will be used for
	 *            output
	 * @param cache
	 *            boolean allow caching (be careful setting this to true for
	 *            non-static retrievals).
	 * @return URLConnection established URL connection.
	 * @throws IOException
	 *             io异常
	 */
	public static URLConnection getConnection(String url, boolean input, boolean output, boolean cache)
			throws IOException {
		return getConnection(getActualUrl(url), null, input, output, cache, true);
	}

	/**
	 * @author Lucifer Wong
	 * @param url
	 *            url
	 * @param input
	 *            boolean indicating whether this connection will be used for
	 *            input
	 * @param output
	 *            boolean indicating whether this connection will be used for
	 *            output
	 * @param cache
	 *            boolean allow caching (be careful setting this to true for
	 *            non-static retrievals).
	 * @return URLConnection established URL connection.
	 * @throws IOException
	 *             io异常
	 */
	public static URLConnection getConnection(URL url, boolean input, boolean output, boolean cache)
			throws IOException {
		return getConnection(url, null, input, output, cache, true);
	}

	/**
	 * Gets a connection from a url. All getConnection calls should go through
	 * this code.
	 * 
	 * @author Lucifer Wong
	 * @param inCookies
	 *            Supply cookie Map (received from prior setCookies calls from
	 *            server)
	 * @param input
	 *            boolean indicating whether this connection will be used for
	 *            input
	 * @param output
	 *            boolean indicating whether this connection will be used for
	 *            output
	 * @param cache
	 *            boolean allow caching (be careful setting this to true for
	 *            non-static retrievals).
	 * @return URLConnection established URL connection.
	 * @param url
	 *            url
	 * @param allowAllCerts
	 *            allowAllCerts
	 * @throws IOException
	 *             io异常
	 */
	public static URLConnection getConnection(URL url, Map<String, Map<?, ?>> inCookies, boolean input, boolean output,
			boolean cache, boolean allowAllCerts) throws IOException {
		URLConnection c = url.openConnection();
		c.setRequestProperty("Accept-Encoding", "gzip, deflate");
		c.setAllowUserInteraction(false);
		c.setDoOutput(output);
		c.setDoInput(input);
		c.setUseCaches(cache);
		c.setReadTimeout(220000);
		c.setConnectTimeout(45000);

		String ref = getReferrer();
		if (!ValidatorUtil.isEmpty(ref)) {
			c.setRequestProperty("Referer", ref);
		}
		String agent = getUserAgent();
		if (!ValidatorUtil.isEmpty(agent)) {
			c.setRequestProperty("User-Agent", agent);
		}

		if (c instanceof HttpURLConnection) { // setFollowRedirects is a static
												// (global) method / setting -
												// resetting it in case other
												// code changed it?
			HttpURLConnection.setFollowRedirects(true);
		}

		if (c instanceof HttpsURLConnection && allowAllCerts) {
			try {
				setNaiveSSLSocketFactory((HttpsURLConnection) c);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Set cookies in the HTTP header
		if (inCookies != null) { // [optional] place cookies (JSESSIONID) into
									// HTTP headers
			setCookies(c, inCookies);
		}
		return c;
	}

	/**
	 * Get content from the passed in URL. This code will open a connection to
	 * the passed in server, fetch the requested content, and return it as a
	 * byte[].
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL to hit
	 * @return byte[] read from URL or null in the case of error.
	 */
	public static byte[] getContentFromUrl(String url) {
		return getContentFromUrl(url, null, null, true);
	}

	/**
	 * Get content from the passed in URL. This code will open a connection to
	 * the passed in server, fetch the requested content, and return it as a
	 * byte[].
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL to hit
	 * @param inCookies
	 *            Map of session cookies (or null if not needed)
	 * @param outCookies
	 *            Map of session cookies (or null if not needed)
	 * @return byte[] of content fetched from URL.
	 */
	public static byte[] getContentFromUrl(String url, Map<String, Map<?, ?>> inCookies,
			Map<String, Map<?, ?>> outCookies) {
		return getContentFromUrl(url, inCookies, outCookies, true);
	}

	/**
	 * Get content from the passed in URL. This code will open a connection to
	 * the passed in server, fetch the requested content, and return it as a
	 * byte[].
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL to hit
	 * 
	 * @param inCookies
	 *            Map of session cookies (or null if not needed)
	 * 
	 * @param outCookies
	 *            Map of session cookies (or null if not needed)
	 * 
	 * @param allowAllCerts
	 *            override certificate validation?
	 * 
	 * @return byte[] of content fetched from URL.
	 */
	public static byte[] getContentFromUrl(String url, Map<String, Map<?, ?>> inCookies,
			Map<String, Map<?, ?>> outCookies, boolean allowAllCerts) {
		try {
			return getContentFromUrl(getActualUrl(url), inCookies, outCookies, allowAllCerts);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get content from the passed in URL. This code will open a connection to
	 * the passed in server, fetch the requested content, and return it as a
	 * byte[].
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL to hit
	 * @return byte[] read from URL or null in the case of error.
	 * @param allowAllCerts allowAllCerts
	 */
	public static byte[] getContentFromUrl(URL url, boolean allowAllCerts) {
		return getContentFromUrl(url, null, null, allowAllCerts);
	}

	/**
	 * Get content from the passed in URL. This code will open a connection to
	 * the passed in server, fetch the requested content, and return it as a
	 * byte[].
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL to hit
	 * @param inCookies
	 *            Map of session cookies (or null if not needed)
	 * @param outCookies
	 *            Map of session cookies (or null if not needed)
	 * @param allowAllCerts
	 *            override certificate validation?
	 * @return byte[] of content fetched from URL.
	 */
	public static byte[] getContentFromUrl(URL url, Map<String, Map<?, ?>> inCookies, Map<String, Map<?, ?>> outCookies,
			boolean allowAllCerts) {
		URLConnection c = null;
		try {
			c = getConnection(url, inCookies, true, false, false, allowAllCerts);

			ByteArrayOutputStream out = new ByteArrayOutputStream(16384);
			InputStream stream = IOUtil.getInputStream(c);
			IOUtil.transfer(stream, out);
			stream.close();

			if (outCookies != null) { // [optional] Fetch cookies from server
										// and update outCookie Map (pick up
										// JSESSIONID, other headers)
				getCookies(c, outCookies);
			}

			return out.toByteArray();
		} catch (Exception e) {
			readErrorResponse(c);
			return null;
		} finally {
			if (c instanceof HttpURLConnection) {
				disconnect((HttpURLConnection) c);
			}
		}
	}

	/**
	 * Get content from the passed in URL. This code will open a connection to
	 * the passed in server, fetch the requested content, and return it as a
	 * String.
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL to hit
	 * @return UTF-8 String read from URL or null in the case of error.
	 */
	public static String getContentFromUrlAsString(String url) {
		return getContentFromUrlAsString(url, null, null, true);
	}

	/**
	 * Get content from the passed in URL. This code will open a connection to
	 * the passed in server, fetch the requested content, and return it as a
	 * String.
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL to hit
	 * @param inCookies
	 *            Map of session cookies (or null if not needed)
	 * @param outCookies
	 *            Map of session cookies (or null if not needed)
	 * @param trustAllCerts
	 *            if true, SSL connection will always be trusted.
	 * @return String of content fetched from URL.
	 */
	public static String getContentFromUrlAsString(String url, Map<String, Map<?, ?>> inCookies,
			Map<String, Map<?, ?>> outCookies, boolean trustAllCerts) {
		byte[] bytes = getContentFromUrl(url, inCookies, outCookies, trustAllCerts);
		try {
			return bytes == null ? null : bytes == null ? null : new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get content from the passed in URL. This code will open a connection to
	 * the passed in server, fetch the requested content, and return it as a
	 * String.
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL to hit
	 * @param allowAllCerts
	 *            true to not verify certificates
	 * @return UTF-8 String read from URL or null in the case of error.
	 */
	public static String getContentFromUrlAsString(URL url, boolean allowAllCerts) {
		return getContentFromUrlAsString(url, null, null, allowAllCerts);
	}

	/**
	 * Get content from the passed in URL. This code will open a connection to
	 * the passed in server, fetch the requested content, and return it as a
	 * String.
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL to hit
	 * @param inCookies
	 *            Map of session cookies (or null if not needed)
	 * @param outCookies
	 *            Map of session cookies (or null if not needed)
	 * @param trustAllCerts
	 *            if true, SSL connection will always be trusted.
	 * @return String of content fetched from URL.
	 */
	public static String getContentFromUrlAsString(URL url, Map<String, Map<?, ?>> inCookies,
			Map<String, Map<?, ?>> outCookies, boolean trustAllCerts) {
		byte[] bytes = getContentFromUrl(url, inCookies, outCookies, trustAllCerts);
		try {
			return bytes == null ? null : bytes == null ? null : new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getCookieDomainFromHost(String host) {
		while (host.indexOf(DOT) != host.lastIndexOf(DOT)) {
			host = host.substring(host.indexOf(DOT) + 1);
		}
		return host;
	}

	/**
	 * Retrieves and stores cookies returned by the host on the other side of
	 * the open java.net.URLConnection.
	 * <p>
	 * The connection MUST have been opened using the connect() method or a
	 * IOException will be thrown.
	 * 
	 * @author Lucifer Wong
	 * @param conn
	 *            a java.net.URLConnection - must be open, or IOException will
	 *            be thrown
	 * @param store
	 *            store
	 */
	@SuppressWarnings("unchecked")
	public static void getCookies(URLConnection conn, Map<String, Map<?, ?>> store) {
		// let's determine the domain from where these cookies are being sent
		String domain = getCookieDomainFromHost(conn.getURL().getHost());
		Map<String, ConcurrentHashMap<?, ?>> domainStore; // this is where we
															// will store
															// cookies for this
		// domain

		// now let's check the store to see if we have an entry for this domain
		if (store.containsKey(domain)) {
			// we do, so lets retrieve it from the store
			domainStore = (Map<String, ConcurrentHashMap<?, ?>>) store.get(domain);
		} else {
			// we don't, so let's create it and put it in the store
			domainStore = new ConcurrentHashMap<>();
			store.put(domain, domainStore);
		}

		if (domainStore.containsKey("JSESSIONID")) {
			// No need to continually get the JSESSIONID (and set-cookies
			// header) as this does not change throughout the session.
			return;
		}

		// OK, now we are ready to get the cookies out of the URLConnection
		String headerName;
		for (int i = 1; (headerName = conn.getHeaderFieldKey(i)) != null; i++) {
			if (headerName.equalsIgnoreCase(SET_COOKIE)) {
				ConcurrentHashMap<String, String> cookie = new ConcurrentHashMap<>();
				StringTokenizer st = new StringTokenizer(conn.getHeaderField(i), COOKIE_VALUE_DELIMITER);

				// the specification dictates that the first name/value pair
				// in the string is the cookie name and value, so let's handle
				// them as a special case:

				if (st.hasMoreTokens()) {
					String token = st.nextToken();
					String key = token.substring(0, token.indexOf(NAME_VALUE_SEPARATOR)).trim();
					String value = token.substring(token.indexOf(NAME_VALUE_SEPARATOR) + 1);
					domainStore.put(key, cookie);
					cookie.put(key, value);
				}

				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					int pos = token.indexOf(NAME_VALUE_SEPARATOR);
					if (pos != -1) {
						String key = token.substring(0, pos).toLowerCase().trim();
						String value = token.substring(token.indexOf(NAME_VALUE_SEPARATOR) + 1);
						cookie.put(key, value);
					}
				}
			}
		}
	}

	/**
	 * 取得url的后缀名
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL
	 * @return url的后缀名
	 */
	public static String getExtension(String url) {
		int pointIndex = url.indexOf(POINT_SIGN);

		if (pointIndex == -1) {
			return null;
		}

		int interrogationIndex = url.indexOf(QUESTION_MARK);

		return interrogationIndex == -1 ? url.substring(pointIndex + 1)
				: url.substring(pointIndex + 1, interrogationIndex);
	}

	/**
	 * 通过servletPath获得namespace
	 * 
	 * @author Lucifer Wong
	 * @param servletPath
	 *            servletPath
	 * @return namespace
	 */
	public static String getNamespace(String servletPath) {
		return servletPath.substring(0, servletPath.lastIndexOf(SEPARATOR_SIGN));
	}

	/**
	 * 从URL中分析字符串参数，放到一个 map 里。
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            URL
	 * @return map，存放的都是字符串的键值对
	 */
	public static Map<String, String> getParameters(String url) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		if (ValidatorUtil.isEmpty(url)) {
			return parameters;
		}

		int questionMarkIndex = url.indexOf(QUESTION_MARK);
		if (questionMarkIndex == -1 || questionMarkIndex == url.length() - 1) {
			return parameters;
		}

		String queryString = url.substring(questionMarkIndex + 1);
		String[] paramArray = queryString.split(String.valueOf(AND_SIGN));

		for (int i = 0; i < paramArray.length; i++) {
			int equalsSignIndex = paramArray[i].indexOf(EQUALS_SIGN);
			if (equalsSignIndex == -1) {
				continue;
			}

			String paramName = paramArray[i].substring(0, equalsSignIndex);
			String paramValue = paramArray[i].substring(equalsSignIndex + 1);
			parameters.put(paramName, paramValue);
		}

		return parameters;
	}

	public static String getReferrer() {
		String localReferrer = referrer.get();
		if (!ValidatorUtil.isEmpty(localReferrer)) {
			return localReferrer;
		}
		return globalReferrer;
	}

	public static String getUserAgent() {
		String localAgent = userAgent.get();
		if (!ValidatorUtil.isEmpty(localAgent)) {
			return localAgent;
		}
		return globalUserAgent;
	}

	/**
	 * 忽略URL中的末尾的'/'符号.
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            url地址字符串
	 * @return 忽略末尾'/'符号后的url地址.
	 */
	public static String ignoreLastRightSlash(String url) {
		if (ValidatorUtil.isEmpty(url)) {
			return url;
		}

		// 末尾字符是否为'/', 若是则去除
		if (url.charAt(url.length() - 1) == SEPARATOR_SIGN) {
			return url.substring(0, url.length() - 1);
		}

		return url;
	}

	/**
	 * 访问页面URL，获得页面内容
	 * 
	 * @author Lucifer Wong
	 * @param pageURL
	 *            页面URL
	 * @return 页面内容
	 * @throws IOException
	 *             IO异常
	 */
	public static String readContent(String pageURL) throws IOException {
		InputStream in = null;
		ByteArrayOutputStream out = null;

		try {
			in = visitContent(pageURL);
			out = new ByteArrayOutputStream();

			byte[] buffer = new byte[BUFFER_SIZE];
			int length;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			return new String(out.toByteArray());
		} finally {
			FileUtil.close(in);
			FileUtil.close(out);
		}
	}

	public static void readErrorResponse(URLConnection c) {
		if (c == null) {
			return;
		}
		InputStream in = null;
		try {
			in = ((HttpURLConnection) c).getErrorStream();
			if (in == null) {
				return;
			}
			// read the response body
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			int count;
			byte[] bytes = new byte[8192];
			while ((count = in.read(bytes)) != -1) {
				out.write(bytes, 0, count);
			}
			throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtil.close(in);
		}
	}

	/**
	 * 设置字母集
	 * 
	 * @author Lucifer Wong
	 * @param charSet
	 *            charSet
	 */
	public static void setCharSet(String charSet) {
		URLUtil.charSet = charSet;
	}

	/**
	 * Prior to opening a URLConnection, calling this method will set all
	 * unexpired cookies that match the path or subpaths for thi underlying URL
	 * <p>
	 * The connection MUST NOT have been opened method or an IOException will be
	 * thrown.
	 * 
	 * @author Lucifer Wong
	 * @param conn
	 *            a java.net.URLConnection - must NOT be open, or IOException
	 *            will be thrown
	 * @throws IOException
	 *             Thrown if conn has already been opened.
	 * @param store
	 *            store
	 */
	@SuppressWarnings("unchecked")
	public static void setCookies(URLConnection conn, Map<String, Map<?, ?>> store) throws IOException {
		// let's determine the domain and path to retrieve the appropriate
		// cookies
		URL url = conn.getURL();
		String domain = getCookieDomainFromHost(url.getHost());
		String path = url.getPath();

		Map<String, ConcurrentHashMap<?, ?>> domainStore = (Map<String, ConcurrentHashMap<?, ?>>) store.get(domain);
		if (domainStore == null) {
			return;
		}
		StringBuilder cookieStringBuffer = new StringBuilder();
		Iterator<?> cookieNames = domainStore.keySet().iterator();

		while (cookieNames.hasNext()) {
			String cookieName = (String) cookieNames.next();
			Map<?, ?> cookie = domainStore.get(cookieName);
			// check cookie to ensure path matches and cookie is not expired
			// if all is cool, add cookie to header string
			if (comparePaths((String) cookie.get(PATH), path) && isNotExpired((String) cookie.get(EXPIRES))) {
				cookieStringBuffer.append(cookieName);
				cookieStringBuffer.append('=');
				cookieStringBuffer.append((String) cookie.get(cookieName));
				if (cookieNames.hasNext()) {
					cookieStringBuffer.append(SET_COOKIE_SEPARATOR);
				}
			}
		}
		try {
			conn.setRequestProperty(COOKIE, cookieStringBuffer.toString());
		} catch (IllegalStateException e) {
			throw new IOException("Illegal State! Cookies cannot be set on a URLConnection that is already connected. "
					+ "Only call setCookies(java.net.URLConnection) AFTER calling java.net.URLConnection.connect().");
		}
	}

	public static void setReferrer(String referer) {
		if (ValidatorUtil.isEmpty(globalReferrer)) {
			globalReferrer = referer;
		}
		referrer.set(referer);
	}

	public static void setUserAgent(String agent) {
		if (ValidatorUtil.isEmpty(globalUserAgent)) {
			globalUserAgent = agent;
		}
		userAgent.set(agent);
	}

	/**
	 * 缩短url，把baseURL开头的部分去掉，缩短的url都是以"/"开头的
	 * 
	 * @author Lucifer Wong
	 * @param url
	 *            原来URL
	 * @param baseURL
	 *            头部
	 * @return 缩短url
	 */
	public static String shortenURL(String url, String baseURL) {
		url = StringUtil.trim(url);
		baseURL = StringUtil.trim(baseURL);

		if (baseURL != null && baseURL.endsWith(String.valueOf(SEPARATOR_SIGN))) {
			baseURL = baseURL.substring(0, baseURL.length() - 1);
		}

		return !ValidatorUtil.isEmpty(url) && !ValidatorUtil.isEmpty(baseURL) && url.startsWith(baseURL)
				? url.substring(baseURL.length()) : url;
	}

	/**
	 * 访问页面URL，获得输入流
	 * 
	 * @author Lucifer Wong
	 * @param pageURL
	 *            页面URL
	 * @return 输入流
	 * @throws IOException
	 *             IO异常
	 */
	public static InputStream visitContent(String pageURL) throws IOException {
		URL url = new URL(pageURL);
		HttpURLConnection client = (HttpURLConnection) url.openConnection();

		HttpURLConnection.setFollowRedirects(false);
		client.setInstanceFollowRedirects(false);

		client.connect();

		return client.getInputStream();
	}

	/**
	 * 查询字符串后面增加参数
	 * 
	 * @author Lucifer Wong
	 * @param queryString
	 *            查询字符串，比如：id=1&type=1
	 * @param name
	 *            参数的名称
	 * @param value
	 *            参数的值
	 * @return 拼接后的查询字符串
	 */
	private static StringBuilder appendParameter(StringBuilder queryString, String name, Object value) {
		queryString.append(name);
		queryString.append(EQUALS_SIGN);

		if (value instanceof Boolean) {
			value = ((Boolean) value).booleanValue() ? "1" : "0";
		} else if (value instanceof Date) {
			value = DateUtil.date2StringByDay((Date) value);
		}

		try {
			queryString.append(URLEncoder.encode(String.valueOf(value), charSet));
		} catch (UnsupportedEncodingException e) {
			// ignore
		}
		return queryString;
	}

	private static void setNaiveSSLSocketFactory(HttpsURLConnection sc) {
		sc.setSSLSocketFactory(naiveSSLSocketFactory);
		sc.setHostnameVerifier(NAIVE_VERIFIER);
	}

	static boolean comparePaths(String cookiePath, String targetPath) {
		return cookiePath == null || "/".equals(cookiePath)
				|| targetPath.regionMatches(0, cookiePath, 0, cookiePath.length());
	}

	static boolean isNotExpired(String cookieExpires) {
		if (cookieExpires == null) {
			return true;
		}
		try {
			return new Date().compareTo(DateUtil._parse(cookieExpires, "EEE, dd-MMM-yyyy hh:mm:ss z")) <= 0;
		} catch (ParseException e) {
			return false;
		}
	}
}
