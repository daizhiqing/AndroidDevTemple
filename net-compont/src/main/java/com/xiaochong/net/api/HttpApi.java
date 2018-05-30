package com.xiaochong.net.api;

import com.xiaochong.net.HttpResponse;
import com.xiaochong.net.entity.Req;

import java.util.Map;

import io.reactivex.Observable;

import retrofit2.http.POST;
import retrofit2.http.QueryMap;


/**
 * Created by daizhiqing on 2018/3/27.
 *
 * 存放所有的Api
 */

public interface HttpApi {


    @POST("account")
    Observable<HttpResponse<Req>> getDoubanDatanew(@QueryMap Map<String, Object> map);

}
