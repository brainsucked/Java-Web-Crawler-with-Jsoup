package web.crawler;

import java.util.List;

/**
 * Class JobCrawlerConfig (Builder design pattern) - Configuration class
 */
public class JobCrawlerConfig {
    private final List<String> targetUrls;
    private final int maxParallelCrawls;

    public JobCrawlerConfig(List<String> targetUrls, int maxParallelCrawls) {
        this.targetUrls = targetUrls;
        this.maxParallelCrawls = maxParallelCrawls;
    }

    public List<String> getTargetUrls() {
        return targetUrls;
    }

    public int getMaxParallelCrawls() {
        return maxParallelCrawls;
    }
}