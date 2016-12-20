package cn.duanchao.homework.yoursoulismine.intefaces.retrofit;

import cn.duanchao.homework.yoursoulismine.bean.ThoughtWorksBean;
import retrofit2.http.GET;
import rx.Observable;

public interface ThoughtWorksService {
    //在HttpManager的createService方法中通过反射拿到
    String BASE_URL = "http://thoughtworks-ios.herokuapp.com/";

    @GET("facts.json")
    Observable<ThoughtWorksBean> getThoutWorksData();
}