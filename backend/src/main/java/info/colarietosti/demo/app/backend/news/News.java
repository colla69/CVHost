package info.colarietosti.demo.app.backend.news;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "news")
@Getter @Setter @NoArgsConstructor
public class News {
    @Id
    @GeneratedValue
    private int id;

    private String title;
    private Date release_date;
    private String description_text;
    private String img_link;

}
