package org.com.librarysystem.patterns.singleton;

public class Logger {
    private static final Logger instance = new Logger();

    private Logger() {
        // private constructor
    }

    public static Logger getInstance() {
        return instance;
    }

    public void info(String message) {
        System.out.println("[INFO] " + message);
    }
    public void warn(String message) {
        System.out.println("[WARN] " + message);
    }
    public void error(String message) {
        System.err.println("[ERROR] " + message);
    }
}
