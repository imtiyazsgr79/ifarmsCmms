package in.ifarms.com;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import in.ifarms.com.General.MainActivityLogin;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Context context = MainActivityLogin.getAppContext();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
//                .addInterceptor(new ChuckerInterceptor(context))
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.2.10:8080/engrproject/")
                .baseUrl("http://103.24.4.168:8180/cmms/")
//                .baseUrl("http://192.168.2.13:8087/engrproject/")
                //.baseUrl("http://192.168.1.104:8080/engrproject/")
                //.baseUrl("http://192.168.21.117:8080/engrproject/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create()))
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    public static UserService getUserServices() {
        UserService userService = getRetrofit().create(UserService.class);
        return userService;
    }

}