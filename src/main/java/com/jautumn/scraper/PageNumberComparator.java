package com.jautumn.scraper;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.html.DomAttr;

public class PageNumberComparator implements Comparator<DomAttr> {
    private final Pattern pageNumberPattern;

    public PageNumberComparator(Pattern pageNumberPattern) {
        this.pageNumberPattern = pageNumberPattern;
    }

    @Override
    public int compare(DomAttr attr1, DomAttr attr2) {
        Matcher matcher1 = pageNumberPattern.matcher(attr1.getNodeValue());
        Matcher matcher2 = pageNumberPattern.matcher(attr2.getNodeValue());
        matcher1.find();
        matcher2.find();
        return Integer.valueOf(matcher1.group()).compareTo(Integer.valueOf(matcher2.group()));
    }
}
