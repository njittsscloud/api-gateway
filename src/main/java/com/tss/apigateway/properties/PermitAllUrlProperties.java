package com.tss.apigateway.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author MQG
 * @date 2018/12/05
 */
@Component
@ConfigurationProperties(prefix = "auth")
public class PermitAllUrlProperties {

    private static List<Pattern> permitallUrlPattern;

    private List<Url> permitall;

    public String[] getPermitallPatterns() {
        List<String> urls = new ArrayList();
        Iterator<Url> iterator = permitall.iterator();
        while (iterator.hasNext()) {
            urls.add(iterator.next().getPattern());
        }
        return urls.toArray(new String[0]);
    }

    public static List<Pattern> getPermitallUrlPattern() {
        return permitallUrlPattern;
    }

    public static class Url {
        private String pattern;

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }
    }

    @PostConstruct
    public void init() {
        if (permitall != null && permitall.size() > 0) {
            this.permitallUrlPattern = new ArrayList();
            Iterator<Url> iterator = permitall.iterator();
            while (iterator.hasNext()) {
                String currentUrl = iterator.next().getPattern().replaceAll("\\*\\*", "(.*?)");
                Pattern currentPattern = Pattern.compile(currentUrl, Pattern.CASE_INSENSITIVE);
                permitallUrlPattern.add(currentPattern);
            }

        }
    }

    public boolean isPermitAllUrl(String url) {
        for (Pattern pattern : permitallUrlPattern) {
            if (pattern.matcher(url).find()) {
                return true;
            }
        }
        return false;
    }

    public static void setPermitallUrlPattern(List<Pattern> permitallUrlPattern) {
        PermitAllUrlProperties.permitallUrlPattern = permitallUrlPattern;
    }

    public List<Url> getPermitall() {
        return permitall;
    }

    public void setPermitall(List<Url> permitall) {
        this.permitall = permitall;
    }
}

