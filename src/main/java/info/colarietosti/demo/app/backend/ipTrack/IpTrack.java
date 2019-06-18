package info.colarietosti.demo.app.backend.ipTrack;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ip_track")
@Getter @Setter
@NoArgsConstructor
public class IpTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ip_addr;
    private String locale;
    private String location;
    private Date timestamp;
}
