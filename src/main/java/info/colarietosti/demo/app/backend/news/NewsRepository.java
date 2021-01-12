package info.colarietosti.demo.app.backend.news;

import info.colarietosti.demo.app.backend.news.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News,Long> {

}
