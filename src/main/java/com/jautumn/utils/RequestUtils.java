package com.jautumn.utils;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Nullable;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;

public class RequestUtils {

    public static WebRequest build(String url, HttpMethod method, @Nullable String body) throws MalformedURLException {
        WebRequest webRequest = new WebRequest(new URL(url), method);
        if (HttpMethod.POST == method ) {
            webRequest.setRequestBody(body);
        }
        return webRequest;
    }

    public static WebRequest buildPost(String url, String body) throws MalformedURLException {
        return build(url, HttpMethod.POST, body);
    }

    public static WebRequest buildGet(String url) throws MalformedURLException {
        return build(url, HttpMethod.GET, null);
    }
}
