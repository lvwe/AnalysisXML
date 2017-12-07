package com.example.administrator.analysisxml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7 0007.
 */

@XStreamAlias("bookList")
public class BookList {

    @XStreamImplicit(itemFieldName = "book")
    private List<Book> mBookList;

    public List<Book> getBookList() {
        return mBookList;
    }

    public void setBookList(List<Book> bookList) {
        mBookList = bookList;
    }


}
