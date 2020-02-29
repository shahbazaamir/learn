package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.DbRepo;

@Component
public class DbServiceImpl implements DbService {

    @Autowired
    DbRepo repo;

    @Override
    public void save() {
        repo.save();
        System.out.println("saved");
    }
}
