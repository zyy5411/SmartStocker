package zyy.stocker.spider.meta;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import zyy.stocker.model.Stock;

/**
 * 数据源的接口，用于获取数据源及解析数据
 * 
 * @author zyy
 *
 */
public interface IDataSource {
	/**
	 * 获取根据数据源的配置解析出对应的URL
	 * 
	 * @return URL
	 */
	public String getURL();

	/**
	 * 获取该数据源解析后的数据实体
	 * 
	 * @param inputStream
	 *            需要进行解析的流
	 * @return 解析后的数据实体的迭代器
	 */
	public Iterator<Stock> getIterator(InputStream inputStream);

	// public void config(String code, String begindate, String enddate);

	/**
	 * 解析数据源配置，获取数据源列表
	 * 
	 * @param dataSourceConfig
	 *            单个的数据源配置
	 * @return 数据源列表
	 */
	public List<IDataSource> getDataSources(DataSourceConfig dataSourceConfig);

	public void setInputStream(InputStream inputStream);
}
