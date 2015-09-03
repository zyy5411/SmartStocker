package zyy.stocker.service;

import org.apache.ibatis.session.SqlSession;

import zyy.config.db.DBConnector;
import zyy.config.log.MyLogger;
import zyy.stocker.dao.IStock;
import zyy.stocker.model.Stock;

public class StockService {

	static StockService stockService;
	IStock iStock;

	public static synchronized StockService getInstance() {
		if (stockService == null)
			stockService = new StockService();
		return stockService;
	}

	private StockService() {
		iStock = DBConnector.getSqlSession().getMapper(IStock.class);
	}

	public void addStock(Stock stock) {
		if (isStockExits(stock.getCode(), stock.getDate())) {
			MyLogger.getLogger(StockService.class).warn(
					"股票代码：" + stock.getCode() + ",日期：" + stock.getDate()
							+ ",记录已存在， 不插入数据库。");
		} else {
			MyLogger.getLogger(StockService.class).info(
					"添加股票：" + stock.toString());
			iStock.addAStock(stock);
			SqlSession session = DBConnector.getSqlSession();
			session.commit();
		}
	}

	public boolean isStockExits(String code, String date) {
		return null != iStock.isStockExist(code, date);
	}

	public Stock getStock(String code, String date) {
		return iStock.getStock(code, date);
	}

	public void commint() {
		DBConnector.getSqlSession().commit();
	}

	public static void main(String[] args) {
		StockService stockService = new StockService();
		System.out.println(stockService.getStock("000725", "2015-01-01"));
		stockService.addStock(stockService.getStock("000725", "2015-01-01")
				.setCode("013"));
		// MyLogger.getLogger(StockService.class).error("12312");
	}
}
