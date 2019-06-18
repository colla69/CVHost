package info.colarietosti.demo.app.ui.projects;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import info.colarietosti.demo.app.ui.utils.HtmlFuncs;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.vaadin.spring.annotation.PrototypeScope;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Log
@PrototypeScope
@Getter @Setter
public class ProjectComp extends CssLayout {

    private Date sdate;
    private Date edate;
    private String name;
    private String description;
    private String rolename;
    private String lang;
    private String company;

    Label startl;
    Label endl;
    CssLayout titlewrap = new CssLayout();
    DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");


    public ProjectComp(Date sdate, Date edate, String name, String description, String rolename, String lang, String company) {
        this.sdate = sdate;
        this.edate = edate;
        this.name = name;
        this.lang = lang;
        this.description = description;
        this.rolename = rolename;
        this.company = company;
        init();
    }

    private void init(){
        this.addStyleName("projectPane");
        Label l = new Label( "written in: "+this.getLang() );
        Label n = new Label( this.getName() );
        Label r = new Label(" as " + this.getRolename() + " for " + this.getCompany());
        Label description = new Label();

        description.setContentMode(ContentMode.HTML);
        description.setValue(HtmlFuncs.makeEmbedded(this.getDescription()));
        description.addStyleName("descriptionline");
        description.setResponsive(true);

        l.addStyleName("role");
        l.setContentMode(ContentMode.HTML);

        startl = new Label( "> " + dateFormat.format(getSdate()) );
        startl.addStyleName("dateline");
        endl = new Label( "> " + dateFormat.format(getEdate()) );

        titlewrap.addStyleName("titlepanel");
        n.addStyleName("title");
        r.setContentMode(ContentMode.HTML);
        r.addStyleName("role");

        endl.addStyleName("dateline");
        titlewrap.addComponent(n);
        titlewrap.addComponent(r);
        this.addComponent(startl);
        this.addComponent(titlewrap);

        this.addComponent(l);
        this.addComponent(description);
        this.addComponent(endl);
    }

    public void switchDates(){
        Date end = this.getEdate();
        this.setEdate( getSdate() );
        this.setSdate( end );
        startl.setValue( "> " + dateFormat.format(getSdate()) );
        endl.setValue( "> " + dateFormat.format(getEdate()) );
    }
}
