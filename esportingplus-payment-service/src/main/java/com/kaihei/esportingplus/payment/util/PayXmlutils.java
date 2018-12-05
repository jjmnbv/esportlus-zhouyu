package com.kaihei.esportingplus.payment.util;

import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhouyu
 * @Date: 2018/10/16 15:37
 * @Description:微信支付xml报文解析工具
 */
public class PayXmlutils {

    private static final Logger logger = LoggerFactory.getLogger(PayXmlutils.class);

    public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory
                .setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        documentBuilderFactory
                .setFeature("http://xml.org/sax/features/external-general-entities", false);
        documentBuilderFactory
                .setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        documentBuilderFactory
                .setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",
                        false);
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);

        return documentBuilderFactory.newDocumentBuilder();
    }

    public static org.w3c.dom.Document newDocument() throws ParserConfigurationException {
        return newDocumentBuilder().newDocument();
    }

    /**
     * 将map转化为xml报文
     */
    public static String mapToXml(Map<String, String> data) {
        String output = null;
        try {
            org.w3c.dom.Document document = newDocument();
            org.w3c.dom.Element root = document.createElement("xml");
            document.appendChild(root);
            for (String key : data.keySet()) {
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
            output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");

            writer.close();
        } catch (Exception ex) {
        }
        return output;
    }

    /**
     * 将xml报文转化为map
     */
    public static Map<String, String> xmlTomap(String xml) throws BusinessException {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilder documentBuilder = newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
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
                // todo nothing
            }
            return data;
        } catch (Exception ex) {
            logger.warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), xml);
            throw new BusinessException(BizExceptionEnum.EXTERNAL_TENPAY_XML_2_MAP_ERROR);
        }
    }

    /**
     * 根据回调的xml报文得到交易订单号
     */
    public static String getOutTradeNo(String xmlnotify) {
        try {
            Document doc = DocumentHelper.parseText(xmlnotify);
            Element root = doc.getRootElement();
            Element element = root.element("out_trade_no");
            if (element == null) {
                return null;
            }
            return element.getText();
        } catch (DocumentException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
