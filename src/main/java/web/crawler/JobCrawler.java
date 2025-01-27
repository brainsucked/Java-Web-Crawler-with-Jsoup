package web.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class JobCrawler - crawl job listing websites
 */
public class JobCrawler {
    private final JobCrawlerConfig config; // Configuration object containing target URLs and thread pool size
    private final ExecutorService executorService; // Thread pool to manage concurrent crawling tasks

    /**
     * Constructor for JobCrawler.
     * Initializes the thread pool with a fixed size based on maxParallelCrawls.
     *
     * @param config The configuration for the crawler (target URLs and max parallel crawls)
     */
    public JobCrawler(JobCrawlerConfig config) {
        this.config = config;
        this.executorService = Executors.newFixedThreadPool(config.getMaxParallelCrawls());
    }

    /**
     * Starts the crawling process.
     * Each URL in the target list is submitted as a task to the thread pool.
     * The method ensures all tasks are submitted and waits for their completion.
     */
    public void crawl() {
        for (final String url : config.getTargetUrls()) {
            // Submitting each URL to the thread pool for crawling
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    crawlWebsite(url);
                }
            });
        }

        // Shutdown the executor service to prevent new tasks
        executorService.shutdown();

        try {
            // Waiting for all tasks completion
            if (!executorService.awaitTermination(10, java.util.concurrent.TimeUnit.MINUTES)) {
                System.err.println("Timeout waiting for tasks to finish.");
            }
        } catch (InterruptedException e) {
            System.err.println("Execution interrupted: " + e.getMessage());
        }
    }

    private void crawlWebsite(String data) {
        /**
         * Split data string around matches
         * s[0] - url to craw
         * s[1] - job listing selector
         * s[2] - job title selector
         */
        String regex = "[|]";
        String[] s = data.split(regex);

        try {
            // Fetch the website's HTML content
            Document document = fetchDocument(s[0]);

            // TODO: Add support for navigating through pages
            // Select all job listing elements based on the CSS selector
            Elements jobElements = document.select(s[1]);
            //Elements jobElements = document.select(".job-list-item");

            for (Element jobElement : jobElements) {
                // Extract job title
                String jobTitle = jobElement.select(s[2]).text();
                //String jobTitle = jobElement.select(".job-title").text();

                // Extract job URL
                String jobUrl = jobElement.select("a").attr("href");

                // If a site has relative url - add the host
                Pattern pattern = Pattern.compile("https:", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(jobUrl);
                boolean matchFound = matcher.find();
                if(!matchFound) {
                    URI uri = new URI(s[0]);
                    String host = uri.getHost();
                    jobUrl = "https://" + host + jobUrl;
                }

                // Create a Job object and save it in the in-memory storage
                Job job = new Job(jobTitle, jobUrl);
                InMemoryJobStorage.getInstance().saveJob(job);
            }
        } catch (Exception e) {
            System.err.println("Error crawling site: " + s[0] + " - " + e.getMessage());
        }
    }

    /**
     * Fetches the HTML document from the given URL.
     * This method can be overridden for testing purposes.
     *
     * @param url The URL to fetch the HTML document from
     * @return The fetched Document object
     * @throws Exception If an error occurs while fetching the document
     */
    protected Document fetchDocument(String url) throws Exception {
        return Jsoup.connect(url)
        .ignoreContentType(true)
        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
        .header("Accept-Language", "en-US,en;q=0.5")
        .header("Accept-Encoding", "gzip, deflate, br")
        .referrer("https://google.bg")
        .followRedirects(true)
        .get();
    }
}