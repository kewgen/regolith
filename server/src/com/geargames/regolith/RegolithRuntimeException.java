package com.geargames.regolith;

/**
 * User: mkutuzov
 * Date: 15.06.12
 */
public class RegolithRuntimeException extends RuntimeException {
    public RegolithRuntimeException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public RegolithRuntimeException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public RegolithRuntimeException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public RegolithRuntimeException(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
