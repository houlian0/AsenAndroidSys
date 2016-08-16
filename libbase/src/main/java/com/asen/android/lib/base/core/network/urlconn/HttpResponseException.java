package com.asen.android.lib.base.core.network.urlconn;

/**
 * Õ¯¬Á¡¨Ω” “Ï≥£
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class HttpResponseException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public HttpResponseException() {
        super();
    }

    public HttpResponseException(String message) {
        super(message);
    }

    public HttpResponseException(Throwable throwable) {
        super(throwable);
    }

    public HttpResponseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
