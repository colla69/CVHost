package info.colarietosti.demo.app.ui.CV;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import info.colarietosti.demo.app.backend.ipTrack.IpTrackService;
import info.colarietosti.demo.app.ui.AppUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;

import javax.annotation.PostConstruct;
import java.util.Calendar;

@PrototypeScope
@SpringView(ui = AppUI.class)
public class QualificationView extends CssLayout implements View {

    @Autowired
    private IpTrackService ipTrackService;
    CssLayout buttonwrap = new CssLayout();
    Label shower = new Label();
    public static final String URL = Page.getCurrent().getLocation().toString().substring(0,Page.getCurrent().getLocation().toString().indexOf('#'));

    @PostConstruct
    private void init(){
        makebuttons();
        shower.setContentMode(ContentMode.HTML);
        shower.addStyleName("shower");
        shower.setSizeFull();
        shower.setValue(makeEmbedded(QualificationEnum.threePZ.getLink()));
        this.addComponent(buttonwrap);
        this.addComponent(shower);
        this.setSizeFull();
        this.addStyleName("qualificationview");
    }

    private String makeEmbedded( String source){
        return //"<div>"+
                "<embed src=\""+URL+"files/"+source+"\" width=\"100%\" height=\"100%\" alt=\"pdf\" "+
                "pluginspage=\"http://www.adobe.com/products/acrobat/readstep2.html\">";//+"</div>";
    }

    private void makebuttons() {
        buttonwrap.setHeightUndefined();
        buttonwrap.setWidth("100%");
        for (QualificationEnum q : QualificationEnum.values()){
            Button b = new Button(q.getName());
            b.addStyleName("buttonq");
            b.addClickListener(clickEvent -> {
                shower.setValue(makeEmbedded(q.getLink()));
                shower.setHeight("100%");
            });
            buttonwrap.addComponent(b);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        final WebBrowser webBrowser = UI.getCurrent().getSession().getBrowser();
        ipTrackService.trackNewVisit(
                webBrowser.getAddress(),
                webBrowser.getLocale().toString(),
                Calendar.getInstance().getTime(),
                "Qualifications"
        );
    }
}
