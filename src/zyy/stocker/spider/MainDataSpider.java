package zyy.stocker.spider;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import zyy.config.BeanFactory;
import zyy.config.db.DBConnector;
import zyy.config.log.MyLogger;
import zyy.stocker.model.Stock;
import zyy.stocker.service.StockService;
import zyy.stocker.spider.meta.DataCollectionConfiguration;
import zyy.stocker.spider.meta.IDataSource;

/**
 * 
 * @author zyy 进行数据获取及存储的主控类
 */
public class MainDataSpider {

	public void run() {
		DataCollectionConfiguration spiderConfiguration = (DataCollectionConfiguration) BeanFactory
				.getBean("DataCollectionConfiguration");
		DataCollectionParser spiderParser = new DataCollectionParser(
				spiderConfiguration);
		spiderParser.parse();
		List<IDataSource> dataSources = spiderParser.getDataSources();
		System.out.println(dataSources.size());
		for (IDataSource dataSource : dataSources) {
			start(dataSource);
		}
	}

	public void start(IDataSource dataSource) {
		try {
			System.out.println("开始1："
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date()));
			System.out.println(dataSource.getURL());
			InputStream inputStream = getInputStream(dataSource.getURL());
			System.out.println("开始2："
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date()));
			Iterator<Stock> iterator = dataSource.getIterator(inputStream);
			Stock stock;
			while (iterator.hasNext()) {
				stock = iterator.next();
				System.out.println("add stock:" + stock.toString());
				MyLogger.getLogger(MainDataSpider.class).info(
						"add stock:" + stock.toString());
				StockService.getInstance().addStock(stock);
			}
			DBConnector.getSqlSession().close();
			System.out.println("开始3："
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private InputStream getInputStream(String URL) throws IOException,
			ClientProtocolException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(URL);
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		InputStream inputStream = response1.getEntity().getContent();
		return inputStream;
	}

	public static void main(String[] args) {
		// IDataSource stockSource = new SohuDataSource("000725", "20150707",
		// "20150720");
		new MainDataSpider().run();
	}
}
