package org.project.naming;

public class EJBReferences {
	public static final String COMMON_EJB = "java:app/Bean2/CommonSessionBean!org.project.common.ejb.CommonSessionBeanInterfaceRemote";
	public static final String ACCOUNT_EJB = "java:app/Bean2/AccountBean!org.project.banking.AccountRemote";
	public static final String CUSTOMER_EJB = "java:app/Bean2/CustomerBean!org.project.banking.customer.CustomerBeanRemote";
	public static final String BANKING_EJB = "java:app/Bean2/BankingBean!org.project.banking.BankingBeanRemote";
	public static final String USER_EJB = "java:app/Bean2/UserBean!org.project.user.UserBeanRemote";
}
