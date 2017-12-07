package com.example.administrator.analysisxml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Administrator on 2017/11/16 0016.
 */

public class SAXBookParser implements BookParser {


    @Override
    public List<Book> parse(InputStream in) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance(); //获取SAXParserFactory实例
        SAXParser parser = factory.newSAXParser();                 //从SAXParserFactory获取SAXParser实例
        MyHandle  handle = new MyHandle();
        parser.parse(in,handle);                                    //根据Handler规则解析输入流
        return handle.getBooks();
    }

    @Override
    public String serialize(List<Book> books) throws Exception {
        //取得SAXTransformFactory实例
        SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();
        TransformerHandler handler = factory.newTransformerHandler();//从factory获取TransformHandler实例
        Transformer transformer = handler.getTransformer(); //从handler获取Transform实例
        transformer.setOutputProperty(OutputKeys.ENCODING,"utf-8");//设置输出编码格式
        transformer.setOutputProperty(OutputKeys.INDENT,"yes");//是否自动添加额外的空白
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"no");//是否忽略XML声明

        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);
        handler.setResult(result);

        String uri = ""; //代表命名空间URI，当URI无值时，须置为空字符串
        String localName = ""; //命名空间的本地名称（不包含前缀）当没有进行命名空间处理时，须置为空字符串

        handler.startDocument();
        handler.startElement(uri,localName,"books",null);

        AttributesImpl attrs = new AttributesImpl();  //负责存放元素的属性信息
        char[] chars = null;
        for (Book book : books){
            attrs.clear(); //清空属性列表
            attrs.addAttribute(uri,localName,"id","string",String.valueOf(book.getId()));//添加一个名为id的属性
            handler.startElement(uri,localName,"book",attrs); //开始一个book元素，关联上面设定的id属性

            handler.startElement(uri,localName,"name",null); //开始一个name元素，没有属性
            chars  = String.valueOf(book.getName()).toCharArray();
            handler.characters(chars,0,chars.length); // 设置name元素的文本节点
            handler.endElement(uri,localName,"name");

            handler.startElement(uri,localName,"price",null);
            chars = String.valueOf(book.getPrice()).toCharArray();
            handler.characters(chars,0,chars.length);
            handler.endElement(uri,localName,"price");

            handler.endElement(uri,localName,"book");
        }
        handler.endElement(uri,localName,"books");
        handler.endDocument();
        return writer.toString();
    }

    /**
     * 重写DefaultHandle中的方法  定义自己的事件处理逻辑
     * --1.DefaultHandle主要是事件处理器，可以接收解析器报告的所有事件,处理所发现的事件
     * --2.它实现了EntityResolver，DTDHandler，ErrorHandler，ContentHandler接口，
     * ---其中主要的是ContentHandler接口，其中主要方法是
     * ----startDocument()--当执行文档遇到起始节点的时候调用，可有获取起始节点相关信息
     * ----characters（char[],int,int）--然后此方法被调用，可有获取节点内的文本信息
     * ----startElement(String,String,String,Attributes)
     * ----endElement(String,String,String)
     * ----endDocument()--
     *
     * ---但是在方法体内并没有进行任何操作，因此要复写其方法
     */
    private class MyHandle extends DefaultHandler {

        private List<Book> mBookList;
        private Book mBook;
        private StringBuilder mBuilder;

        //返回解析后得到的Book对象集合
        public List<Book> getBooks() {
            return mBookList;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            mBookList = new ArrayList<>();
            mBuilder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (localName.equals("book")){
                mBook = new Book();
            }
            mBuilder.setLength(0); //将字符串设置为0，以便从新开始读取元素内的字符节点
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            mBuilder.append(ch,start,length); //将读取到的字符数组追加到build中
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (localName.equals("id")){
                mBook.setId(Integer.parseInt(mBuilder.toString()));
            }else if (localName.equals("name")){
                mBook.setName(mBuilder.toString());
            }else if (localName.equals("price")){
                mBook.setPrice(Float.parseFloat(mBuilder.toString()));
            }else if (localName.equals("book")){
                mBookList.add(mBook);
            }
        }
    }
}
