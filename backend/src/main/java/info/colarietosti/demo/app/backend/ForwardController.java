package info.colarietosti.demo.app.backend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardController {

    private static final Logger LOG = LoggerFactory.getLogger(ForwardController.class);

    // Forwards all routes to FrontEnd except: '/', '/index.html', '/api', '/api/**'
    // Required because of 'mode: history' usage in frontend routing, see README for further details
    @RequestMapping(value = "{ debugUrl:^[\\/](?!index)..*}")
    public String redirectApi(@PathVariable String debugUrl) {
        LOG.info("URL ("+debugUrl+") entered directly into the Browser, so we need to redirect...");
        return "forward:/";
    }

}