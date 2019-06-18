package info.colarietosti.demo.app.ui.news;

import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import lombok.extern.java.Log;
import org.vaadin.spring.annotation.PrototypeScope;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log
@PrototypeScope
public class NewsCard extends CssLayout {

    private final Date release_date;
    private final String title;
    private final String description;
    private final String img_link;

    private final Label titleLabel;
    private final Label releaseDate;
    private final Label descriptionArea;
    private final Image img;

    public NewsCard(Date release_date, String title, String description, String img_link) {
        this.release_date = release_date;
        this.title = title;
        this.description = description;
        this.img_link = img_link;
        descriptionArea = new Label(description);
        releaseDate = new Label(new SimpleDateFormat("EEE, dd/MM/yyyy").format(this.release_date));
        titleLabel = new Label(title);
        img = new Image();
        if (img_link != null) {
            img.setSource(new ExternalResource(this.img_link));
        }
        descriptionArea.setSizeFull();
        CssLayout imgWrap = new CssLayout();
        imgWrap.addComponent(img);
        imgWrap.addStyleName("imgwrap");
        descriptionArea.setEnabled(false);
        descriptionArea.setContentMode(ContentMode.HTML);
        titleLabel.setContentMode(ContentMode.HTML);
        titleLabel.addStyleName("title");
        img.addStyleName("newsimage");
        releaseDate.addStyleName("date");
        descriptionArea.addStyleName("description");
        this.addStyleName("newsCard");
        addComponents(titleLabel, releaseDate, imgWrap, descriptionArea);
    }
}
