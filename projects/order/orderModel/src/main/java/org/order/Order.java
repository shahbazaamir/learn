package org.order;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * persitance class for ORDER_BOOK
 */
@Entity
@Table(name="ORDER_BOOK")
public class Order implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private String id;
	
	@Column(name="SCRIPT_NAME")
	private String ScriptName;
	
	@Column(name="STOCK_PRICE")
	private BigDecimal stockPrice;
	
	@Column(name="STOCKS")
	private BigDecimal stocks;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScriptName() {
		return ScriptName;
	}

	public void setScriptName(String scriptName) {
		ScriptName = scriptName;
	}

	public BigDecimal getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(BigDecimal stockPrice) {
		this.stockPrice = stockPrice;
	}

	public BigDecimal getStocks() {
		return stocks;
	}

	public void setStocks(BigDecimal stocks) {
		this.stocks = stocks;
	}
	
	
	
}
