package web.crawler;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Application
 */
public class Runner {
    public static void main(String[] args) {

        //CookieManager cookieManager = new CookieManager();
        //cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        //System.setProperty("java.net.useSystemProxies", "true");

        // Configure crawler
        List<String> targetUrls = new ArrayList<>();

        /**
         * How to use: url|.job-itm-selector|.job-title-selector ( separated by | )
         * jobs.bg & jobtiger.bg - does not work
         */

        //targetUrls.add("https://www.jobs.bg/front_job_search.php?categories%5B%5D=56&domains%5B%5D=3|.page-1|.card-title span(2)");
        //targetUrls.add("https://www.jobtiger.bg/obiavi-za-rabota/?keyword=java|.jobs-list-view|.job-position-title");
        //targetUrls.add("https://www.zaplata.bg/it/|item|.title a");
        //targetUrls.add("https://dev.bg/company/jobs/java/|.job-list-item|.job-title");
        targetUrls.add("https://www.alo.bg/obiavi/rabota/rabota-v-stranata-predlaga/?region_id=18&location_ids=3564|.listvip-item|.listvip-item-title");
        targetUrls.add("https://yox.bg/%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0/%D0%B3%D1%80%D0%B0%D0%B4-%D1%80%D1%83%D1%81%D0%B5-63427|.job-post|h2 a");

        int maxParallelCrawls = 5; // Parallel threads

        JobCrawlerConfig config = new JobCrawlerConfig(targetUrls, maxParallelCrawls);
        JobCrawler crawler = new JobCrawler(config);

        crawler.crawl();

        // Output results
        System.out.println("Crawled Jobs:");
        for (Job job : InMemoryJobStorage.getInstance().getAllJobs()) {
            System.out.println(job);
        }
    }
}

