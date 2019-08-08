package com.tiangou.nettest.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IpService {

    @GET("getIpInfo.php?ip=59.108.54.37")
    Call<IpModel> getIpMsg();
}
