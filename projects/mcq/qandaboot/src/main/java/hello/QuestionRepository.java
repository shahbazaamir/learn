 package hello;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, String> {

    public Question findByQuestionId(String questionId);
    public List<Question> findBySubjectId(String subjectId);

}
