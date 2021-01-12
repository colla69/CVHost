package info.colarietosti.demo.app.ui.projects;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import info.colarietosti.demo.app.backend.projectInfos.ProjectInfos;
import info.colarietosti.demo.app.backend.projectInfos.ProjectInfosRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

@SpringView
@Log
public class ProjectsView extends CssLayout implements View {

    @Autowired
    ProjectInfosRepository projectInfosRepository;

    private boolean ascending = true;
    CssLayout titlewrap = new CssLayout();
    CssLayout contentwrap = new CssLayout();

    @PostConstruct
    private void init() {
        this.addStyleName("projectsView");
        Label title = new Label(" My Projects");
        title.addStyleName("projLabel");
        title.setWidth("93%");
        titlewrap.addComponent(title);
        titlewrap.addStyleName("titlewrap");
        Button b = new Button("sort");
        b.addClickListener(clickEvent -> makeSortedProjectInfos(!ascending));
        b.addStyleName("sortbutton");
        titlewrap.addComponent(b);
        makeSortedProjectInfos(ascending);
        this.addComponents(titlewrap,contentwrap);
    }

    private List<ProjectInfos> makeSortedProjectInfos(boolean dateAsc) {
        ascending = dateAsc;
        contentwrap.removeAllComponents();
        List<ProjectInfos> pInfos = projectInfosRepository.findAll();


        if (dateAsc) {
            pInfos.sort( (ProjectInfos p1,ProjectInfos p2)-> p2.getStartDate().compareTo(p1.getStartDate()));
        } else {
            pInfos.sort(Comparator.comparing(ProjectInfos::getStartDate));
        }

        int i = 0;
        for (ProjectInfos pi : pInfos) {
            i++;
            ProjectComp p = new ProjectComp(pi.getStartDate(), pi.getEndDate(),
                    pi.getName(), pi.getDescription(), pi.getRoleName(), pi.getLang(), pi.getCompany());
            if ((i % 2) == 0) {
                p.addStyleName("darkpane");
                //log.info("dark "+i);
            } else {
                p.addStyleName("lightpane");
                //log.info("light "+i);
            }
            if (dateAsc){
                p.switchDates();
            }
            contentwrap.addComponent(p);
        }
        return pInfos;
    }
}
