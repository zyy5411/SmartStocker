package zyy.stocker.model;

public class Stock {
	private String code;
	private String date;
	private float openPrice;
	private float closePrice;
	private float maxPrice;
	private float leastPrice;
	private float turnOverRate;// 换手率
	private float turnVolumn;// 成交额
	private float volumn;// 成交量
	private float changeRate;// 涨跌幅

	public String getCode() {
		return code;
	}

	public Stock setCode(String code) {
		this.code = code;
		return this;
	}

	public String getDate() {
		return date;
	}

	public Stock setDate(String date) {
		this.date = date;
		return this;
	}

	public float getOpenPrice() {
		return openPrice;
	}

	public Stock setOpenPrice(float openPrice) {
		this.openPrice = openPrice;
		return this;
	}

	public float getClosePrice() {
		return closePrice;
	}

	public Stock setClosePrice(float closePrice) {
		this.closePrice = closePrice;
		return this;
	}

	public float getMaxPrice() {
		return maxPrice;
	}

	public Stock setMaxPrice(float maxPrice) {
		this.maxPrice = maxPrice;
		return this;
	}

	public float getLeastPrice() {
		return leastPrice;
	}

	public Stock setLeastPrice(float leastPrice) {
		this.leastPrice = leastPrice;
		return this;
	}

	public float getTurnOverRate() {
		return turnOverRate;
	}

	public Stock setTurnOverRate(float turnOverRate) {
		this.turnOverRate = turnOverRate;
		return this;
	}

	public float getTurnVolumn() {
		return turnVolumn;
	}

	public Stock setTurnVolumn(float turnVolumn) {
		this.turnVolumn = turnVolumn;
		return this;
	}

	public Float getVolumn() {
		return volumn;
	}

	public Stock setVolumn(Float volumn) {
		this.volumn = volumn;
		return this;
	}

	public float getChangeRate() {
		return changeRate;
	}

	public Stock setChangeRate(float changeRate) {
		this.changeRate = changeRate;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("股票代码：");
		sb.append(code);
		sb.append("，时间：");
		sb.append(date);
		sb.append("，开盘价：");
		sb.append(openPrice);
		sb.append("，收盘价：");
		sb.append(closePrice);
		sb.append(",最高价：");
		sb.append(maxPrice);
		sb.append(",最低价：");
		sb.append(leastPrice);
		sb.append(",换手率：");
		sb.append(turnOverRate);
		sb.append(",成交额：");
		sb.append(turnVolumn);
		sb.append(",成交量：");
		sb.append(volumn);
		sb.append(",涨跌幅：");
		sb.append(changeRate);
		sb.append("]");
		return sb.toString();
	}
}
