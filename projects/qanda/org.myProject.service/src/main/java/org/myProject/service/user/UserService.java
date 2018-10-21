package org.myProject.service.user;

public interface UserService {
	public abstract UserDetails createUser (UserDetails userDetails);
	public abstract UserDetails suspendUser (UserDetails userDetails);
	public abstract UserDetails loginUser (UserDetails userDetails);
	public abstract UserDetails logoutUser (UserDetails userDetails);
	
}
