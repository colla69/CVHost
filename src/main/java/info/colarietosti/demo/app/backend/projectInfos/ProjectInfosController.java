package info.colarietosti.demo.app.backend.projectInfos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProjectInfosController {

    private final ProjectInfosRepository projectInfosRepository;

    @Autowired
    public ProjectInfosController(ProjectInfosRepository projectInfosRepository) {
        this.projectInfosRepository = projectInfosRepository;
    }

    @GetMapping(value="/projectInfos")
    public List<ProjectInfos> getProjInfos(){
        return projectInfosRepository.findAll();
    }
}
