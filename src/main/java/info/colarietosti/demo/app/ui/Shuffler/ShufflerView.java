package info.colarietosti.demo.app.ui.Shuffler;

import info.colarietosti.demo.app.backend.util.mySeriesShuffler.SeriesShuffler;
import com.vaadin.navigator.View;
import info.colarietosti.demo.app.backend.util.mySeriesShuffler.Serie;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import info.colarietosti.demo.app.ui.AppUI;

import java.io.Serializable;

@SpringView(ui = AppUI.class)
public class ShufflerView extends VerticalLayout implements Serializable, View {

    final SeriesShuffler shuffler = new SeriesShuffler();
    ComboBox<Serie> cb;
    Button button;

    public ShufflerView() {
        button = new Button("Click Me");

        button.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        cb = new ComboBox<Serie>("Suche dir eine Serie aus",shuffler.getSeriesList());
        cb.setItemCaptionGenerator((ItemCaptionGenerator<Serie>) serie -> serie.getName());

        button.removeClickShortcut();

        button.addClickListener(clickEvent -> {
            String url = "";
            url = shuffler.shuffle(cb.getSelectedItem());
            if (url == "") {
                Notification.show("Something went wrong, please try again",Notification.Type.WARNING_MESSAGE);
            } else {
                UI.getCurrent().getPage().open(url, "_blank", false);
            }
        });

        this.addComponents(cb, button);
    }

}
