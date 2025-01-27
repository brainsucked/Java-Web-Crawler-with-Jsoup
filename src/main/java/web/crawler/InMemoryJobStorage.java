package web.crawler;

import java.util.ArrayList;
import java.util.List;

/**
 * Class InMemoryJobStorage (Singleton design pattern) - store craweled jobs in array
 */
public class InMemoryJobStorage {
    private static final InMemoryJobStorage INSTANCE = new InMemoryJobStorage();
    private final List<Job> jobs = new ArrayList<>();

    private InMemoryJobStorage() {}

    public static InMemoryJobStorage getInstance() {
        return INSTANCE;
    }

    public synchronized void saveJob(Job job) {
        jobs.add(job);
    }

    public synchronized List<Job> getAllJobs() {
        return new ArrayList<>(jobs);
    }
}
