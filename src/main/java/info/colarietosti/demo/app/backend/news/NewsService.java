package info.colarietosti.demo.app.backend.news;


import info.colarietosti.demo.app.backend.news.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    @Autowired
    NewsRepository newsRepository;
}