package web.crawler;

/**
 * Class Job - holds basic information about a job
 */
public class Job {
    private final String title;
    private final String url;

    public Job(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Job{title='" + title + '\'' + ", url='" + url + '\'' + '}';
    }
}