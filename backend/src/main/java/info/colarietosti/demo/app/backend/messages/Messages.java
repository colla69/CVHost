package info.colarietosti.demo.app.backend.messages;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sender;
    private String email;
    private String message;

}