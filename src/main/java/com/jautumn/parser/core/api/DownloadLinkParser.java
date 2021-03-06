package com.jautumn.parser.core.api;

import java.io.IOException;

import com.jautumn.parser.core.exceptions.BadDownloadServiceURLException;
import com.jautumn.parser.core.exceptions.ConnectionLimitException;

public interface DownloadLinkParser {

    /**
     * Parse pages' of com.jautumn.download service website to get a link for file com.jautumn.download
     * @param startURL url of start page
     * @return link for file com.jautumn.download
     * @throws IOException
     * @throws InterruptedException
     * @throws BadDownloadServiceURLException bad start page url
     * @throws ConnectionLimitException some services limit com.jautumn.download for a while
     */
    String getDownloadURL
            (String startURL)
            throws IOException, InterruptedException, BadDownloadServiceURLException, ConnectionLimitException;
}
