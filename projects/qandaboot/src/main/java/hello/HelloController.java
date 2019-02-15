package hello;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
	@CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping("/question")
    public String index() {
        return "[{\"id\":\"1\",\"questionDesc\":\"What is java?\"}]";
    }
    
}