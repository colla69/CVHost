package info.colarietosti.demo.app.backend.projectInfos.repository;

import info.colarietosti.demo.app.backend.projectInfos.entity.ProjectInfos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectInfosRepository extends JpaRepository<ProjectInfos,Long> {

}
