package info.colarietosti.demo.app.backend.ipTrack;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class IpTrackService {

    @Autowired
    IpTrackRepository ipTrackRepository;

    public void trackNewVisit(String ip, String locale, Date time, String location, String referer){
        IpTrack ipEntry  = new IpTrack();
        ipEntry.setIp_addr(ip);
        ipEntry.setLocale(locale);
        ipEntry.setTimestamp(time);
        ipEntry.setLocation(location);
        ipEntry.setReferer(referer);
        ipTrackRepository.save(ipEntry);
    }

}
