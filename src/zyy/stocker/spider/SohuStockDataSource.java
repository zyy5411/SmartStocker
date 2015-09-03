package zyy.stocker.spider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;

import zyy.stocker.model.Stock;
import zyy.stocker.spider.meta.DataSourceConfig;
import zyy.stocker.spider.meta.IDataSource;

public class SohuStockDataSource implements IDataSource {

	// http://q.stock.sohu.com/hisHq?code=cn_000725&start=20150720&end=20150727&order=D
	private String code;
	private String begindate;
	private String enddate;
	private InputStream inputStream;

	public SohuStockDataSource(String code, String begindate, String enddate) {
		this.code = code;
		this.begindate = begindate;
		this.enddate = enddate;
	}

	public SohuStockDataSource() {
	}

	@Override
	public String getURL() {
		URIBuilder uriBuilder = null;
		try {
			uriBuilder = new URIBuilder("http://q.stock.sohu.com")
					.setPath("/hisHq").addParameter("code", "cn_" + code)
					.addParameter("order", "D");
			if (begindate != null) {
				uriBuilder.addParameter("start", begindate);
			}
			if (enddate != null) {
				uriBuilder.addParameter("end", enddate);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		System.out.println(uriBuilder.toString());
		return uriBuilder.toString();
	}

	@Override
	public Iterator<Stock> getIterator(InputStream inputStream) {
		inputStream = inputStream == null ? this.inputStream : inputStream;
		return new SohuStockIterator(inputStream);
	}

	class SohuStockIterator implements Iterator<Stock> {

		BufferedInputStream bufferedInputStream;
		StringBuilder sb = new StringBuilder();
		boolean hasNext = false;

		// InputStream inputStream;

		public SohuStockIterator(InputStream inputStream) {
			this.bufferedInputStream = new BufferedInputStream(inputStream);
			// this.inputStream = inputStream;
			try {
				read();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void read() throws IOException, Exception {
			int nextChar;
			nextChar = bufferedInputStream.read();
			if (nextChar == -1) {
				return;
			}
			if ('{' == (char) nextChar) {
				while (nextChar != -1) {
					sb.append((char) nextChar);
					nextChar = bufferedInputStream.read();
				}
				System.err.println(sb.toString());
			} else if ('[' == nextChar) {
				correctRead('{', bufferedInputStream.read());
				nextChar = bufferedInputStream.read();
				while (nextChar != -1) {
					sb.append((char) nextChar);
					if (sb.toString().endsWith("\"status\":")) {
						String status = getNext(bufferedInputStream, ',');
						if (!"0".equals(status)) {
							while (nextChar != -1) {
								sb.append((char) nextChar);
								nextChar = bufferedInputStream.read();
							}
							System.err.println("status:" + status);
							System.err.println(sb.toString());
						} else {
							sb.delete(0, sb.length());
						}
					}
					if (sb.toString().endsWith("\"hq\":")) {
						correctRead('[', bufferedInputStream.read());
						hasNext = true;
						break;
					}
					nextChar = bufferedInputStream.read();
				}

			}
			System.out.println("hasNext:" + hasNext);
		}

		private String getNext(BufferedInputStream bufferedInputStream,
				char spliter) throws IOException {
			int next = bufferedInputStream.read();
			StringBuilder sb = new StringBuilder();
			while (next != -1 && spliter != (char) next) {
				sb.append((char) next);
				next = bufferedInputStream.read();
			}
			return sb.toString();
		}

		private boolean correctRead(int desiredChar, int actChar)
				throws Exception {
			if (desiredChar != actChar) {
				throw new Exception("desire get char " + (char) desiredChar
						+ ",but act char is " + (char) actChar);
			}
			return true;
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public Stock next() {
			StringBuilder sb = null;
			Stock stock = null;
			try {
				int next = bufferedInputStream.read();
				if (hasNext && (char) next == '[') {
					sb = new StringBuilder();
					next = bufferedInputStream.read();
					while (next != -1 && next != ']') {
						sb.append((char) next);
						next = bufferedInputStream.read();
					}
					String[] values = sb.toString().replace("\"", "")
							.split(",");
					stock = new Stock()
							.setDate(values[0])
							.setCode(code)
							.setOpenPrice(Float.parseFloat(values[1]))
							.setClosePrice(Float.parseFloat(values[2]))
							.setChangeRate(
									Float.parseFloat(values[4].replace("%", "")))
							.setLeastPrice(Float.parseFloat(values[5]))
							.setMaxPrice(Float.parseFloat(values[6]))
							.setVolumn(Float.parseFloat(values[7]))
							.setTurnVolumn(Float.parseFloat(values[8]))
							.setTurnOverRate(
									Float.parseFloat(values[9].replace("%", "")));

					next = bufferedInputStream.read();
					if (',' != next) {
						hasNext = false;
					}
				} else {
					hasNext = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return stock;
		}
	}

	// @Override
	// public void config(String code, String begindate, String enddate) {
	// this.code = code;
	// this.begindate = begindate;
	// this.enddate = enddate;
	// }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public List<IDataSource> getDataSources(DataSourceConfig config) {
		String fromDate = config.getFromDate();
		String toDate = config.getToDate();
		int fromCode = Integer.parseInt(config.getFromCode());
		int toCode = Integer.parseInt(config.getToCode());
		int size = toCode - fromCode + 1;
		ArrayList<IDataSource> arrayList = null;
		if (size > 0) {
			arrayList = new ArrayList<IDataSource>(size);
			while (fromCode <= toCode) {
				arrayList.add(new SohuStockDataSource(String.format("%06d",
						fromCode), fromDate, toDate));
				fromCode++;
			}
		}

		return arrayList;
	}

	@Override
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
