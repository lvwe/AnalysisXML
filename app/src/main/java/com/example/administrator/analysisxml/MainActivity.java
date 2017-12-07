package com.example.administrator.analysisxml;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.thoughtworks.xstream.XStream;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button mBtnRead;
    private Button mBtnWrite;
    private BookParser mParser;
    private List<Book> mBookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnRead = (Button) findViewById(R.id.read_xml);
        mBtnWrite = (Button) findViewById(R.id.write_xml);

        mBtnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SAX解析
//               saxParser();
                //PULL解析
//                pullParser();
                //DOM解析
//                domParser();
                //xStream解析
                xStreamParser();


            }
        });

        mBtnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBookList.add(new Book(99.9f,1001,"C++"));
                mBookList.add(new Book(88.9f,1002,"Android"));
                mBookList.add(new Book(66.9f,1003,"PHP"));
                //SAX生成xml
                saxGenerateXML();
                //PULL生成xml
//                pullGenerateXML();
                //DOM生成xml
//                domGenerateXML();
            }
        });
    }

    private void xStreamParser() {
        try {
            InputStream is =  getAssets().open("book3.xml");
            XStream xStream = new XStream();
            xStream.processAnnotations(BookList.class);
            BookList book = (BookList) xStream.fromXML(is);
            for (int i = 0; i < book.getBookList().size(); i++) {
                Log.d(TAG, "xStreamParser: "+book.getBookList().get(i).toString());
            }


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void saxParser(){
        try {
            InputStream is = getAssets().open("books.xml");
            mParser = new SAXBookParser();//创建SAXBookParser实例
            mBookList = mParser.parse(is);//解析输入流
            for (Book book : mBookList) {
                Log.d(TAG, book.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void domParser() {
        try {
            InputStream is = getAssets().open("books.xml");
            mParser = new DOMBookParser();//创建SAXBookParser实例
            mBookList = mParser.parse(is);//解析输入流
            for (Book book : mBookList) {
                Log.d(TAG, book.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pullParser() {
        try {
            InputStream is = getAssets().open("books.xml");
            mParser = new PULLBookParser();//创建SAXBookParser实例
            mBookList = mParser.parse(is);//解析输入流
            for (Book book : mBookList) {
                Log.d(TAG, book.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saxGenerateXML(){
        try {
            BookParser parser = new SAXBookParser();
            String xml = parser.serialize(mBookList);//序列化
            FileOutputStream fos = openFileOutput("books.xml", Context.MODE_PRIVATE);
            fos.write(xml.getBytes("UTF-8"));
        }catch (Exception e){
            Log.d(TAG, "saxGenerateXML: "+e.getMessage());
        }
    }

    private void pullGenerateXML(){
        try {
            BookParser parser = new PULLBookParser();
            String xml = parser.serialize(mBookList);//序列化
            FileOutputStream fos = openFileOutput("books.xml", Context.MODE_PRIVATE);
            fos.write(xml.getBytes("UTF-8"));
        }catch (Exception e){
            Log.d(TAG, "pullGenerateXML: "+e.getMessage());
        }
    }

    private void domGenerateXML(){
        try {
            BookParser parser = new DOMBookParser();
            String xml = parser.serialize(mBookList);//序列化
            FileOutputStream fos = openFileOutput("books.xml", Context.MODE_PRIVATE);
            fos.write(xml.getBytes("UTF-8"));
        }catch (Exception e){
            Log.d(TAG, "domGenerateXML: "+e.getMessage());
        }
    }


}
