package info.colarietosti.demo.app.backend.projectInfos;


import info.colarietosti.demo.app.backend.projectInfos.ProjectInfosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectInfosService {

    @Autowired
    ProjectInfosRepository projectInfosRepository;
}
