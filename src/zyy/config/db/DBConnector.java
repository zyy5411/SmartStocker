package zyy.config.db;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DBConnector {

	final static String MYBATIS_RESOURCE = "zyy/config/db/mybatis_config.xml";
	static SqlSessionFactory sessionFactory = null;
	static SqlSession sqlSession;

	public DBConnector() {
	}

	public static synchronized SqlSession getSqlSession() {
		if (sessionFactory == null) {
			InputStream is = DBConnector.class.getClassLoader()
					.getResourceAsStream(MYBATIS_RESOURCE);
			sessionFactory = new SqlSessionFactoryBuilder().build(is);
		}
		if (sqlSession == null)
			sqlSession = sessionFactory.openSession();
		return sqlSession;
	}

}
