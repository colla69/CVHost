package info.colarietosti.demo.app.ui.projects;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

import java.util.ArrayList;
import java.util.List;

public class InfoGroup extends CssLayout {

    private List<InfoBlock> blocks = new ArrayList<>();
    private Label ltitle = new Label();

    public InfoGroup (String title){
        this.addStyleName("cvblock");
        ltitle.setValue(title);
        ltitle.addStyleName("title");
        addComponent(ltitle);
    }

    public void addInfoBlock(String name,String value) {
        InfoBlock i = new InfoBlock(name,value);
        addComponent(i);
    }
}
