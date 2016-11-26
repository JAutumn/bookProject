package com.jautumn.scraper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import com.jautumn.download.DefaultDownloader;
import com.jautumn.download.Downloader;
import com.jautumn.model.Book;
import com.jautumn.parser.core.api.DownloadLinkParser;
import com.jautumn.parser.core.exceptions.BadDownloadServiceURLException;
import com.jautumn.parser.core.exceptions.ConnectionLimitException;
import com.jautumn.parser.impl.DepositFilesDownloadLinkParser;

public class ScanlibApp {
    private static Logger logger = Logger.getLogger(ScanlibApp.class.getName());

    private static final Path DEFAULT_DIR = Paths.get("/home/evgeniy/Downloads/programming/java");
    private static final Pattern pageNumberPattern = Pattern.compile("\\d+");
    private static final Pattern downloadLinkPattern = Pattern.compile("https://depositfiles.com.*");

    private static final String TARGET_URL = "http://scanlibs.com/";
    private static final String CATEGORY_LINKS = "//a[contains(@class, 'page-numbers')]/@href";

    private static final String POSTS = "//div[contains(@class, 'entry-content')]";
    private static final String ISBNS = "/text()[contains(., 'ISBN')]";
    private static final String READ_MORE_LINKS = "/a/@href";

    private static final String DOWNLOAD_SERVICE_URL_PATH = "//span[contains(@data-url, 'depositfiles.com')]/@data-url";


    private WebClient client;
    private Downloader downloader;
    private DownloadLinkParser downloadLinkParser;


    public ScanlibApp() {
        client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getCookieManager().setCookiesEnabled(false);

        downloader = new DefaultDownloader();
        downloadLinkParser = new DepositFilesDownloadLinkParser(client);
    }

    public static void main(String[] args) throws BadDownloadServiceURLException, ConnectionLimitException {
        Options options = new Options();

        Option bookPostURLOption = new Option("u", "url", true, "book post URL");
        bookPostURLOption.setRequired(false);

        options.addOption(bookPostURLOption);

        CommandLineParser commandLineParser = new BasicParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            helpFormatter.printHelp(ScanlibApp.class.getName(), options);

            System.exit(1);
            return;
        }


        ScanlibApp scanlibApp = new ScanlibApp();

        String bookPostUrl = cmd.getOptionValue("url");
        System.out.println(bookPostUrl);

        try {
            if (StringUtils.isNotBlank(bookPostUrl)) {
                scanlibApp.processOne(bookPostUrl);
            } else {
                scanlibApp.processAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void processAll() throws InterruptedException, BadDownloadServiceURLException, ConnectionLimitException {
        try {
            HtmlPage page = client.getPage(TARGET_URL);
            List<DomAttr> paginationLinks = (List<DomAttr>) page.getByXPath(CATEGORY_LINKS);
            HtmlPage lastPage = client.getPage(getLastPage(paginationLinks));
            List<HtmlElement> posts = (List<HtmlElement>) lastPage.getByXPath(POSTS);
            for (HtmlElement bookPost : posts) {
                Book book = new Book();
                book.setIsbn(((Node) bookPost.getFirstByXPath(bookPost.getCanonicalXPath() + ISBNS)).getNodeValue());
                String bookPostURL = ((Node) bookPost.getFirstByXPath(bookPost.getCanonicalXPath() + READ_MORE_LINKS)).getNodeValue();
                book.setPageLink(bookPostURL);
                processBookPost(bookPostURL);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processOne(String bookPostUrl) throws IOException, InterruptedException, BadDownloadServiceURLException, ConnectionLimitException {
        processBookPost(bookPostUrl);
    }

    private void processBookPost(String bookPostURL) throws IOException, InterruptedException, BadDownloadServiceURLException, ConnectionLimitException {
        HtmlPage bookPage = client.getPage(bookPostURL);
        Node downloadLinkNode = bookPage.getFirstByXPath(DOWNLOAD_SERVICE_URL_PATH);
        Matcher matcher = downloadLinkPattern.matcher(downloadLinkNode.getNodeValue());
        matcher.find();
        String downloadLink = matcher.group();
        download(downloadLink);
    }

    private static String getLastPage(List<DomAttr> nodes) {
        return nodes.stream().max(new PageNumberComparator(pageNumberPattern)).orElseThrow(() -> new RuntimeException("last page didn't found")).getNodeValue();
    }

    private void download
            (String url)
            throws IOException, InterruptedException, BadDownloadServiceURLException, ConnectionLimitException {
        String downloadURL = downloadLinkParser.getDownloadURL(url);
        logger.info("got downloadURL: " + downloadURL);
        //TODO com.jautumn.download with proxy
        downloader.download(DEFAULT_DIR, downloadURL);
    }
}
