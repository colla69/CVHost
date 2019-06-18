package info.colarietosti.demo.app.ui.CV;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import info.colarietosti.demo.app.backend.ipTrack.IpTrackService;
import info.colarietosti.demo.app.ui.AppUI;
import info.colarietosti.demo.app.ui.projects.InfoGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;

import javax.annotation.PostConstruct;

@PrototypeScope
@SpringView(name = CVView.NAME,ui = AppUI.class)
public class CVView extends CssLayout implements View {

    public static final String NAME = "CV";

    @Autowired
    private IpTrackService ipTrackService;
    @Autowired
    CVContentImpl cvContent;

    @PostConstruct
    public void init(){
        this.addStyleName("cvview");
        String basepath = VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath();
        ExternalResource resource = new ExternalResource("https://raw.githubusercontent.com/colla69/Demo/master/src/main/webapp/WEB-INF/Images/foto.png");
        //FileResource resource = new FileResource(new File(basepath +"/WEB-INF/images/foto.png"));

        Image im = new Image();
        im.addStyleName("cvImage");
        im.setSource(resource);

        addComponent(im);
        addComponent( getPersonInfo() );
        addComponent( getberufsErfahrung() );
        addComponent( getSchool() );
        addComponent( getLangs() );
        addComponent( getMisc() );
        //addComponent( cvContent );
    }

    private InfoGroup getPersonInfo() {
        InfoGroup personal = new InfoGroup("PERSONAL INFORMATION");
        personal.addInfoBlock("Surname", "Colarieti Tosti");
        personal.addInfoBlock("Name", "Andrea");
        personal.addInfoBlock("Address", "Strassbergerstr. 06, 80809 München");
        personal.addInfoBlock("Cellphone", "+49 157 84 89 14 09");
        personal.addInfoBlock("E-Mail", "a.colarietitosti@googlemail.com");
        personal.addInfoBlock("Homepage", "http://www.colarietitosti.info");
        personal.addInfoBlock("Birthday", "33464");
        personal.addInfoBlock("Birthplace", "Rome ( Italy )");
        personal.addInfoBlock("Marital status", "single");
/*

        InfoGroup personal = new InfoGroup("PERSÖNLICHE INFORMATION");
        personal.addInfoBlock("name","Colarieti Tosti");
        personal.addInfoBlock("Vorname","Andrea");
        personal.addInfoBlock("Adresse","Strassbergerstr. 06 ,\n 80809 Monaco di Baviera ");
        personal.addInfoBlock("E-Mail","a.colarietitosti@googlemail.com");
        personal.addInfoBlock("Geburtsdatum/ -ort","14.08.1991, Rom ( Italien )");
        personal.addInfoBlock("Familienstand","ledig");*/
        return personal;
    }

    private InfoGroup getSchool() {
        InfoGroup personal = new InfoGroup("EDUCATION");


        personal.addInfoBlock("1997 - 2002","primary school “Walt Disney” in Rome");
        personal.addInfoBlock("2002 - 2005 ","middle school “Ignazio Silone” in Rome");
        personal.addInfoBlock("2005 - 2008 ","Scientific Gymnasium");
        personal.addInfoBlock("","“Niccoló Copernico” in Udine");
        personal.addInfoBlock("09.2008","Moved to Munich");
        personal.addInfoBlock("2008 - 2010 ","Scientific Gymnasium");
        personal.addInfoBlock("","Max-Plank Gymnasium München");
        personal.addInfoBlock("2010 - 2011","Scientific Gymnasium");
        personal.addInfoBlock("","“Voltaire” ( Italien )");
        personal.addInfoBlock("Graduation","70/100");
        personal.addInfoBlock("Apprenticeship","Specialist for application development");
        personal.addInfoBlock("2011 - 2013","71/100");
        personal.addInfoBlock("2018 - today","Ludwigs-Maximilians-Universität in Munich");
        personal.addInfoBlock("","faculty of Informatics and Mathematics");

        /*InfoGroup personal = new InfoGroup("SCHULABSCHLUSS");
        personal.addInfoBlock("1997 bis 2002","Grundschule “Walt Disney” in Rom");
        personal.addInfoBlock("2002 bis 2005","Mittlere Schule “Ignazio Silone” in Rom\n");
        personal.addInfoBlock("2005 bis 2008 ","Naturwissenschaftliches Gymnasium\n" +"Niccoló Copernico in Udine");
        personal.addInfoBlock("09.2008","Umzug nach München");
        personal.addInfoBlock("2008 bis 2010 ","Naturwissenschaftliches Gymnasium" );
        personal.addInfoBlock("","Max-Plank Gymnasium, München");
        personal.addInfoBlock("2010 bis 2011","Naturwissenschafliches Gymnasium\n");
        personal.addInfoBlock("","Voltaire ( Italien )");
        personal.addInfoBlock("Schulabschluss","70/100 (ca. 3,1)");
        personal.addInfoBlock("Ausbildung","Fachinformatiker, Anwendungsentwicklung");
        personal.addInfoBlock("2011 bis 2013","71/100");
        */
        return personal;
    }

    private InfoGroup getLangs() {

        InfoGroup personal = new InfoGroup("LANGUAGES");
        personal.addInfoBlock("Italian","first language");
        personal.addInfoBlock("English","fluently spoken and written,");
        personal.addInfoBlock("","2005  - B1 at the British Council in Rome");
        personal.addInfoBlock("","2004  - A2 at the British Council in Rome");
        personal.addInfoBlock("German","fluently spoken and written,");
        personal.addInfoBlock("","2010 - TestDaF at Goethe Institute in Munich");
        personal.addInfoBlock("","Reading skills: 5/5");
        personal.addInfoBlock("","Listening skills: 4/5");
        personal.addInfoBlock("","Writing skills: 4/5");
        personal.addInfoBlock("","Speaking skills: 5/5");
        personal.addInfoBlock("","2004 - B1 at Goethe Institute in Rome");
        personal.addInfoBlock("","2003 - A2 at Goethe Institute in Rome");
        personal.addInfoBlock("Latin", "4 years at High School");

        /*
        InfoGroup personal = new InfoGroup("BERUFSERFAHRUNG");
        personal
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }.addInfoBlock("Italienisch","Muttersprache");
        personal.addInfoBlock("Englisch","gute Kentnisse im Wort und Schrift");
        personal.addInfoBlock("","2004  Stufe A2 beim British Council in Rom\n");
        personal.addInfoBlock("","2005  Stufe B1 beim British Council in Rom");

        personal.addInfoBlock("Deutsch","gute Kentnisse im Wort und Schrift\n");
        personal.addInfoBlock("","2003  Stufe A2 beim Goethe Institut in Rom");
        personal.addInfoBlock("","2004  Stufe B1 beim Goethe Institt in Rom");
        personal.addInfoBlock("","2010  TestDaF beim Goethe Institut in München");
        personal.addInfoBlock("","Leseverstehen:          5");
        personal.addInfoBlock("","Hörverstehen:           4");
        personal.addInfoBlock("","Schriftlicher Ausdruck: 4");
        personal.addInfoBlock("","Mündlicher Ausdruck:    5");
        personal.addInfoBlock("Latein","4 jahre auf dem Italienischen Gymnasium");
        //personal.addInfoBlock("","");
        */
        return personal;

    }
    private InfoGroup getMisc(){
        InfoGroup personal = new InfoGroup("PERSONAL SKILLS & HOBBIES");
        personal.addInfoBlock("Skills","SQL");
        personal.addInfoBlock("","Linux  (Ubuntu, CentOS, Arch)");
        personal.addInfoBlock("","Java EE - Spring");
        personal.addInfoBlock("","Git");
        personal.addInfoBlock("","DevOps with Atlassian Products");
        personal.addInfoBlock("","Delphi / Pascal");
        personal.addInfoBlock("","Maven");
        personal.addInfoBlock("","Eclipselink / Hibernate");
        personal.addInfoBlock("","Microsoft Office, Adobe Photoshop (oder GIMP)");
        personal.addInfoBlock("","VoiP");
        personal.addInfoBlock("","Python");
        personal.addInfoBlock("","Django Framework");
        personal.addInfoBlock("Sports","Snowboard / Longboard riding");
        personal.addInfoBlock("","Biking (dirtjump, street, downhill)");
        personal.addInfoBlock("","Swimming");
        personal.addInfoBlock("","Triathlon (8 Years)");
        personal.addInfoBlock("","Gymnastics");
        personal.addInfoBlock("Hobbies & Interests","Reading,  Zen, Videogames, DIY, Speedcubing");
        /*
        InfoGroup personal = new InfoGroup("SONSTIGE KENNTNISSE & HOBBIES");

        personal.addInfoBlock("Informatik Kentnisse","SQL");
        personal.addInfoBlock("","Linux  (Ubuntu, CentOS, Arch)\n");
        personal.addInfoBlock("","Java EE - Spring\n");
        personal.addInfoBlock("","Git ");
        personal.addInfoBlock("","DevOps mit Atlassian Produkte");
        personal.addInfoBlock("","Maven ");
        personal.addInfoBlock("","Delphi / Pascal\n");
        personal.addInfoBlock("","Eclipselink / Hibernate\n");
        personal.addInfoBlock("","Microsoft Office, Adobe Photoshop (oder GIMP)\n");
        personal.addInfoBlock("","VoiP ");
        personal.addInfoBlock("Sports","Snowboard / Longboard\n");
        personal.addInfoBlock("","Fahrradfahren (dirtjump, street, downhill)");
        personal.addInfoBlock("","Schwimmen");
        personal.addInfoBlock("","Triathlon (8 Jahre – Leistungssport)");
        personal.addInfoBlock("","Turnen");
        personal.addInfoBlock("Hobbies & Interessen","Lesen, Zen, Videogames, Basteln, Speedcubing");
        */
        //personal.addInfoBlock("","");
        return personal;

    }

    private InfoGroup getberufsErfahrung() {
        InfoGroup personal = new InfoGroup("WORK EXPERIENCE");
        personal.addInfoBlock("2018 - today", "DevOps developer at MsgGillardon AG");
        personal.addInfoBlock("", "- Planning and writing of testcases for heidi");
        personal.addInfoBlock("2013 - 2018", "Application developer at 3Points Software Gmbh");
        personal.addInfoBlock("","- Development of new Product Versions");
        personal.addInfoBlock("","- Remote Deployments and support");
        personal.addInfoBlock("","- Planning and development of databases");
        personal.addInfoBlock("","- Development of Springboot applications");
        /*
        InfoGroup personal = new InfoGroup("BERUFSERFAHRUNG");

        personal.addInfoBlock("2013 bis heute" ,"Anwndungsentwickler bei 3Points Software Gmbh" );
        personal.addInfoBlock("","- Entwicklung neuer Produktversionen ");
        personal.addInfoBlock("","- Pflege der Einspielungsprozesse");
        personal.addInfoBlock("","- Erstellen und einrichten von Datenbanken");
        personal.addInfoBlock("","- Entwicklung mit Springboot");
        */
        return personal;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
