package com.example.administrator.analysisxml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/16 0016.
 */

public class PULLBookParser implements BookParser {
    @Override
    public List<Book> parse(InputStream in) throws Exception {

        List<Book> bookList = null;
        Book book = null;

        XmlPullParser parser = Xml.newPullParser(); //由android.util.Xml创建XmlPullParser实例
        parser.setInput(in, "UTF-8");  //设置输入流，并指明编码格式

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    bookList = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("book")) {
                        book = new Book();
                    } else if (parser.getName().equals("id")) {
                        eventType = parser.next();
                        book.setId(Integer.parseInt(parser.getText()));
                    } else if (parser.getName().equals("price")) {
                        eventType = parser.next();
                        book.setPrice(Float.parseFloat(parser.getText()));
                    } else if (parser.getName().equals("name")) {
                        eventType = parser.next();
                        book.setName(String.valueOf(parser.getText()));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("book")) {
                        bookList.add(book);
                        book = null;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return bookList;
    }

    @Override
    public String serialize(List<Book> books) throws Exception {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        serializer.setOutput(writer);//设置输出方向为writer
        serializer.startDocument("utf-8",true);
        serializer.startTag("","books");
        for (Book book : books){
            serializer.startTag("","book");
            serializer.attribute("","id",String.valueOf(book.getId())+"");

            serializer.startTag("","price");
            serializer.text(book.getPrice()+"");
            serializer.endTag("","price");

            serializer.startTag("","name");
            serializer.text(book.getName()+"");
            serializer.endTag("","name");

            serializer.endTag("","book");
        }
        serializer.endTag("","books");
        serializer.endDocument();
        return writer.toString();
    }
}
