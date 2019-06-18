package info.colarietosti.demo.app.ui.CV;

import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import info.colarietosti.demo.app.ui.FamFamFlags;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Scope("prototype")
@Component
public class CVContentImpl extends CVContent {


    @PostConstruct
    public void init() {
        getCvDE().setVisible(true);
        getDeBut().setIcon(FamFamFlags.GERMANY);
        getEnBut().setIcon(FamFamFlags.UNITED_STATES);
        getItBut().setIcon(FamFamFlags.ITALY);

        getItBut().addClickListener(event ->
        {
            getCvDE().setVisible(false);
            getCvEN().setVisible(false);
            getCvIT().setVisible(true);
        });
        getEnBut().addClickListener(event ->
        {
            getCvDE().setVisible(false);
            getCvEN().setVisible(true);
            getCvIT().setVisible(false);
        });
        getDeBut().addClickListener(event ->
        {
            getCvDE().setVisible(true);
            getCvEN().setVisible(false);
            getCvIT().setVisible(false);
        });
    }


    public Button getDeBut() {
        return deBut;
    }

    public Button getEnBut() {
        return enBut;
    }

    public Button getItBut() {
        return itBut;
    }

    public Image getFoto() {
        return foto;
    }

    public Label getCvDE() {
        return cvDE;
    }

    public Label getCvEN() {
        return cvEN;
    }

    public Label getCvIT() {
        return cvIT;
    }
}
