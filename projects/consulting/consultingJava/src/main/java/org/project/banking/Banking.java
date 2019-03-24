package org.project.banking;

import java.util.HashMap;

import model.Account;
import model.AccountPK;
import model.Bank;
import model.BankingDetail;
import model.Branch;
import model.Country;
import model.Customer;

import org.framework.DataType;
import org.framework.module.Module;
import org.framework.vo.ValueObject;
import org.project.banking.customer.CustomerBeanRemote;
import org.project.common.ejb.EJBManager;
import org.project.common.vo.CommonVO;
import org.project.common.web.CommonParams;
import org.project.log.LogWriter;
import org.project.naming.EJBReferences;
import org.project.naming.JSPReference;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Banking implements Module{
	public static final String ACCOUNT_SEARCH = "AccountsSearch" ;
	
	public static final String COUNTRY_ADD = "CountryAdd";
	public static final String COUNTRY_SEARCH ="CountrySearch";
	public static final String COUNTRY_MODIFY ="CountryModify";
	public static final String COUNTRY_FIND =  "CountryFind";
	public static final String BANK_ADD = "BankAdd";
	public static final String BANK_MODIFY = "BankModify";
	public static final String BANK_SEARCH = "BankSearch";
	
	public static final String BRANCH_ADD = "BranchAdd";
	public static final String BRANCH_MODIFY = "BranchModify";
	public static final String CUSTOMER_ADD = "customerAdd";
	public static final String CUSTOMER_MODIFY = "CustomerModify";
	public static final String ACCOUNT_ADD = "AccountsAdd" ;
	public static final String ACCOUNT_MODIFY = "AccountsModify" ;
	public static final String BANKING = "Banking" ;
	
	/**
	 * 
	 * 
	 * @since 1.7
	 */
	//@Override
	public ValueObject processRequest(ValueObject valueObject) {
		LogWriter.writeLog("Banking IN ::");
		CommonVO objCommonVO = (CommonVO) valueObject ;
		try{
			String reqId = (String)objCommonVO.getParamHm().get(CommonParams.REQUEST_ID);
			//objCommonVO = getAllAccounts(objCommonVO);
			switch(reqId){
			case ACCOUNT_SEARCH :
				objCommonVO =searchAccount(objCommonVO);
				break;
			case ACCOUNT_ADD : 
				objCommonVO =saveAccount(objCommonVO);
				break;
			case COUNTRY_ADD :
				objCommonVO =saveCountry(objCommonVO);
				break;
			case BANK_ADD :
				objCommonVO =addBank(objCommonVO);
				break;	
			case COUNTRY_SEARCH :
				objCommonVO =searchCountry(objCommonVO);
				break;
			case COUNTRY_MODIFY :
				objCommonVO =modifyCountry(objCommonVO);
				break;
				
			case COUNTRY_FIND :
				objCommonVO =findCountry(objCommonVO);
				break;
			case BANK_MODIFY :
				objCommonVO =modifyBank(objCommonVO);
				break;
			case BANK_SEARCH :
				objCommonVO =searchBank(objCommonVO);
				break;
			case BRANCH_ADD :
				objCommonVO =addBranch(objCommonVO);
				break;
				
			case CUSTOMER_ADD :
				objCommonVO =addCustomer(objCommonVO);
				break;
		case CUSTOMER_MODIFY :
			objCommonVO =modifyCustomer(objCommonVO);
			break;
		case ACCOUNT_MODIFY:
			objCommonVO =modifyAccount(objCommonVO);
			break;
		case BANKING:
			objCommonVO = banking(objCommonVO);
			break;
				
			
		}
		}catch(Exception e){
			e.printStackTrace();
			objCommonVO.setResponsePage(JSPReference.BANKING_ERR);
		}finally{
			LogWriter.writeLog("Banking OUT ::");
		}
		return objCommonVO;
	}
	
	private CommonVO banking(CommonVO objCommonVO) throws Exception {
		BankingBeanRemote bankingBean = (BankingBeanRemote)  EJBManager.getEJBObject(EJBReferences.BANKING_EJB);
		BankingDetail baningDetail = new BankingDetail();
		baningDetail.setCountry((String)objCommonVO.getParamHm().get(CommonParams.COUNTRY));
		baningDetail.setBank((String)objCommonVO.getParamHm().get(CommonParams.BANK_NAME));
		baningDetail.setBranch((String)objCommonVO.getParamHm().get(CommonParams.BRANCH_NAME));
		baningDetail.setCif((String)objCommonVO.getParamHm().get(CommonParams.CIF));
		baningDetail.setAccountNo((String)objCommonVO.getParamHm().get(CommonParams.ACCOUNT_NO));
		baningDetail.setDebitCardDetails((String)objCommonVO.getParamHm().get(CommonParams.DEBIT_CARD_NO));
		baningDetail.setCreditCardDetails((String)objCommonVO.getParamHm().get(CommonParams.CREDIT_CARD_NO));
		baningDetail.setInternetBankingDetails((String)objCommonVO.getParamHm().get(CommonParams.INTERNET_BANING_NO));
		bankingBean.saveBankingDetail(baningDetail);
		return objCommonVO ;
	}

	private CommonVO modifyCustomer(CommonVO objCommonVO) {
		// TODO Auto-generated method stub
		return null;
	}
	private CommonVO modifyAccount(CommonVO objCommonVO) throws Exception {
		BankingBeanRemote bankingBean = (BankingBeanRemote)  EJBManager.getEJBObject(EJBReferences.BANKING_EJB);
		Account account = new Account();
		Customer customer = new Customer();
		customer.getId().setCif((String)objCommonVO.getParamHm().get(CommonParams.CIF));
		AccountPK accountPK = new AccountPK();
		//accountPK.se
		//account.
		//account.setAccountNo( (String)objCommonVO.getParamHm().get(CommonParams.ACCOUNT_NO));
		account.setAccountType((String)objCommonVO.getParamHm().get(CommonParams.ACCOUNT_TYPE));
		bankingBean.modifyAccount(account, customer);
		return objCommonVO;
	}
	private CommonVO addCustomer(CommonVO objCommonVO) throws Exception {
		BankingBeanRemote bankingBean = (BankingBeanRemote)  EJBManager.getEJBObject(EJBReferences.BANKING_EJB);
		Customer customer = new Customer();
		Branch branch = new Branch();
		//branch.setBranchCode((String)objCommonVO.getParamHm().get(CommonParams.BRANCH_CODE));
		//customer.setCif((String)objCommonVO.getParamHm().get(CommonParams.CIF));
		customer.setCustomerName((String)objCommonVO.getParamHm().get(CommonParams.CUSTOMER_NAME));
		//customer.setSwiftCode((String)objCommonVO.getParamHm().get(CommonParams.SWIFT_CODE));
		bankingBean.addCustomer(customer , branch);
		return objCommonVO;
	}

	private CommonVO addBranch(CommonVO objCommonVO) throws Exception {
		BankingBeanRemote bankingBean = (BankingBeanRemote)  EJBManager.getEJBObject(EJBReferences.BANKING_EJB);
		Branch branch = new Branch();
		//branch.setBranchCode((String)objCommonVO.getParamHm().get(CommonParams.BRANCH_CODE));
		branch.setBranchName((String)objCommonVO.getParamHm().get(CommonParams.BRANCH_NAME));
		branch.setBranchAddress((String)objCommonVO.getParamHm().get(CommonParams.BRANCH_ADDRESS));
		//branch.setSwiftCode((String)objCommonVO.getParamHm().get(CommonParams.SWIFT_CODE));
		Bank bank = new Bank();
		bank.setBankCode((String)objCommonVO.getParamHm().get(CommonParams.BANK_CODE));
		bankingBean.addBranch(branch,bank);
		return objCommonVO;
	}

	/**
	 * @since 1.6
	 * @param objCommonVO
	 * @return
	 */
	private CommonVO getAllAccounts (CommonVO objCommonVO)throws Exception{
		AccountRemote obj4 = (AccountRemote) EJBManager.getEJBObject(EJBReferences.ACCOUNT_EJB);
		Account account = new Account();
		//account.setAccountNo((String)(objCommonVO.getParamHm().get(CommonParams.ACCOUNT_NO)));
		String accountJson=  obj4.findAccount(account,DataType.JSON_STRING);
		HashMap<String, Object> objHm = new HashMap<String, Object>();
		JsonParser parser = new JsonParser();
		JsonObject o = (JsonObject)parser.parse(accountJson);
		objHm.put("ACCOUNT", o);
		
		objCommonVO.setCommonRespHm(objHm);
		objCommonVO.setResponsePage(JSPReference.ACCOUNT_VIEW);
		return objCommonVO ;
	}
	private CommonVO saveCustomer(CommonVO objCommonVO)throws Exception{
		CustomerBeanRemote customerBean = (CustomerBeanRemote) EJBManager.getEJBObject(EJBReferences.CUSTOMER_EJB);
		Customer customer = new Customer();
		customer.setCustomerName((String)(objCommonVO.getParamHm().get(CommonParams.CUSTOMER_NAME)));
		//customer.setCif((String)(objCommonVO.getParamHm().get(CommonParams.CIF)));
		customerBean.saveCustomer(customer);
		objCommonVO.setResponsePage(JSPReference.CUSTOMER_ADD);
		return objCommonVO;
	}
	private CommonVO getCustomer(CommonVO objCommonVO)throws Exception{
		CustomerBeanRemote customerBean = (CustomerBeanRemote) EJBManager.getEJBObject(EJBReferences.CUSTOMER_EJB);
		Customer customer = new Customer();
		//customer.setCustomerName((String)(objCommonVO.getParamHm().get(CommonParams.CUSTOMER_NAME)));
		//customer.setCif((String)(objCommonVO.getParamHm().get(CommonParams.CIF)));
		customer = customerBean.findCustomer(customer);
		objCommonVO.setResponsePage(JSPReference.CUSTOMER_ADD);
		return objCommonVO;
	}
	private CommonVO modifyBank(CommonVO objCommonVO) throws Exception {
		BankingBeanRemote bankingBean = (BankingBeanRemote)  EJBManager.getEJBObject(EJBReferences.BANKING_EJB);
		Country country = new Country();
		Bank bank = new Bank();
		country.setCountryCode((String)objCommonVO.getParamHm().get(CommonParams.COUNTRY_CODE));
		bank.setBankCode((String)objCommonVO.getParamHm().get(CommonParams.BANK_CODE));
		bank.setBankName((String)objCommonVO.getParamHm().get(CommonParams.BANK_NAME));
		bankingBean.modifyBank(country, bank);
		return objCommonVO;
	}
	private CommonVO findCountry(CommonVO objCommonVO) throws Exception {
		BankingBeanRemote baking = (BankingBeanRemote) EJBManager.getEJBObject(EJBReferences.BANKING_EJB);
		Country country = new Country();
		country.setCountryCode((String)(objCommonVO.getParamHm().get(CommonParams.COUNTRY_CODE)));
		String countryJson = baking.searchCountry(country.getCountryCode());
		objCommonVO.setAjaxResp(countryJson);
		return objCommonVO;
	}
	private CommonVO modifyCountry(CommonVO objCommonVO) throws Exception {
		BankingBeanRemote bankingBean = (BankingBeanRemote)  EJBManager.getEJBObject(EJBReferences.BANKING_EJB);
		Country country = new Country();
		country.setCountry((String)objCommonVO.getParamHm().get(CommonParams.COUNTRY ));
		country.setCountryCode((String)objCommonVO.getParamHm().get(CommonParams.COUNTRY_CODE));
		country.setCurrencyCode((String)objCommonVO.getParamHm().get(CommonParams.CURRENCY_CODE));
		bankingBean.modifyCountry(country);
		return objCommonVO;
	}
	private CommonVO searchCountry(CommonVO objCommonVO) throws Exception {
		BankingBeanRemote baking = (BankingBeanRemote) EJBManager.getEJBObject(EJBReferences.BANKING_EJB);
		Country country = new Country();
		country.setCountryCode((String)(objCommonVO.getParamHm().get(CommonParams.COUNTRY_CODE)));
		String countryListJson = baking.searchAllCountry(country.getCountryCode());
		//instead of a country search for all
		JsonParser parser = new JsonParser();
		JsonArray o = (JsonArray)parser.parse(countryListJson);
		HashMap<String, Object> objHm = new HashMap<String, Object>();
		objHm.put("COUNTRY_SEARCH_RESULT", o);
		objCommonVO.setCommonRespHm(objHm);
		objCommonVO.setResponsePage(JSPReference.COUNTRY_VIEW);
		return objCommonVO ;
	}
	private CommonVO searchBank(CommonVO objCommonVO) throws Exception {
		BankingBeanRemote baking = (BankingBeanRemote) EJBManager.getEJBObject(EJBReferences.BANKING_EJB);
		Bank bank = new Bank();
		bank.setBankCode((String)(objCommonVO.getParamHm().get(CommonParams.BANK_CODE)));
		String BankListJson = baking.searchAllBank(bank.getBankCode());
		//instead of a country search for all
		JsonParser parser = new JsonParser();
		JsonArray o = (JsonArray)parser.parse(BankListJson);
		HashMap<String, Object> objHm = new HashMap<String, Object>();
		objHm.put("SEARCH_RESULT", o);
		objCommonVO.setCommonRespHm(objHm);
		objCommonVO.setResponsePage(JSPReference.COMMON_VIEW);
		return objCommonVO ;
	}
	private CommonVO saveCountry(CommonVO objCommonVO) throws Exception {
		BankingBeanRemote bankingBean = (BankingBeanRemote)  EJBManager.getEJBObject(EJBReferences.BANKING_EJB);
		Country country = new Country();
		country.setCountry((String)objCommonVO.getParamHm().get(CommonParams.COUNTRY ));
		country.setCountryCode((String)objCommonVO.getParamHm().get(CommonParams.COUNTRY_CODE));
		country.setCurrencyCode((String)objCommonVO.getParamHm().get(CommonParams.CURRENCY_CODE));
		bankingBean.saveCountry(country);
		return objCommonVO;
	}
	private CommonVO addBank(CommonVO objCommonVO) throws Exception {
		BankingBeanRemote bankingBean = (BankingBeanRemote)  EJBManager.getEJBObject(EJBReferences.BANKING_EJB);
		Country country = new Country();
		Bank bank = new Bank();
		country.setCountryCode((String)objCommonVO.getParamHm().get(CommonParams.COUNTRY_CODE));
		bank.setBankCode((String)objCommonVO.getParamHm().get(CommonParams.BANK_CODE));
		bank.setBankName((String)objCommonVO.getParamHm().get(CommonParams.BANK_NAME));
		bankingBean.saveBank(country, bank);
		return objCommonVO;
	}
	private CommonVO saveAccount(CommonVO objCommonVO) throws Exception {
		AccountRemote accountBean = (AccountRemote) EJBManager.getEJBObject(EJBReferences.ACCOUNT_EJB);
		Account account = new Account();
		Customer customer = new Customer();
		//CustomerBeanRemote customerBean = (CustomerBeanRemote) EJBManager.getEJBObject(EJBReferences.CUSTOMER_EJB);
		//customer.setCif((String)(objCommonVO.getParamHm().get(CommonParams.CIF)));
		//customer = customerBean.findCustomer(customer);
		//account.setAccountNo((String)(objCommonVO.getParamHm().get(CommonParams.ACCOUNT_NO)));
		//account.setCustomer(customer);;
		account.setAccountType((String)(objCommonVO.getParamHm().get(CommonParams.ACCOUNT_TYPE)));
		accountBean.saveAccount(account,customer);
		HashMap<String, Object> objHm = new HashMap<String, Object>();
		
		objHm.put("ACCOUNT_ADD_STATUS","SUCCESS" );
		objCommonVO.setCommonRespHm(objHm);
		objCommonVO.setResponsePage(JSPReference.ACCOUNT_ADD);
		return objCommonVO ;
	}
	private CommonVO searchAccount(CommonVO objCommonVO) throws Exception {
		AccountRemote obj4 = (AccountRemote) EJBManager.getEJBObject(EJBReferences.ACCOUNT_EJB);
		Account account = new Account();
		//account.setAccountNo((String)(objCommonVO.getParamHm().get(CommonParams.ACCOUNT_NO)));
		//String accountsListJson = obj4.searchAccount(account.getAccountNo(),DataType.JSON_STRING);
		JsonParser parser = new JsonParser();
		//JsonArray o = (JsonArray)parser.parse(accountsListJson);
		HashMap<String, Object> objHm = new HashMap<String, Object>();
		//objHm.put("ACCOUNT_SEARCH_RESULT", o);
		objCommonVO.setCommonRespHm(objHm);
		objCommonVO.setResponsePage(JSPReference.ACCOUNT_VIEW);
		return objCommonVO ;
	}
}
