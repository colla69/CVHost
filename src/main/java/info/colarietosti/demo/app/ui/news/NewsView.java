package info.colarietosti.demo.app.ui.news;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;
import info.colarietosti.demo.app.backend.news.News;
import info.colarietosti.demo.app.backend.news.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringView
public class NewsView extends CssLayout implements View {

    @Autowired
    NewsRepository newsRepository;

    CssLayout wrap = new CssLayout();

    @PostConstruct
    private void init(){
        this.wrap.setSizeFull();
        this.addComponent(wrap);
        setSizeFull();

        this.addStyleName("wrap");
        List<News> newsList = newsRepository.findAll();
        newsList.sort( (News p1, News p2)-> p2.getRelease_date().compareTo(p1.getRelease_date()));
        newsList.forEach(n -> {
            NewsCard nc = new NewsCard(
                    n.getRelease_date(),
                    n.getTitle(),
                    n.getDescription_text(),
                    n.getImg_link());
            wrap.addComponent( nc );
        });
    }
}
