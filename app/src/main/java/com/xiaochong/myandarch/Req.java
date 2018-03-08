package com.xiaochong.myandarch;

import java.util.List;

/**
 * 作者：daizhiqing on 2018/3/8 00:32
 * 邮箱：daizhiqing8@163.com
 * 描述：
 */

public class Req {


    /**
     * code : 000000
     * msg : 请求成功
     * data : {"result":[{"coinName":"BTC","closingPrice":10678.98,"nowPrice":10299.99,"cnyPrice":"￥65198.93","flag":-1,"range":"3.54%","date":"2018-03-06","volume":"59.41万","sevenRange":null,"marketValue":null,"ranking":0,"currency":"￥"},{"coinName":"BCH","closingPrice":1173.59,"nowPrice":1131,"cnyPrice":"￥7159.23","flag":-1,"range":"3.62%","date":"2018-03-06","volume":"31.79万","sevenRange":null,"marketValue":null,"ranking":0,"currency":"￥"},{"coinName":"ETH","closingPrice":785.84,"nowPrice":761,"cnyPrice":"￥4817.13","flag":-1,"range":"3.16%","date":"2018-03-06","volume":"227.58万","sevenRange":null,"marketValue":null,"ranking":0,"currency":"￥"},{"coinName":"DASH","closingPrice":558.3,"nowPrice":538,"cnyPrice":"￥3405.54","flag":-1,"range":"3.63%","date":"2018-03-06","volume":"19.34万","sevenRange":null,"marketValue":null,"ranking":0,"currency":"￥"},{"coinName":"LTC","closingPrice":193.04,"nowPrice":186.85,"cnyPrice":"￥1182.76","flag":-1,"range":"3.20%","date":"2018-03-06","volume":"314.04万","sevenRange":null,"marketValue":null,"ranking":0,"currency":"￥"},{"coinName":"XRP","closingPrice":0.8925,"nowPrice":0.8678,"cnyPrice":"￥5.49","flag":-1,"range":"2.76%","date":"2018-03-06","volume":"92124.22万","sevenRange":null,"marketValue":null,"ranking":0,"currency":"￥"}]}
     */

    private String code;
    private String msg;
    private DataEntity data;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * result : [{"coinName":"BTC","closingPrice":10678.98,"nowPrice":10299.99,"cnyPrice":"￥65198.93","flag":-1,"range":"3.54%","date":"2018-03-06","volume":"59.41万","sevenRange":null,"marketValue":null,"ranking":0,"currency":"￥"},{"coinName":"BCH","closingPrice":1173.59,"nowPrice":1131,"cnyPrice":"￥7159.23","flag":-1,"range":"3.62%","date":"2018-03-06","volume":"31.79万","sevenRange":null,"marketValue":null,"ranking":0,"currency":"￥"},{"coinName":"ETH","closingPrice":785.84,"nowPrice":761,"cnyPrice":"￥4817.13","flag":-1,"range":"3.16%","date":"2018-03-06","volume":"227.58万","sevenRange":null,"marketValue":null,"ranking":0,"currency":"￥"},{"coinName":"DASH","closingPrice":558.3,"nowPrice":538,"cnyPrice":"￥3405.54","flag":-1,"range":"3.63%","date":"2018-03-06","volume":"19.34万","sevenRange":null,"marketValue":null,"ranking":0,"currency":"￥"},{"coinName":"LTC","closingPrice":193.04,"nowPrice":186.85,"cnyPrice":"￥1182.76","flag":-1,"range":"3.20%","date":"2018-03-06","volume":"314.04万","sevenRange":null,"marketValue":null,"ranking":0,"currency":"￥"},{"coinName":"XRP","closingPrice":0.8925,"nowPrice":0.8678,"cnyPrice":"￥5.49","flag":-1,"range":"2.76%","date":"2018-03-06","volume":"92124.22万","sevenRange":null,"marketValue":null,"ranking":0,"currency":"￥"}]
         */

        private List<ResultEntity> result;

        public void setResult(List<ResultEntity> result) {
            this.result = result;
        }

        public List<ResultEntity> getResult() {
            return result;
        }

        public static class ResultEntity {
            /**
             * coinName : BTC
             * closingPrice : 10678.98
             * nowPrice : 10299.99
             * cnyPrice : ￥65198.93
             * flag : -1
             * range : 3.54%
             * date : 2018-03-06
             * volume : 59.41万
             * sevenRange : null
             * marketValue : null
             * ranking : 0
             * currency : ￥
             */

            private String coinName;
            private double closingPrice;
            private double nowPrice;
            private String cnyPrice;
            private int flag;
            private String range;
            private String date;
            private String volume;
            private Object sevenRange;
            private Object marketValue;
            private int ranking;
            private String currency;

            public void setCoinName(String coinName) {
                this.coinName = coinName;
            }

            public void setClosingPrice(double closingPrice) {
                this.closingPrice = closingPrice;
            }

            public void setNowPrice(double nowPrice) {
                this.nowPrice = nowPrice;
            }

            public void setCnyPrice(String cnyPrice) {
                this.cnyPrice = cnyPrice;
            }

            public void setFlag(int flag) {
                this.flag = flag;
            }

            public void setRange(String range) {
                this.range = range;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public void setVolume(String volume) {
                this.volume = volume;
            }

            public void setSevenRange(Object sevenRange) {
                this.sevenRange = sevenRange;
            }

            public void setMarketValue(Object marketValue) {
                this.marketValue = marketValue;
            }

            public void setRanking(int ranking) {
                this.ranking = ranking;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getCoinName() {
                return coinName;
            }

            public double getClosingPrice() {
                return closingPrice;
            }

            public double getNowPrice() {
                return nowPrice;
            }

            public String getCnyPrice() {
                return cnyPrice;
            }

            public int getFlag() {
                return flag;
            }

            public String getRange() {
                return range;
            }

            public String getDate() {
                return date;
            }

            public String getVolume() {
                return volume;
            }

            public Object getSevenRange() {
                return sevenRange;
            }

            public Object getMarketValue() {
                return marketValue;
            }

            public int getRanking() {
                return ranking;
            }

            public String getCurrency() {
                return currency;
            }
        }
    }

    @Override
    public String toString() {
        return "Req{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
