package org.project.user;

import javax.ejb.Remote;

import model.PUser;

@Remote
public interface UserBeanRemote {


	PUser findUser(PUser user);

}
