/**
 * Copyright (c) Sunesis d.o.o.
 */
package com.kumuluz.ee.logs;

import java.util.*;

/**
 * Created by Rok on 14. 03. 2017.
 *
 * @Author Rok Povse, Marko Skrjanec
 */
public class LogUtil {

    private static LogUtil logUtil;

    private Logger loggerInstance;
    private LogCommons logCommonsInstance;
    private LogConfigurator logConfigurator;

    private Map<String,Logger> loggers;
    private Map<String,LogCommons> loggersCommons;

    private LogUtil() {
        loggers = new HashMap();
        initInstances();
    }

    /**
     * Returns a singleton LogUtil instance
     * @return LogUtil instance
     */
    public static LogUtil getInstance() {
        if (logUtil == null) {
            logUtil = new LogUtil();
        }

        return logUtil;
    }

    /**
     * Returns Logger Instance if it exists for this name
     * If not, it first creates it.
     * @param loggerName  String name of the logger
     * @return Log insntace
     */
    public Logger getLogInstance(String loggerName) {
        if (!loggers.containsKey(loggerName)) {
            loggers.put(loggerName, loggerInstance.getLogger(loggerName));
        }
        return loggers.get(loggerName);
    }

    /**
     * Returns LogCommons Instance if it exists for this name
     * If not, it first creates it.
     * @param loggerName String name of the logger
     * @return LogCommons insntace
     */
    public LogCommons getLogCommonsInstance(String loggerName) {
        if (!loggersCommons.containsKey(loggerName)) {
            loggersCommons.put(loggerName, logCommonsInstance.getCommonsLogger(loggerName));
        }
        return loggersCommons.get(loggerName);
    }

    /**
     * @return LogConfigurator instance
     */
    public LogConfigurator getLogConfigurator() {
        return logConfigurator;
    }

    /**
     * Searches and initializes inital instances
     * for logger, commons logger and log configurator
     */
    private void initInstances() {

        List<Logger> loggerImpl = new ArrayList<>();
        ServiceLoader.load(Logger.class).forEach(loggerImpl::add);

        List<LogCommons> logCommonsImpl = new ArrayList<>();
        ServiceLoader.load(LogCommons.class).forEach(logCommonsImpl::add);

        List<LogConfigurator> logConfiguratorsConfigImpl = new ArrayList<>();
        ServiceLoader.load(LogConfigurator.class).forEach(logConfiguratorsConfigImpl::add);


        if (loggerImpl.isEmpty() || loggerImpl.size() > 1) {
            throw new IllegalArgumentException(
                    " Please provide exactly one implementation " +
                    " of class com.kumuluz.ee.logs.Logger");
        }

        if (logCommonsImpl.isEmpty() || loggerImpl.size() > 1) {
            throw new IllegalArgumentException( " Please provide exactly one implementation " +
                    "of class com.kumuluz.ee.logs.LogCommons");
        }

        if (logConfiguratorsConfigImpl.isEmpty() || loggerImpl.size() > 1) {
            throw new IllegalArgumentException( " Please provide exactly one implementation " +
                    "of class com.kumuluz.ee.logs.LogConfigurator");
        }

        loggerInstance = loggerImpl.get(0);
        logCommonsInstance = logCommonsImpl.get(0);
        logConfigurator = logConfiguratorsConfigImpl.get(0);
    }
}
