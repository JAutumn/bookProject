package com.jautumn.download;

import java.io.IOException;
import java.nio.file.Path;

public interface Downloader {

    void download(Path driPath, String downloadURL) throws IOException, InterruptedException;

    void proxyDownload(Path dirPath, String downloadURL, String proxyHost, int proxyPort) throws IOException, InterruptedException;
}
