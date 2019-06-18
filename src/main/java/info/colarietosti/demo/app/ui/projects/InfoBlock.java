package info.colarietosti.demo.app.ui.projects;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;


public class InfoBlock extends CssLayout {

    private String name ;
    private String value;

    public InfoBlock(String name, String value) {
        this.addStyleName("cvinfoblock");
        this.name = name;
        this.value = value;


        Label lname = new Label( getName().equals("") ? "" :  getName()+":");
        Label lvalue = new Label( getValue());


        lname.addStyleName("cvline");
        addComponents(lname,lvalue);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
