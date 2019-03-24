package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the COUNTRY database table.
 * 
 */
@Entity
@Table(name="COUNTRY")
@NamedQuery(name="Country.findAll", query="SELECT c FROM Country c")
public class Country implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COUNTRY_CODE", unique=true, nullable=false, length=2)
	private String countryCode;

	@Column(length=100)
	private String country;

	@Column(name="CURRENCY_CODE", length=3)
	private String currencyCode;

	//bi-directional many-to-one association to Bank
	@OneToMany(mappedBy="country")
	private List<Bank> banks;

	public Country() {
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public List<Bank> getBanks() {
		return this.banks;
	}

	public void setBanks(List<Bank> banks) {
		this.banks = banks;
	}

	public Bank addBank(Bank bank) {
		getBanks().add(bank);
		bank.setCountry(this);

		return bank;
	}

	public Bank removeBank(Bank bank) {
		getBanks().remove(bank);
		bank.setCountry(null);

		return bank;
	}

}