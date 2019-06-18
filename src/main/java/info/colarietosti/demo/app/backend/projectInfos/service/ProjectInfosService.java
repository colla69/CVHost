package info.colarietosti.demo.app.backend.projectInfos.service;


import info.colarietosti.demo.app.backend.projectInfos.repository.ProjectInfosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectInfosService {

    @Autowired
    ProjectInfosRepository projectInfosRepository;
}
