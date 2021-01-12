package info.colarietosti.demo.app.ui.news;

import com.vaadin.ui.Layout;
import info.colarietosti.demo.app.backend.news.News;
import info.colarietosti.demo.app.backend.news.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsCardsGetter {

    @Autowired
    NewsRepository newsRepository;

    public void getAndEmbedNews(Layout layout){
        List<News> newsList = newsRepository.findAll();
        newsList.sort( (News p1, News p2)-> p2.getRelease_date().compareTo(p1.getRelease_date()));
        newsList.forEach(n -> {
            NewsCard nc = new NewsCard(
                    n.getRelease_date(),
                    n.getTitle(),
                    n.getDescription_text(),
                    n.getImg_link());
            layout.addComponent( nc );
        });
    }
}
