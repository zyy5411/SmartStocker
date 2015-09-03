package zyy.stocker.model;

import java.io.InputStream;
import java.util.Iterator;

public interface StockSourceAPI {

	public String getURL();

	public Iterator<Stock> getIterator(InputStream inputStream);

}
