package com.bacuti.common.utils;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Utility class for common methods to be used throughout the application.
 */
public class CommonUtil {

    // Static batch size for save
    public static int batchSize = 20;

    /**
     * Gets the user meta-data from context.
     */
    public static LinkedTreeMap<String, String> getLoggedInUserMeta() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = (Jwt) principal;
        Map<String, Object> claims = jwt.getClaims();
        return (LinkedTreeMap<String, String>) claims.get("userMetaData");
    }
}
