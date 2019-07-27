package info.colarietosti.demo.app.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import info.colarietosti.demo.app.backend.ipTrack.IpTrackService;
import info.colarietosti.demo.app.ui.navigation.NavigationManager;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;

@Theme("apptheme")
@SpringUI(path = "")
@Viewport("width=device-width,initial-scale=1.0,user-scalable=no")
@Title("Colarieti CV")
@Log
public class AppUI extends UI {

    private final NavigationManager navigationManager;
    private final MainView mainView;


    @Autowired
    private IpTrackService ipTrackService;

    @Autowired
    public AppUI( NavigationManager navigationManager, MainView mainView) {
        this.navigationManager = navigationManager;
        this.mainView = mainView;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(mainView);
        navigationManager.navigateToDefaultView();
        String realIP = "";
        String loc = "";
        String referer = "";
        for (final java.util.Enumeration vias = vaadinRequest.getHeaders("X-Real-IP"); vias.hasMoreElements(); ) {
            realIP = (String) vias.nextElement();
        }
        for (final java.util.Enumeration vias = vaadinRequest.getHeaders("referer"); vias.hasMoreElements(); ) {
            referer = (String) vias.nextElement();
        }
        for (final java.util.Enumeration vias = vaadinRequest.getHeaders("accept-language"); vias.hasMoreElements(); ) {
            loc = (String) vias.nextElement();
        }
        ipTrackService.trackNewVisit(
                realIP,
                loc,
                Calendar.getInstance().getTime(),
                "PAGE_CALL",
                referer
        );
    }
}
