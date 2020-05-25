package com.mcsunnyside.loginredirect;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class RedirectRule {
    private String from;
    private String to;
    private List<String> host;

    public boolean matches(String in, String hostname) {
        if (!from.equalsIgnoreCase(in)) {
            return false;
        }
        for (String rule : host) {
            if (rule.equalsIgnoreCase(hostname)) {
                return true;
            }
        }
        return false;
    }
}
