package info.colarietosti.demo.app.ui.CV;

import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import info.colarietosti.demo.app.backend.ipTrack.IpTrackService;
import info.colarietosti.demo.app.ui.news.NewsCardsGetter;
import info.colarietosti.demo.app.ui.utils.HtmlFuncs;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;

import javax.annotation.PostConstruct;

@PrototypeScope
@SpringView
@Log
public class WelcomeView extends CssLayout implements View {

    @Autowired
    private IpTrackService ipTrackService;
    private FileResource[] resources;

    final private String INTRO_MESSAGE = "<h1>Welcome to my Homepage! :)</h1>\n" +
            "<p>My name is Andrea Colarieti Tosti and am glad to welcome you to my Guest portal!<br />This page only has one function: it allows you to dowload my CV and enables me to demonstrate<br />a little bit of my Programming and Administration skills.</p>\n" +
            "<h3>About Me:</h3>\n" +
            "<p>I was born in Rome in August '91, I can remember how hot it was and the deep feeling that something was off.... my computer was missing!&nbsp;<br />Since then I nurtured my passion for computers and electronics in any way I could think of.<br />As a kid I wrote an interactive tourist guide about places in Rome as I was learning about them in school, then my glorious career continued Through very small private projects hand tuning configuration files and simple administration of my home Network.<br />After that, I stopped programming as my \"gaming phase\" kicked-in, and I started programming again after high school with my first trainee job.<br />With the first projects I learned about loops and data structures and everything started making sense.<br />I was ready to take on my first project! Writing a chess game, mostly to understand how to use all the newly learned notions.<br />After a lot of failed tries it was finally working! I will never forget that feeling... I am deeply thankful to be able to continue to pursue that feeling in my day by day, working as a programmer.<br />The story continues with many productive projects, the biggest ones being part of the <a href=\"https://www.3points.de/\">mediafusion</a> project.<br />Soon enough my need to learn more lead me to search for new challenges, and motivated me to continue my studies at the <a href=\"https://www.uni-muenchen.de/index.html\">Ludwig-Maximilian-Universit&auml;t</a> in Munich.<br />Did I poke your interest? </p>\n" +
            "<h3><a href=\"https://cv.colarietitosti.info/#!news\">Read my News</a> or "+
            "<a href=\"https://cv.colarietitosti.info/#!projects\">Read about my fullfilled projects</a> <br /><a href=\"https://cv.colarietitosti.info/#!CV\">Read my CV</a><br /> <a href=\"https://cv.colarietitosti.info/files/CV_Docs.zip\">Download my CV</a></h3>\n" +
            "<h3>Visit me on GitHub: <a href=\"https://github.com/colla69\" target=\"_blank\">take me there!</a></h3>"+
            "<p>&nbsp; </p>";
/*
<p>/*</p>
<h1>Welcome to my Homepage! :)</h1>
<p>My name is Andrea Colarieti Tosti and am glad to welcome you to my Guest portal!<br />This page only has one function: it allows you to dowload my CV and enables me to demonstrate<br />a little bit of my Programming and Administration skills.</p>Delphi XE 7
<h3>About Me:</h3>
<p>I was born in Rome in August '91, I can remember how hot it was and the deep feeling that something was off.... my computer was missing!&nbsp;<br />Since then I nurtured my passion for computers and electronics in any way I could think of.<br />As a kid I wrote an interactive tourist guide about places in Rome as I was learning about them in school, then my glorious career continued Through very small private projects hand tuning configuration files and simple administration of my home Network.<br />After that, I stopped programming as my "gaming phase" kicked-in, and I started programming again after high school with my first trainee job.<br />With the first projects I learned about loops and data structures and everything started making sense.<br />I was ready to take on my first project! Writing a chess game, mostly to understand how to use all the newly learned notions.<br />After a lot of failed tries it was finally working! I will never forget that feeling... I am deeply thankful to be able to continue to pursue that feeling in my day by day, working as a programmer.<br />The story continues with a many productive projects, the biggest ones being part of the <a href="https://www.3points.de/">mediafusion</a> project.<br />Soon enough my need to learn more lead me to search for new challenges, and motivated me to continue my studies at the <a href="https://www.uni-muenchen.de/index.html">Ludwig-Maximilian-Universit&auml;t</a> in Munich.<br />Did I poke your interest? </p>
<h3><a href="https://cv.colarietitosti.info/#!projects">Read about my fullfilled projects</a> <br /><a href="https://cv.colarietitosti.info/#!CV">Read my CV</a><br /> <a href="https://cv.colarietitosti.info/files/CV_Docs.zip">Download my CV</a></h3>
<p>.&nbsp; </p>
*/

    @Autowired
    NewsCardsGetter newsCardsGetter;

    private final CssLayout left = new CssLayout();
    private final CssLayout right = new CssLayout();
    static final String URL = Page.getCurrent().getLocation().toString();


    @PostConstruct
    public void init(){
        this.addStyleName("welcome");
        this.setWidth("100%");

        final Label intro = new Label();
        intro.setContentMode(ContentMode.HTML);
        intro.setWidth("100%");
        intro.setValue(HtmlFuncs.makeEmbedded(INTRO_MESSAGE));
        intro.addStyleName("intro");

        final String basepath = VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath();

        final ExternalResource resource = new ExternalResource(URL+"/files/CVShort.jpg");
        final Label foot = new Label();
        foot.setContentMode(ContentMode.HTML);
        foot.setWidth("100%");
        foot.setValue(HtmlFuncs.makeEmbedded("Klick the image to Zoom IN/OUT:"));
        foot.setStyleName("foot");

        final Image im = new Image();
        im.setSource(resource);
        im.setHeight("300px");

        im.addClickListener(clickEvent -> {
            final Window show = new Window();
            final Image i = new Image();
            i.setSource( im.getSource() );
            show.setContent(i);
            show.setHeight("90%");
            i.setHeight("100%");
            show.center();
            show.setClosable(true);
            show.setModal(true);

            show.addClickListener(clickEvent1 -> {
                if (show != null) {
                    show.close();
                }
            });
            UI.getCurrent().addWindow(show);
        });

        Label newsHeader = new Label("<h1>Read my News</h1>");
        newsHeader.setContentMode(ContentMode.HTML);
        right.addStyleName("right");

        left.addComponents(intro,foot,im);
        right.addComponent( newsHeader );
        newsCardsGetter.getAndEmbedNews(right);

        this.addComponents(left,right);

    }

}
