package info.colarietosti.demo.app.ui.contact;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;
import info.colarietosti.demo.app.backend.ipTrack.IpTrackService;
import info.colarietosti.demo.app.ui.AppUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;

import javax.annotation.PostConstruct;

@PrototypeScope
@SpringView(name=ContactView.NAME,ui = AppUI.class)
public class ContactView extends CssLayout implements View {

    protected static final String NAME = "Contact";

    @Autowired
    private IpTrackService ipTrackService;
    @Autowired
    private ContactDesignImpl aboutDesign;

    @PostConstruct
    public void init(){
        this.setSizeFull();
        aboutDesign.setSizeUndefined();

        addComponent(aboutDesign);
    }

}
