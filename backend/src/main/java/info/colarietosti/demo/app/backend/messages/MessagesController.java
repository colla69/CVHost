package info.colarietosti.demo.app.backend.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/backend")
@RestController
public class MessagesController {

    private final MessagesRepository messagesRepository;

    @Autowired
    public MessagesController(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @GetMapping("/messages")
    public @ResponseBody List<Messages> getMessages(){
        return messagesRepository.findAll();
    }

    @PostMapping(path = "/saveMessage")
    @ResponseBody
    public HttpStatus postMessage(@RequestBody Messages message){
        messagesRepository.save(message);
        return HttpStatus.OK;
    }

}
