package zyy.stocker.spider.consumermode;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import zyy.stocker.spider.meta.IDataSource;

/**
 * 进行数据获取的线程
 * 
 * @author zyy
 *
 */
public class DataObtainThread extends Thread {
	BlockingQueue<IDataSource> queues;
	BlockingQueue<IDataSource> dataSources;
	static int size = 0;

	public DataObtainThread(BlockingQueue<IDataSource> queues,
			BlockingQueue<IDataSource> dataSources) {
		this.queues = queues;
		this.dataSources = dataSources;
	}

	@Override
	public void run() {
		IDataSource dataSource;
		while (true) {
			try {
				dataSource = takeADataSource();
				if (null == dataSource) {
					break;
				}
				InputStream inputStream = getInputStream(dataSource.getURL());
				dataSource.setInputStream(inputStream);
				queues.put(dataSource);
				System.out.println("添加一个！：" + queues.size() + ",共天添加："
						+ (++size));
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private IDataSource takeADataSource() throws InterruptedException {
		IDataSource dataSource = null;
		synchronized (this) {
			if (!dataSources.isEmpty()) {
				dataSource = dataSources.take();
			}
		}
		return dataSource;
	}

	private InputStream getInputStream(String URL) throws IOException,
			ClientProtocolException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(URL);
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		InputStream inputStream = response1.getEntity().getContent();
		return inputStream;
	}

}
