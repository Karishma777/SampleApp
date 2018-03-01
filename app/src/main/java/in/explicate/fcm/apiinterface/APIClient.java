package in.explicate.fcm.apiinterface;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIClient {

    private static Retrofit retrofit = null;

    public  static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      //  OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzYTIxMjAyOC03NzcxLTQyMjgtYmQ1NC00NTBmZTk2NzU1NzQiLCJpYXQiOjE1MTMxNjgxNzEsInN1YiI6IkFwcCBBY2Nlc3MgVG9rZW4iLCJpc3MiOiJ3d3cubWFnYXJwYXR0YWNpdHkuY29tIn0.S4-hEtxq2GZbgcJlWw6mOU6ORpmYKEhn7mnKkbcm1TU")
                        .addHeader("Content-Type","application/x-www-form-urlencoded")
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();


        retrofit = new Retrofit.Builder()
                .baseUrl("http://202.189.234.106:8080/tmswsapp/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

}
