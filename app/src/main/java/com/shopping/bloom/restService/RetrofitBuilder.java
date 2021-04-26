package com.shopping.bloom.restService;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.shopping.bloom.utils.Const.*;

public class RetrofitBuilder {


    public Retrofit retrofit;

    private final String BASEURL = "http://bloomapp.in/api/";

    private static RetrofitBuilder mInstance;

    private RetrofitBuilder(Application application) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS).build();

        /*ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                //todo need to be added on server currently onl suporting tls version 1_2
               // .cipherSuites(CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA)
                .build();*/

        //   SSLContext sslContext = SslUtils.getSslContextForCertificateFile(context, "BPClass2RootCA-sha2.cer");
        //   client.setSslSocketFactory(sslContext.getSocketFactory());

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //  .sslSocketFactory(tlsSocketFactory, tlsSocketFactory.getTrustManager())
                //    .connectionSpecs(Arrays.asList(ConnectionSpec.COMPATIBLE_TLS))
                .addInterceptor(
                        chain -> {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder()
                                    .addHeader("Access-Control-Allow-Origin", "*")
                                    .addHeader("Access-Control-Allow-Methods", "GET,POST,PUT, OPTIONS")
                                    .method(original.method(), original.body());
                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                )
                .addInterceptor(interceptor)
                .addInterceptor(new ChuckInterceptor(application))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                       return true;

                    }
                })
                .build();


        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

    }

    public ApiInterface getApi() {
        return retrofit.create(ApiInterface.class);
    }


    public static synchronized RetrofitBuilder getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new RetrofitBuilder(application);
        }
        return mInstance;
    }




}
