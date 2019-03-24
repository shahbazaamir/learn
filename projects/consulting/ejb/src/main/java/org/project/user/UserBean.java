package org.project.user;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Country;
import model.PUser;

/**
 * Session Bean implementation class UserBean
 */
@Stateless
public class UserBean implements UserBeanRemote {

    /**
     * Default constructor. 
     */
    public UserBean() {
    }
    @PersistenceContext(unitName = "ConsultingJPA")
   	private EntityManager entityManager;


	@Override
	public PUser findUser(PUser user) {
		PUser p = entityManager.find(PUser.class, user.getUserId());
		return p;
	}
	

}
