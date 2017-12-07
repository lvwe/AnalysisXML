package com.example.administrator.analysisxml;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Administrator on 2017/11/16 0016.
 */

public interface BookParser {

    /**
     * 解析输入流，得到Book对象的集合
     * @param in
     * @return
     * @throws Exception
     */
    public List<Book> parse(InputStream in)throws Exception;

    /**
     * 序列化Book对象集合，得到XML形式的字符串
     * @param books
     * @return
     * @throws Exception
     */
    public String serialize(List<Book> books) throws Exception;
}
