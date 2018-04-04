package org.zgc.mvvmdemo.util.http;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * Created by Nick on 2017/12/10
 */
public class HttpLogInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        /** No logs. */
        NONE,
        /**
         * Logs request and response lines.
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }

    public interface Logger {
        void log(String message);

        /** A {@link HttpLogInterceptor.Logger} defaults output appropriate for the current platform. */
        HttpLogInterceptor.Logger DEFAULT = new HttpLogInterceptor.Logger() {
            @Override public void log(String message) {
                Platform.get().log(INFO, message, null);
            }
        };
    }

    public HttpLogInterceptor() {
        this(HttpLogInterceptor.Logger.DEFAULT);
    }

    public HttpLogInterceptor(HttpLogInterceptor.Logger logger) {
        this.logger = logger;
    }

    private final HttpLogInterceptor.Logger logger;

    private volatile HttpLogInterceptor.Level level = HttpLogInterceptor.Level.NONE;

    /** Change the level at which this interceptor logs. */
    public HttpLogInterceptor setLevel(HttpLogInterceptor.Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public HttpLogInterceptor.Level getLevel() {
        return level;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        HttpLogInterceptor.Level level = this.level;

        StringBuffer logStr = new StringBuffer();

        Request request = chain.request();
        if (level == HttpLogInterceptor.Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == HttpLogInterceptor.Level.BODY;
        boolean logHeaders = logBody || level == HttpLogInterceptor.Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        String requestStartMessage = "--> "
                + request.method()
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : "");
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        logStr.append(requestStartMessage);
        logStr.append("\n");


        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logStr.append("Content-Type: " + requestBody.contentType());
                    logStr.append("\n");
                }
                if (requestBody.contentLength() != -1) {
                    logStr.append("Content-Length: " + requestBody.contentLength());
                    logStr.append("\n");
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logStr.append(name + ": " + headers.value(i));
                    logStr.append("\n");
                }
            }

            if (!logBody || !hasRequestBody) {
                logStr.append("--> END " + request.method());
                logStr.append("\n");
            } else if (bodyEncoded(request.headers())) {
                logStr.append("--> END " + request.method() + " (encoded body omitted)");
                logStr.append("\n");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                logStr.append("");
                logStr.append("\n");
                if (isPlaintext(buffer)) {
                    logStr.append(buffer.readString(charset));
                    logStr.append("\n");
                    logStr.append("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
                    logStr.append("\n");
                } else {
                    logStr.append("--> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)");
                    logStr.append("\n");
                }
            }
        }
        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logStr.append("<-- HTTP FAILED: " + e);
            logStr.append("\n");
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        logStr.append("<-- "
                + response.code()
                + (response.message().isEmpty() ? "" : ' ' + response.message())
                + ' ' + response.request().url()
                + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body" : "") + ')');
        logStr.append("\n");
        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logStr.append(headers.name(i) + ": " + headers.value(i));
                logStr.append("\n");
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logStr.append("<-- END HTTP");
                logStr.append("\n");
            } else if (bodyEncoded(response.headers())) {
                logStr.append("<-- END HTTP (encoded body omitted)");
                logStr.append("\n");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (!isPlaintext(buffer)) {
                    logStr.append("");
                    logStr.append("\n");
                    logStr.append("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                    logStr.append("\n");
                    return response;
                }

                if (contentLength != 0) {
                    logStr.append("");
                    logStr.append("\n");
                    logStr.append(buffer.clone().readString(charset));
                    logStr.append("\n");
                }

                logStr.append("<-- END HTTP (" + buffer.size() + "-byte body)");
                logStr.append("\n");
            }
        }
        logger.log(logStr.toString());
        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
