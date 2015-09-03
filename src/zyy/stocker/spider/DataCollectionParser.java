package zyy.stocker.spider;

import java.util.LinkedList;
import java.util.List;

import zyy.config.log.MyLogger;
import zyy.stocker.spider.meta.DataCollectionConfiguration;
import zyy.stocker.spider.meta.DataSourceConfig;
import zyy.stocker.spider.meta.IDataSource;

/**
 * 根据配置对数据进行解析的类
 * 
 * @author zyy
 *
 */
public class DataCollectionParser {
	private DataCollectionConfiguration dataCollectionConfiguration;
	private List<IDataSource> dataSourceList = null;
	private boolean isParsed = false;

	public DataCollectionParser(
			DataCollectionConfiguration dataCollectionConfiguration) {
		this.dataCollectionConfiguration = dataCollectionConfiguration;
	}

	public void parse() {
		if (!isParsed) {
			List<DataSourceConfig> dataSourceConfigs = dataCollectionConfiguration
					.getDataSourceConfigs();
			for (DataSourceConfig config : dataSourceConfigs) {
				parseDataSourceConfig(config);
			}
			isParsed = true;
		}
	}

	private void parseDataSourceConfig(DataSourceConfig config) {
		try {
			IDataSource parserClass = getParserClass(config);
			List<IDataSource> datas = parserClass.getDataSources(config);
			dataSourceList = new LinkedList<IDataSource>(datas);
			// dataSourceList.addAll(datas);
			System.out.println(dataSourceList.size());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			MyLogger.getLogger(DataCollectionParser.class)
					.error(e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			MyLogger.getLogger(DataCollectionParser.class).error(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			MyLogger.getLogger(DataCollectionParser.class).error(e);
		}
	}

	public List<IDataSource> getDataSources() {
		parse();
		return dataSourceList;
	}

	private IDataSource getParserClass(DataSourceConfig config)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		String classPath = "";
		IDataSource dataSource = null;
		if ("SohuData".equals(config.getHandleClass())) {
			classPath = "zyy.stocker.spider.SohuStockDataSource";
			Class<?> c1 = Class.forName(classPath);
			dataSource = (IDataSource) c1.newInstance();
		} else {
			throw new ClassNotFoundException("没有找到 " + config.getHandleClass()
					+ "对应的类");
		}
		return dataSource;

	}
}
