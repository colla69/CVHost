package info.colarietosti.demo.app.backend.messages;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<Messages,Long> {

}
