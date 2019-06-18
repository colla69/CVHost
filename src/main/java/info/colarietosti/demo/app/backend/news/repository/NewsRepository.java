package info.colarietosti.demo.app.backend.news.repository;

import info.colarietosti.demo.app.backend.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News,Long> {

}
