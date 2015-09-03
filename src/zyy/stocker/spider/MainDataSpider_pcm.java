package zyy.stocker.spider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import zyy.config.BeanFactory;
import zyy.stocker.spider.consumermode.AnalysisThread;
import zyy.stocker.spider.consumermode.DataObtainThread;
import zyy.stocker.spider.meta.DataCollectionConfiguration;
import zyy.stocker.spider.meta.IDataSource;

/**
 * 
 * 进行数据获 取及存储的主控类
 * 
 * @author zyy
 */
public class MainDataSpider_pcm {

	private static final int THREAD1_SIZE = 4;
	private BlockingQueue<IDataSource> queues;
	private BlockingQueue<IDataSource> dataSources;

	public void config() {
		DataCollectionConfiguration dataCollectionConfiguration = (DataCollectionConfiguration) BeanFactory
				.getBean("DataCollectionConfiguration");
		DataCollectionParser spiderParser = new DataCollectionParser(
				dataCollectionConfiguration);
		dataSources = new LinkedBlockingDeque<IDataSource>(
				spiderParser.getDataSources());
		queues = new LinkedBlockingDeque<IDataSource>(
				dataCollectionConfiguration.getMAX_LIST_SIZE());
	}

	public void runMultiThread() {
		config();
		System.out.println("开始1："
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date()));
		for (int i = 0; i < THREAD1_SIZE; i++) {
			new DataObtainThread(queues, dataSources).start();
		}
		new AnalysisThread(queues).start();
	}

	public static void main(String[] args) {
		new MainDataSpider_pcm().runMultiThread();
	}
}
