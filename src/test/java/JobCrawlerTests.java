import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import web.crawler.InMemoryJobStorage;
import web.crawler.Job;
import web.crawler.JobCrawler;
import web.crawler.JobCrawlerConfig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JobCrawlerTests {

    private InMemoryJobStorage jobStorage;
    private JobCrawlerConfig config;
    private JobCrawler crawler;

    @Before
    public void setup() {

        // Инициализация на конфигурацията
        List<String> targetUrls = new ArrayList<>();
        targetUrls.add("https://yox.bg/%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0/%D0%B3%D1%80%D0%B0%D0%B4-%D1%80%D1%83%D1%81%D0%B5-63427|.job-post|h2 a");

        config = new JobCrawlerConfig(targetUrls, 2);

        // Създаваме нов обект за съхранение на обяви
        jobStorage = InMemoryJobStorage.getInstance();
        jobStorage.getAllJobs().clear(); // Изчистваме за тестовете

        // Инициализация на краулера
        crawler = new JobCrawler(config);
    }

    @Test
    public void testSaveJob() {
        Job job = new Job("ПРОДАВАЧ - КОНСУЛТАНТ", "https://yox.bg/%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0/%D0%B3%D1%80%D0%B0%D0%B4-%D1%80%D1%83%D1%81%D0%B5-63427");
        jobStorage.saveJob(job);

        List<Job> jobs = jobStorage.getAllJobs();
        assertEquals(3, jobs.size());
        assertEquals("ПРОДАВАЧ - КОНСУЛТАНТ", jobs.get(0).getTitle());
    }

    @Test
    public void testGetAllJobs() {
        jobStorage.saveJob(new Job("ПРОДАВАЧ - КОНСУЛТАНТ", "https://yox.bg/%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0/%D0%B3%D1%80%D0%B0%D0%B4-%D1%80%D1%83%D1%81%D0%B5-63427"));
        jobStorage.saveJob(new Job("Търговски консултант в А1 магазин", "https://yox.bg/%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0/%D0%B3%D1%80%D0%B0%D0%B4-%D1%80%D1%83%D1%81%D0%B5-63427"));

        List<Job> jobs = jobStorage.getAllJobs();
        assertEquals(2, jobs.size());
    }

    @Test
    public void testCrawlWebsite() throws Exception {
        // Подготвяме мокнати HTML данни
        String html = "<div class=\"job-post\"> <div class=\"row\"> <div class=\"col\">  <h2> <a href=\"/jobs/продавач-консултант-1717873\" role=\"button\" rel=\"nofollow noreferrer\" aria-label=\"пълно описание на продавач - консултант\" data-job-component=\"link\"> Продавач - консултант </a> </h2> <a href=\"https://yox.bg/зора-м-м-с-\" class=\"sub-title\" data-job-component=\"employer-link\" target=\"_blank\"> Зора </a>  <p class=\"sub-title place\">гр. Русе</p>  </div>  <div class=\"col-auto\"> <img src=\"https://cdn.yox.bg/webp/60/images/94173d27-a5c0-4b6e-ab54-e4e73cd818ed/лого-на-зора-м-м-с-оод.jpeg\" data-src=\"https://cdn.yox.bg/webp/60/images/94173d27-a5c0-4b6e-ab54-e4e73cd818ed/лого-на-зора-м-м-с-оод.jpeg\" class=\"logo\" alt=\"Лого\" width=\"60\" title=\"Зора\"> </div>  </div>  <div class=\"content\">   <div class=\"job-tags\"> <img alt=\"icon\" width=\"20\" height=\"20\" src=\"data:image/svg+xml,%0A%3Csvg xmlns='http://www.w3.org/2000/svg' height='24px' viewBox='0 0 24 24' width='24px' fill='%23000000'%3E%3Cpath d='M0 0h24v24H0zm10 5h4v2h-4zm0 0h4v2h-4z' fill='none'/%3E%3Cpath d='M10 16v-1H3.01L3 19c0 1.11.89 2 2 2h14c1.11 0 2-.89 2-2v-4h-7v1h-4zm10-9h-4.01V5l-2-2h-4l-2 2v2H4c-1.1 0-2 .9-2 2v3c0 1.11.89 2 2 2h6v-2h4v2h6c1.1 0 2-.9 2-2V9c0-1.1-.9-2-2-2zm-6 0h-4V5h4v2z'/%3E%3C/svg%3E\">  <span class=\"tag\">Пълно работно време</span>  <span class=\"tag\">Постоянна работа</span>  <span class=\"tag\">Подходяща и за кандидати с малък или без опит</span>  </div>  <p class=\"excerpt\">  ТЪРГОВСКА ВЕРИГА „ ЗОРА” ТЪРСИ ЕНЕРГИЧНИ И МОТИВИРАНИ КАНДИДАТИ ЗА ПОЗИЦИЯТА „ПРОДАВАЧ - КОНСУЛТАНТ”  Интересна позиция с възможност...  </p> <div class=\"expand-more\" data-job-component=\"more-links\"> <span class=\"date-tag\">24 Jan 2025</span> <span data-job-component=\"more-links-trigger\">Още...</span>  <ul class=\"expand-more hide\">  <li>Виж всички обяви за работа в <a href=\"https://yox.bg/работа/обяви-за-работа-зора-м-м-с-\" data-job-component=\"more-links-outbound\" target=\"_blank\" title=\"Виж всички обяви за работа във фирма ЗОРА\"> ЗОРА</a></li>  <li>Обяви за <a href=\"https://yox.bg/работа/град-русе-63427\" data-job-component=\"more-links-outbound\" target=\"_blank\" title=\"Виж всички обяви за работа в гр. Русе\"> работа в Русе</a></li>  <li>Заплата и инфо: <a href=\"https://yox.bg/заплати/заплата-в-зора-м-м-с-\" data-job-component=\"more-links-outbound\" target=\"_blank\" title=\"Виж заплати в ЗОРА\"> ЗОРА заплата</a></li>  </ul>  </div> </div>  </div>";

        Document mockDocument = Jsoup.parse(html);
        //System.out.println(mockDocument.select("h2 a").first().text());

        // Тестваме методите
        crawler.crawl();
        List<Job> jobs = jobStorage.getAllJobs();

        // Уверяваме се, че данните са правилно извлечени
        assertEquals(18, jobs.size());
        assertEquals("ПРОДАВАЧ - КОНСУЛТАНТ", jobs.get(0).getTitle());
        assertEquals("Продавач - консултант", mockDocument.select("h2 a").first().text());
        assertEquals("https://yox.bg/%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0/%D0%B3%D1%80%D0%B0%D0%B4-%D1%80%D1%83%D1%81%D0%B5-63427", jobs.get(0).getUrl());
    }

    @Test
    public void testSingletonStorage() {
        // Уверете се, че Singleton е правилно имплементиран
        InMemoryJobStorage storage1 = InMemoryJobStorage.getInstance();
        InMemoryJobStorage storage2 = InMemoryJobStorage.getInstance();

        assertSame(storage1, storage2);
    }

    @Test
    public void testJobToString() {
        Job job = new Job("Data Scientist", "https://jobs.web/job2");
        String jobString = job.toString();

        assertTrue(jobString.contains("Data Scientist"));
        assertTrue(jobString.contains("https://jobs.web/job2"));
    }
}
