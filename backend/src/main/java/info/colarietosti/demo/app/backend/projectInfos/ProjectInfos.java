package info.colarietosti.demo.app.backend.projectInfos;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "projectInfos")
public class ProjectInfos implements Comparable<ProjectInfos>  {

    @Id @GeneratedValue
    private int id;

    private String Name;
    private Date StartDate;
    private Date endDate;
    private String description;
    private String RoleName;
    private String lang;
    private String company;
    private String companyName;
    private String companyLink;
    private String companyLogo;
    private String image;

    @Override
    public int compareTo(ProjectInfos o) {
        return o.getStartDate().compareTo(this.getStartDate());
    }

}

