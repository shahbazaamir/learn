package org.project.banking;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.framework.DataType;
//import org.project.log.LogWriter;
import org.project.log.LogWriter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Account;
import model.Customer;


/**
 * Session Bean implementation class Account
 */
@Stateless
public class AccountBean implements AccountRemote {

	/**
	 * Default constructor. 
	 */
	public AccountBean() {
		// TODO Auto-generated constructor stub
	}

	//@Override
	public List<Object> select() {

		return null;
	}
	@PersistenceContext(unitName = "ConsultingJPA")
	private EntityManager entityManager;

/*
	@Override
	public void saveAccount(Account account , Customer customer) {
		 Customer p = entityManager.find(Customer.class, customer.getCif());
		account.getId().setCustomer(p);
		entityManager.persist(account);
	}
*/
	//@Override
	public Account findAccount(Account account) {
	//	Account p = entityManager.find(Account.class, account.getAccountNo());
		return null;
	}
	/*
	@Override
	public String findAccount(Account account , String dataType){
		String returnObj = null ;

		Account p = entityManager.find(Account.class, account. getAccountNo());
		LogWriter.writeLog("logloglog11");
		p.getCustomer();
		switch (dataType){
		case DataType.JSON_STRING :
			returnObj  =  p.toString();
			break ; 
			/*
    		case DataType.JSON_OBJECT :
    			Gson json = new Gson() ;
    			LogWriter.writeLog("hello");
    			LogWriter.writeLog("hello");
    			LogWriter.writeLog("hello");
    			//String s= json.toJson(p);
    			String s = p.toJson();
    			LogWriter.writeLog(s);
    			JsonParser parser = new JsonParser();
    			JsonObject o = (JsonObject)parser.parse(s);
    			returnObj = o ;
    		break;
			 */
	/*
		default :
			returnObj =  null;
		}
		return returnObj ;

	}
	*/
	//@Override
	public String searchAccount(String accountNo , String dataType){
		String jsonString = null ;
		JsonArray jsonArray = new JsonArray();
		List<Account> accountList = searchAccount(accountNo);
		LogWriter.writeLog("logloglog122222222222221");
		switch (dataType){
		case DataType.JSON_STRING :

			JsonParser parser = new JsonParser();
			JsonObject jsonObject = null ;
			for(Account account : accountList){
				jsonString  = account.toString();
				jsonObject = (JsonObject)parser.parse(jsonString);
				jsonArray.add(jsonObject);
			}
			break ; 
		default :
		}
		Gson json = new Gson() ;
		return json.toJson(jsonArray) ;
	}
	@Override
	public List<Account> searchAccount(String searchKeyword) {
		TypedQuery<Account> query = entityManager.createQuery(" select a from " + Account.class.getName() +"  a where a.accountNo LIKE :searchKeyword ",Account.class);
		query.setParameter("searchKeyword", "%"+searchKeyword+"%");
		return query.getResultList();
	}

	@Override
	public List<Account> retrieveAllAccounts() {

		String q = "SELECT p from " + Account.class.getName() + " p";
		Query query = entityManager.createQuery(q);
		List<Account> accounts = query.getResultList();
		return accounts;
	}

	@Override
	public void saveAccount(Account account, Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String findAccount(Account account, String dataType) {
		// TODO Auto-generated method stub
		return null;
	}
}
