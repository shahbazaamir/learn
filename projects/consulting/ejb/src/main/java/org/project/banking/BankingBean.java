package org.project.banking;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Account;
import model.Bank;
import model.BankingDetail;
import model.Branch;
import model.Country;
import model.Customer;

/**
 * Session Bean implementation class BankingBean
 */
@Stateless
public class BankingBean implements BankingBeanRemote {

    /**
     * Default constructor. 
     */
    public BankingBean() {
        // TODO Auto-generated constructor stub
    }
    
    @PersistenceContext(unitName = "ConsultingJPA")
	private EntityManager entityManager;

    @Override
	public void saveCountry(Country country) {
		entityManager.persist(country);
	}
    @Override
	public void saveBank(Country country,Bank bank) {
		//entityManager.persist(country);
    	country = entityManager.find(Country.class, country.getCountryCode());
    	//country.addBank(bank);
    	bank.setCountry(country);
    	entityManager.persist(bank);
    	
	}
    @Override
	public void modifyBank(Country country,Bank bank) {
    	country = entityManager.find(Country.class, country.getCountryCode());
    	List<Bank> banks = country.getBanks();
    	Bank modifiedBank = null ;
    	for(Bank b : banks){
    		if(b.getBankCode().equals(bank.getBankCode())){
    			modifiedBank = b ;
    			break;
    		}
    	}
    	modifiedBank.setBankName(bank.getBankName());
	}
	
    @Override
	public String searchAllCountry(String countryCode) {
		String q = "SELECT p from " + Country.class.getName() + " p";
		Query query = entityManager.createQuery(q);
		List<Country> countryList = query.getResultList();
		String jsonString = null ;
		JsonArray jsonArray = new JsonArray();
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = null ;
		for(Country country : countryList){
			jsonString  = country.toString();
			jsonObject = (JsonObject)parser.parse(jsonString);
			jsonArray.add(jsonObject);
		}
		Gson json = new Gson() ;
		return json.toJson(jsonArray) ;
	}
    
    @Override
	public String searchAllBank(String bankCode) {
		String q = "SELECT p from " + Bank.class.getName() + " p";
		Query query = entityManager.createQuery(q);
		List<Bank> BankList = query.getResultList();
		String jsonString = null ;
		JsonArray jsonArray = new JsonArray();
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = null ;
		for(Bank Bank : BankList){
			jsonString  = Bank.toString();
			jsonObject = (JsonObject)parser.parse(jsonString);
			jsonArray.add(jsonObject);
		}
		Gson json = new Gson() ;
		return json.toJson(jsonArray) ;
	}
    @Override
	public String searchAll(String modelName , String Code) {
		String q = "SELECT p from " + modelName + " p";
		Query query = entityManager.createQuery(q);
		List<Bank> BankList = query.getResultList();
		String jsonString = null ;
		JsonArray jsonArray = new JsonArray();
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = null ;
		for(Bank Bank : BankList){
			jsonString  = Bank.toString();
			jsonObject = (JsonObject)parser.parse(jsonString);
			jsonArray.add(jsonObject);
		}
		Gson json = new Gson() ;
		return json.toJson(jsonArray) ;
	}
    
	@Override
	public String searchCountry	(String countryCode) {
		Country p = entityManager.find(Country.class, countryCode);
		return p.toString();
	}
	@Override
	public void modifyCountry(Country country) {
		Country p = entityManager.find(Country.class, country.getCountryCode());
		p.setCurrencyCode(country.getCurrencyCode());
		p.setCountry(country.getCountry());
		
	}
	/*
	@Override
	public void modifyAccount(Account account , Customer customer) {
		customer = entityManager.find(Customer.class, customer.getCif());
    	List<Account> accounts = customer.getAccounts();
    	Account modifiedAccount = null ;
    	for(Account b : accounts){
    		if(b.getAccountNo().equals(account.getAccountNo())){
    			modifiedAccount = b ;
    			break;
    		}
    	}
    	modifiedAccount.setAccountType(account.getAccountType());
	}
	@Override
	public void addBranch(Branch branch , Bank bank) {
		bank = entityManager.find(Bank.class, bank.getBankCode());
		branch.setBank(bank);
		entityManager.persist(branch);
		
	}
	@Override
	public void modifyBranch(Branch branch , Bank bank) {
		bank = entityManager.find(Bank.class, bank.getBankCode());
    	List<Branch> branches = bank.getBranches();
    	Branch modifiedBranch = null ;
    	for(Branch b : branches){
    		if(b.getBranchCode().equals(branch.getBranchCode())){
    			modifiedBranch = b ;
    			break;
    		}
    	}
    	modifiedBranch.setBranchName(branch.getBranchName());
    	modifiedBranch.setBranchAddress(branch.getBranchAddress());
    	modifiedBranch.setSwiftCode(branch.getSwiftCode());
	}
	@Override
	public void addCustomer(Customer customer , Branch branch) {
		branch = entityManager.find(Branch.class, branch.getBranchCode());
		customer.setBranch(branch);
		entityManager.persist(customer);
		
	}
	@Override
	public void modifyCustomer(Customer customer , Branch branch) {
		branch = entityManager.find(Branch.class, branch.getBranchCode());
    	List<Customer> customeres = branch.getId().getCustomers();
    	Customer modifiedCustomer = null ;
    	for(Customer b : customeres){
    		if(b.getCif().equals(customer.getCif())){
    			modifiedCustomer = b ;
    			break;
    		}
    	}
    	modifiedCustomer.setCustomerName(customer.getCustomerName());
    	modifiedCustomer.setSwiftCode(customer.getSwiftCode());
	}
	@Override
	public void saveBankingDetail(BankingDetail baningDetail) {
		entityManager.persist(baningDetail);
	}
	@Override
	public void modifyBankingDetail(BankingDetail baningDetail) {
		//entityManager.find( BankingDetail.class ,  baningDetail.get);
	}
	*/
	@Override
	public void addBranch(Branch branch, Bank bank) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void modifyBranch(Branch branch, Bank bank) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addCustomer(Customer customer, Branch branch) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void modifyCustomer(Customer customer, Branch branch) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void modifyAccount(Account account, Customer customer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void saveBankingDetail(BankingDetail baningDetail) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void modifyBankingDetail(BankingDetail baningDetail) {
		// TODO Auto-generated method stub
		
	}
}
