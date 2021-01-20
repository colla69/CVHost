package info.colarietosti.demo.app.backend.ipTrack;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ip_track")
public class IpTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ip_addr;
    private String locale;
    private String location;
    private String referer;
    private Date timestamp;
}
