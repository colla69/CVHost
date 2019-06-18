package info.colarietosti.demo.app.ui.contact;

import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Link;
import org.vaadin.spring.annotation.PrototypeScope;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@PrototypeScope
@SpringComponent
public class ContactDesignImpl extends ContactDesign implements Serializable {

//https://www.hackerrank.com/colla69

    @PostConstruct
    public void init(){

        getEmail().setCaption("Email me!");
        getEmail().setResource(new ExternalResource("mailto:a.colarititosti@googlemail.com"));
        getEmail().setTargetName("_blank");

        getGithub().setCaption("GitHub");
        getGithub().setResource(new ExternalResource("http://github.com/colla69"));
        getGithub().setTargetName("_blank");

        getLinkedin().setCaption("Linkedin");
        getLinkedin().setResource(new ExternalResource("http://linkedin.com/in/actosti/"));
        getLinkedin().setTargetName("_blank");

        getHackeerrank().setCaption("Hackerrank");
        getHackeerrank().setResource(new ExternalResource("http://www.hackerrank.com/colla"));
        getHackeerrank().setTargetName("_blank");

    }


    public Link getEmail() {
        return email;
    }

    public Link getLinkedin() {
        return linkedin;
    }

    public Link getGithub() {
        return github;
    }

    public Link getHackeerrank() {
        return hackeerrank;
    }
}
