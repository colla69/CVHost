package info.colarietosti.demo.app.backend.ipTrack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/backend")
@RestController
public class IpTrackController {

    private final IpTrackRepository ipTrackRepository;

    @Autowired
    public IpTrackController(IpTrackRepository ipTrackRepository) {
        this.ipTrackRepository = ipTrackRepository;
    }

    @GetMapping(value = "/visitors")
    public @ResponseBody List<IpTrack> getAllVisitors() {
        return ipTrackRepository.findAll();
    }
}
