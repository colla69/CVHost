package info.colarietosti.demo.app.backend.projectInfos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RequestMapping("/backend")
@RestController
public class ProjectInfosController {

    private final ProjectInfosRepository projectInfosRepository;

    @Autowired
    public ProjectInfosController(ProjectInfosRepository projectInfosRepository) {
        this.projectInfosRepository = projectInfosRepository;
    }

    @GetMapping(value="/projectInfos")
    public List<ProjectInfos> getProjInfos(){
        List<ProjectInfos> projectInfos = projectInfosRepository.findAll();
        Collections.sort(projectInfos);
        return projectInfos;
    }
}
