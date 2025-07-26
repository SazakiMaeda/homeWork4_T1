package com.sazakimaeda.hm4_autorization.model;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenWhitelist {

    private final Set<String> whitelist = ConcurrentHashMap.newKeySet();

    public void addToken(String token) {
        whitelist.add(token);
    }

    public void removeToken(String token) {
        whitelist.remove(token);
    }

    public boolean contains(String token) {
        return whitelist.contains(token);
    }
}
