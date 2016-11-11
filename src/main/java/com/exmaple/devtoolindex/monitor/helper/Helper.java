package com.exmaple.devtoolindex.monitor.helper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by hongkailiu on 2016-11-11.
 */
@Component
@Slf4j
public class Helper {
    private String appVersion;

    /* package */ static final String EMPTY_VERSION_FILE = "empty version file";
    /* package */ static final String NO_VERSION_FILE_FOUND = "no version file found";
    /* package */ static final String VERSION = "VERSION";


    public String getAppVersion() {
        if (appVersion == null) {
            synchronized (this) {
                if (appVersion == null) {
                    appVersion = loadVersionFromResource();
                }
            }
        }
        return appVersion;
    }

    private String loadVersionFromResource() {
        return loadVersionFromResource(VERSION);
    }

    /* package */ String loadVersionFromResource(String path) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(path);
        try {
            String version = StringUtils.trim(IOUtils.toString(is, Charset.defaultCharset()));
            if (StringUtils.isNoneBlank(version)) {
                return version;
            }
            log.error(EMPTY_VERSION_FILE);
            return EMPTY_VERSION_FILE;
        } catch (IOException | NullPointerException e) {
            log.error(e.getMessage(), e);
            return NO_VERSION_FILE_FOUND;
        } finally {
            IOUtils.closeQuietly(is);
        }

    }
}
