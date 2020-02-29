package repository;

import org.springframework.stereotype.Repository;

@Repository
public class DbRepo {
    public boolean save(){
        System.out.println("repo save");
        return true;
    }
}
