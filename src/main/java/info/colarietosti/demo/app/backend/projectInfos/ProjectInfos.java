package info.colarietosti.demo.app.backend.projectInfos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "projectInfos")
@Getter @Setter @NoArgsConstructor
public class ProjectInfos {

    @Id @GeneratedValue
    private int id;

    private String Name;
    private Date StartDate;
    private Date endDate;
    private String description;
    private String RoleName;
    private String lang;
    private String company;
}
