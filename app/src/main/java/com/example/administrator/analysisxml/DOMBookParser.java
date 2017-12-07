package com.example.administrator.analysisxml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Administrator on 2017/11/16 0016.
 */

public class DOMBookParser implements BookParser {
    @Override
    public List<Book> parse(InputStream in) throws Exception {
        List<Book> bookList = new ArrayList<>();
        //取得DocumentBuilderFactory实例
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //从factory获取DocumentBuilder实例
        DocumentBuilder builder = factory.newDocumentBuilder();
        //解析输入流 得到Document实例
        Document doc = builder.parse(in);
        Element rootElement  = doc.getDocumentElement();
        NodeList items = rootElement.getElementsByTagName("book");
        for (int i = 0; i < items.getLength(); i++) {
            Book book = new Book();
            Node item = items.item(i);
            NodeList properties = item.getChildNodes();
            for (int j = 0; j < properties.getLength(); j++) {
                Node  property = properties.item(j);
                String nodeName = property.getNodeName();
                if (nodeName.equals("id")){
                    book.setId(Integer.parseInt(property.getFirstChild().getNodeValue()));
                }else if (nodeName.equals("name")){
                    book.setName(String.valueOf(property.getFirstChild().getNodeValue()));
                }else if (nodeName.equals("price")){
                    book.setPrice(Float.parseFloat(property.getFirstChild().getNodeValue()));
                }
            }
            bookList.add(book);
        }
        return bookList;
    }

    @Override
    public String serialize(List<Book> books) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();//由Builder创建新文档
        Element rootElement = doc.createElement("books");
        for (Book book : books){
            Element bookElement = doc.createElement("book");
            bookElement.setAttribute("id", String.valueOf(book.getId()));

            Element nameElement = doc.createElement("name");
            nameElement.setTextContent(String.valueOf(book.getName()));
            bookElement.appendChild(nameElement);

            Element priceElement = doc.createElement("price");
            priceElement.setTextContent(String.valueOf(book.getPrice()));
            bookElement.appendChild(priceElement);

            rootElement.appendChild(bookElement);
        }
        doc.appendChild(rootElement);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8"); //设置输出采用编码方式
        transformer.setOutputProperty(OutputKeys.INDENT,"yes"); //是否自动添加额外的空白
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"no"); //是否忽略XML声明

        StringWriter writer = new StringWriter();
        Source source = new DOMSource();//表明文档来源是DOC
        Result result = new StreamResult(writer);//表明目标结果为writer
        transformer.transform(source,result);//开始转换

        return writer.toString();
    }
}
