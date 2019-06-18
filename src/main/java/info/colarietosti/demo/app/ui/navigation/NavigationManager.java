package info.colarietosti.demo.app.ui.navigation;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.spring.navigator.SpringNavigator;
import info.colarietosti.demo.app.ui.CV.WelcomeView;
import org.springframework.stereotype.Component;

/**
 * Governs view navigation of the app.
 */
@Component
@UIScope
public class NavigationManager extends SpringNavigator{

	public String getViewId(Class<? extends View> viewClass) {
		SpringView springView = viewClass.getAnnotation(SpringView.class);
		if (springView == null) {
			throw new IllegalArgumentException("The target class must be a @SpringView");
		}
		return Conventions.deriveMappingForView(viewClass, springView);
	}


	public void navigateTo(Class<? extends View> targetView) {
		String viewId = getViewId(targetView);
        navigateTo(viewId);
	}

	public void navigateToDefaultView() {
		navigateTo(WelcomeView.class);
	}
}
