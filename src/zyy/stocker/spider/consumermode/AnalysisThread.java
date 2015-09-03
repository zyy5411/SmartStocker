package zyy.stocker.spider.consumermode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import zyy.stocker.model.Stock;
import zyy.stocker.service.StockService;
import zyy.stocker.spider.meta.IDataSource;

/**
 * 
 * 进行数据解析及存储的线程
 * 
 * @author zyy
 *
 */

public class AnalysisThread extends Thread {
	BlockingQueue<IDataSource> queues;
	int size = 0;

	public AnalysisThread(BlockingQueue<IDataSource> queues) {
		this.queues = queues;
	}

	@Override
	public void run() {

		try {
			while (true) {
				IDataSource dataSource = queues.take();
				System.out.println("取出一个！：" + queues.size() + ",共处理："
						+ (++size));
				Iterator<Stock> iterator = dataSource.getIterator(null);
				Stock stock;
				while (iterator.hasNext()) {
					stock = iterator.next();
					StockService.getInstance().addStock(stock);
				}
				StockService.getInstance().commint();
				System.out.println("结束："
						+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(new Date()));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void start(IDataSource dataSource) {
	}
}
