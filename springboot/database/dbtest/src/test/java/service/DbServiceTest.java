package service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import repository.DbRepo;

@RunWith(MockitoJUnitRunner.class)
public class DbServiceTest {

    @InjectMocks
    DbServiceImpl service;

    @Mock
    DbRepo repo;

    @Test
    public void saveTest(){


        Mockito.doNothing(

        ).when(repo).  save();
        service.save();
    }

}
