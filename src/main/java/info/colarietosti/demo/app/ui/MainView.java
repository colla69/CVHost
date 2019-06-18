package info.colarietosti.demo.app.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.JavaScript;
import info.colarietosti.demo.app.ui.CV.CVView;
import info.colarietosti.demo.app.ui.CV.QualificationView;
import info.colarietosti.demo.app.ui.CV.WelcomeView;
import info.colarietosti.demo.app.ui.contact.ContactView;
import info.colarietosti.demo.app.ui.navigation.NavigationManager;
import info.colarietosti.demo.app.ui.news.NewsView;
import info.colarietosti.demo.app.ui.projects.ProjectsView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@SpringViewDisplay
@UIScope
public class MainView extends MainViewDesign implements ViewDisplay {

	private final Map<Class<? extends View>, Button> navigationButtons = new HashMap<>();
	private final NavigationManager navigationManager;

	@Autowired
	public MainView(NavigationManager navigationManager) {
		this.navigationManager = navigationManager;
    }

	@PostConstruct
	public void init() {
        attachNavigation(cv, CVView.class);
        attachNavigation(welcome, WelcomeView.class);
        attachNavigation(contact, ContactView.class);
        attachNavigation(qualifications, QualificationView.class);
        attachNavigation(projects, ProjectsView.class);
        attachNavigation(news, NewsView.class);

        content.addStyleName("main");
	}


    private void makeDashButton() {
		dash.addClickListener(event -> getUI().getPage()
				.open("https://dash.colarietitosti.info","_blank"));

	}
    private void makeDwnlButton() {
        BrowserWindowOpener opener = new BrowserWindowOpener(
                Page.getCurrent().getLocation().toString().substring(0,Page.getCurrent().getLocation().toString().indexOf('#'))
                        +"files/CV_Docs.zip");
        opener.extend(dcv);
        opener.setUriFragment("editwhatever"); // lässt in new Tab öffnen
    }

	private void attachNavigation(Button navigationButton, Class<? extends View> targetView) {
		navigationButtons.put(targetView, navigationButton);
		navigationButton.addClickListener(e -> navigationManager.navigateTo(targetView));
	}

	@Override
	public void showView(View view) {
		JavaScript.eval("document.getElementById('content').scrollTop=0");
		content.removeAllComponents();
		content.addComponent(view.getViewComponent());
		navigationButtons.forEach((viewClass, button) -> button.setStyleName("selected", viewClass == view.getClass()));

		Button menuItem = navigationButtons.get(view.getClass());
		String viewName = "";
		if (menuItem != null) {
			viewName = menuItem.getCaption();
		}
		makeDwnlButton();
		makeDashButton();
		activeViewName.setValue(viewName);
	}

}
