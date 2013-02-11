package com.geargames.regolith;

/**
 * @author Mikhail_Kutuzov
 *         created: 25.05.12  23:49
 */
public class RegolithException extends Exception {
    public RegolithException() {
        super();
    }

    public RegolithException(String message) {
        super(message);
    }

    public RegolithException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegolithException(Throwable cause) {
        super(cause);
    }
}
