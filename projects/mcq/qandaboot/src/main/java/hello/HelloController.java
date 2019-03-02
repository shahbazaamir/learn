package hello;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class HelloController {
	
	
	
	
	
	
	@Autowired
	private QuestionRepository  repository;
	
	
	@CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping("/question")
    public @ResponseBody List<Question> index() {
        return repository.findAll();
    }
    
}