package zyy.stocker.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import zyy.stocker.model.Stock;

public interface IStock {
	public Stock getStock(@Param("code") String code, @Param("date") String date);

	public String isStockExist(@Param("code") String code,
			@Param("date") String date);

	public List<Stock> getStockListByCode(@Param("code") String code);

	public List<Stock> getStockListByDate(@Param("code") String code,
			@Param("begindate") String begindate,
			@Param("enddate") String enddate);

	public void addAStock(Stock stock);
}
