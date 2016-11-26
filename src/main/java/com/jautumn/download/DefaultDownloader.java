package com.jautumn.download;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class DefaultDownloader implements Downloader {
    private static Logger logger = Logger.getLogger(DefaultDownloader.class.getName());
    private static final String TEST_URL = "http://fileshare1100.depositfiles.com/auth-148010359645d310ab6aa8ffc1a49637-209.133.66.214-48387403-165058487-guest/FS110-6/c-4-0-i-platforma-net-4-dlya-professionalov.zip";
    private static int ATTEMPT_NUMBER = 0;

    public static void main(String[] args) throws InterruptedException, IOException {
        new DefaultDownloader().download(Paths.get(TEST_URL), TEST_URL);
    }

    @Override
    public void proxyDownload(Path dirPath, String downloadURL, String proxyHost, int proxyPort) throws IOException, InterruptedException {
        URL url = new URL(downloadURL);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("137.135.166.225", 8124));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
        startDownload(dirPath, Paths.get(url.getPath()).getFileName().toString(), connection);
    }

    private void startDownload(Path dirPath, String fileName, HttpURLConnection connection) throws InterruptedException {
        try {
            ATTEMPT_NUMBER++;
            logger.info("Connection attempt: " + ATTEMPT_NUMBER);
            logger.info("Response code: " + connection.getResponseCode());
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                logger.info("Start downloading");
                logger.info("content length: " + connection.getContentLength());

                BufferedInputStream in = null;
                FileOutputStream fout = null;
                try {
                    in = new BufferedInputStream(connection.getInputStream());

                    String savePath = dirPath.resolve(fileName).toString();
                    fout = new FileOutputStream(savePath);

                    final byte data[] = new byte[1024];
                    int count;
                    int downloaded = 0;
                    while ((count = in.read(data, 0, 1024)) != -1) {
                        fout.write(data, 0, count);
                        downloaded += count;
                    }
                    logger.info("downloaded: " + downloaded);
                    logger.info("save file to: " + savePath);
                } finally {
                    if (in != null) {
                        in.close();
                    }
                    if (fout != null) {
                        fout.close();
                    }
                }

            } else {
                startDownload(dirPath, fileName, connection);
                return;
            }
        } catch (IOException e) {
            Thread.sleep(3000);
            System.out.println(e);
            startDownload(dirPath, fileName, connection);
            return;
        }
    }

    @Override
    public void download(Path driPath, String downloadURL) throws InterruptedException, IOException {
        URL url = new URL(downloadURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        startDownload(driPath, Paths.get(url.getPath()).getFileName().toString(), connection);
    }
}
