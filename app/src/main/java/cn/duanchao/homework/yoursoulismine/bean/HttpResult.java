package cn.duanchao.homework.yoursoulismine.bean;

public class HttpResult<T> {
    //这里只有一个api地址，暂且认为title是否为空就是请求成功与否的标志
    public String title;
    public T rows;
}