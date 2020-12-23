package com.ysq.theTourGuide.utils;


import com.ysq.theTourGuide.config.WXPayConstants;
import com.ysq.theTourGuide.config.WXPayConstants.SignType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * <p>User: qrn
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 * 描述: 工具类
 */
public class WXPayUtil {



    /**
     *  XML格式字符串转换为Map
     * @param strXML XML字符串
     * @return  Map XML数据转换后的Map
     * @see Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            WXPayUtil.getLogger().warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
            throw ex;
        }

    }
    /**
     * 将Map转换为XML格式的字符串
     * @param data Map类型数据
     * @return   XML格式的字符串
     * @see Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        return output;
    }


    /**
     * 生成6位或10位随机数 param codeLength(多少位)
     * @param codeLength 参数
     * @return  String

     */
    public static String createCode(int codeLength) {
        String code = "";
        for (int i = 0; i < codeLength; i++) {
            code += (int) (Math.random() * 9);
        }
        return code;
    }
    /**
     * 生成带有 sign 的 XML 格式字符串
     * @param data Map类型数据
     * @param key API密钥
     * @see Exception
     * @return String 含有sign字段的XML
     */
    public static String generateSignedXml(final Map<String, String> data, String key) throws Exception {
        return generateSignedXml(data, key,SignType.MD5);
    }

    /**
     * 生成带有 sign 的 XML 格式字符串
     *
     * @param data Map类型数据
     * @param key API密钥
     * @param signType 签名类型
     * @see Exception
     * @return 含有sign字段的XML
     */
    public static String generateSignedXml(final Map<String, String> data, String key, SignType signType) throws Exception {
        String sign = generateSignature(data, key, signType);
        data.put(WXPayConstants.FIELD_SIGN, sign);
        return mapToXml(data);
    }

    /**
     * 判断签名是否正确
     * @param xmlStr XML格式数据
     * @param key  API密钥
     * @return  boolean 签名是否正确
     * @see Exception
     */
    public static boolean isSignatureValid(String xmlStr, String key) throws Exception {
        Map<String, String> data = xmlToMap(xmlStr);
        if (!data.containsKey(WXPayConstants.FIELD_SIGN) ) {
            return false;
        }
        String sign = data.get(WXPayConstants.FIELD_SIGN);
        return generateSignature(data, key).equals(sign);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。使用MD5签名。
     * @param data Map类型数据
     * @param key API密钥
     * @return  boolean 签名是否正确
     * @see Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key) throws Exception {
        return isSignatureValid(data, key, SignType.MD5);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data Map类型数据
     * @param key API密钥
     * @param signType 签名方式
     * @return boolean 签名是否正确
     * @exception Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key, SignType signType) throws Exception {
        if (!data.containsKey(WXPayConstants.FIELD_SIGN) ) {
            return false;
        }
        String sign = data.get(WXPayConstants.FIELD_SIGN);
        return generateSignature(data, key, signType).equals(sign);
    }

    /**
     * 生成签名
     * @param data  待签名数据
     * @param key  API密钥
     * @return String
     * @see Exception
     */
    public static String generateSignature(final Map<String, String> data, String key) throws Exception {
        return generateSignature(data, key, SignType.MD5);
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key API密钥
     * @param signType 签名方式
     * @return  String 签名
     * @see Exception
     */
    public static String generateSignature(final Map<String, String> data, String key, SignType signType) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(WXPayConstants.FIELD_SIGN)) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        if (SignType.MD5.equals(signType)) {
            return MD5(sb.toString()).toUpperCase();
        }
        else if (SignType.HMACSHA256.equals(signType)) {
            return HMACSHA256(sb.toString(), key);
        }
        else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }
    }


    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }


    /**
     *
     *
     * @param data 待处理数据
     * @return String MD5结果
     * @see Exception
     */
    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 生成 HMACSHA256
     * @param data 待处理数据
     * @param key 密钥
     * @return String 加密结果
     * @see  Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 日志
     * @return Logger
     */
    public static Logger getLogger() {
        Logger logger = LoggerFactory.getLogger("wxpay java sdk");
        return logger;
    }

    /**
     * 获取当前时间戳，单位秒
     * @return long
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis()/1000;
    }

    /**
     * 当前时间的下一个月:
     * @return String
     */
    public static String getcuentime() {
        Calendar cal = Calendar.getInstance();
        cal.add(cal.MONTH, 1);
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String preMonth = dft.format(cal.getTime());
        System.out.println(preMonth);

        return preMonth;
    }

    /**
     * 给时间在加上一个月:
     * @param cur 时间参数
     * @return Date
     */
    public Date getNewDate(Date cur) {
        Calendar c = Calendar.getInstance();
        c.setTime(cur);   //设置时间
        c.add(Calendar.MINUTE, 1); //日期分钟加1,Calendar.DATE(天),Calendar.HOUR(小时)
        Date date = c.getTime(); //结果
        return date;
    }



    /**
     * 获取当前时间戳，单位毫秒
     * @return long
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }

    /**
     * 生成 uuid， 即用来标识一笔单，也用做 nonce_str
     * @return String
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }







    /**
     * map 转换为
     * @param map
     * @return
     */
    public  static String sunx(Map<String, String> map) {
        String  xmlResult="";
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (String key : map.keySet()) {
            sb.append("<" + key + ">" + map.get(key) + "</" + key + ">");
            System.out.println();
        }
        sb.append("</xml>");
        xmlResult = sb.toString();
        return xmlResult;
    }


    public static String getopenid(String openid,String out_trade_no,int total_fee,String app_id,String app_secret,String mch_id,String notifyUrl) {



        //下单的金额，因为在微信支付中默认是分所以要这样处理
        Integer total_fees=total_fee*100;
        //微信下单的金额是String类型的所以要转换类型
        String money=total_fees.toString();
        String nonceStr=WXPayUtil.generateUUID(); //设置UUID作为随机字符串

        Map<String ,String> map = new HashMap<String ,String>();
        map.put("appid",app_id); //商户appid
        map.put("mch_id",mch_id);//商户号
        map.put("nonce_str",nonceStr); //随机数
        map.put("body","大米");//商户名称
        map.put("out_trade_no",out_trade_no);//商户订单号
        map.put("total_fee",money);//下单金额
        map.put("spbill_create_ip", "127.0.0.1");//终端IP
        map.put("notify_url",notifyUrl);//回调地址 这里的接口必须是在线上用户支付成功才能收到微信发送的信息
        map.put("trade_type","JSAPI");//交易类型
        map.put("openid",openid+"");//用户openid
        map.put("sign_type","MD5");//加密类型
        String sign="";
        try {
            sign= WXPayUtil.generateSignature(map, app_secret); //生成sign签名WeChatTool.sercet_key是商户的支付秘钥
        } catch (Exception e) {
            e.printStackTrace();
        }
        //拼接成xml的格式，这里的参数必须要和上面的一致，并且每次下单的订单号不能一致
        String formData="<xml>";

        formData += "<appid>"+ app_id +"</appid>";
        formData += "<mch_id>"+ mch_id+"</mch_id>";
        formData += "<nonce_str>"+nonceStr+"</nonce_str>";
        formData += "<body>"+new Date().getMonth()+"</body>";
        formData += "<out_trade_no>"+out_trade_no +"</out_trade_no>";
        formData += "<total_fee>"+money+"</total_fee>";
        formData += "<spbill_create_ip>"+"127.0.0.1"+"</spbill_create_ip>";
        formData += "<notify_url>"+notifyUrl+"</notify_url>";
        formData += "<trade_type>"+"authorization_code"+"</trade_type>";
        formData += "<openid>"+openid+"</openid>"; //appid
        formData += "<sign_type>"+"MD5"+"</sign_type>";
        formData += "<sign>"+sign+"</sign>"; //签名算法
        formData += "</xml>";
        return formData;
    }


}
