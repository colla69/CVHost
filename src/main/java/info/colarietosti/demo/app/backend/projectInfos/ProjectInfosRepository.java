package info.colarietosti.demo.app.backend.projectInfos;

import info.colarietosti.demo.app.backend.projectInfos.ProjectInfos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectInfosRepository extends JpaRepository<ProjectInfos,Long> {

}
