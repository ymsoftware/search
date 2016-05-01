package org.ap.core.nginx;

import nginx.clojure.java.ArrayMap;
import sun.security.pkcs11.P11Util;

import static nginx.clojure.MiniConstants.CONTENT_TYPE;

/**
 * Created by yuri on 4/30/16.
 */
public class Constants {
    public static final String REQUEST_BODY = "body";
    public static final ArrayMap JSON_CONTENT_TYPE = ArrayMap.create(CONTENT_TYPE, "text/json");
}
