package info.colarietosti.demo.app.backend.news.service;


import info.colarietosti.demo.app.backend.news.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    @Autowired
    NewsRepository newsRepository;
}
