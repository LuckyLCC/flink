package com.liuchang.assetsdemo;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import sun.misc.BASE64Encoder;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AssetDemo {

    private static SSLSocketFactory sslSocketFactory1;
    private static X509TrustManager x509TrustManager;



    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("assets/m123.233.250.83.cer");
        getSslSocketFactoryAndTrustManagerFactory(fileInputStream);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory1,x509TrustManager)
                .readTimeout(7676, TimeUnit.MILLISECONDS)
                .connectTimeout(7676, TimeUnit.MILLISECONDS)
                .hostnameVerifier(new OkHttpHostnameVerifier())
                .build();

        Request req;
        Request.Builder builder = new Request.Builder();
        // 请求数据封装
//        String jsonStr ="{\n" +
//                "\t\"IPCType\": \"orderCreate\",\n" +
//                "\t\"CompanyId\": \"huwochuxing\",\n" +
//                "\t\"orderCreate\": [{\n" +
//                "\t\t\"OrderTime\": 20220712153439,\n" +
//                "\t\t\"Destination\": \"沙坪坝(地铁站)\",\n" +
//                "\t\t\"CompanyId\": \"huwochuxing\",\n" +
//                "\t\t\"Address\": 370100,\n" +
//                "\t\t\"DestLongitude\": 106459644,\n" +
//                "\t\t\"FareType\": \"6269686671933024\",\n" +
//                "\t\t\"OrderId\": \"2207124471520086611\",\n" +
//                "\t\t\"DepLongitude\": 106469925,\n" +
//                "\t\t\"Encrypt\": 1,\n" +
//                "\t\t\"DepLatitude\": 29559939,\n" +
//                "\t\t\"DepartTime\": 20220712153439,\n" +
//                "\t\t\"Departure\": \"重庆大学A区(南1门)\",\n" +
//                "\t\t\"DestLatitude\": 29557360\n" +
//                "\t}],\n" +
//                "\t\"Source\": \"huwochuxing\"\n" +
//                "}";


        String jsonStr="{\n" +
                "\t\"IPCType\": \"orderCreate\",\n" +
                "\t\"CompanyId\": \"huwochuxing\",\n" +
                "\t\"orderCreate\": [{\n" +
                "\t\t\"OrderTime\": 20220712153439,\n" +
                "\t\t\"Destination\": \"沙坪坝(地铁站)\",\n" +
                "\t\t\"CompanyId\": \"huwochuxing\",\n" +
                "\t\t\"Address\": 370100,\n" +
                "\t\t\"DestLongitude\": 106459644,\n" +
                "\t\t\"FareType\": \"6269686671933024\",\n" +
                "\t\t\"OrderId\": \"2207124471520086611\",\n" +
                "\t\t\"DepLongitude\": 106469925,\n" +
                "\t\t\"Encrypt\": 1,\n" +
                "\t\t\"DepLatitude\": 29559939,\n" +
                "\t\t\"DepartTime\": 20220712153439,\n" +
                "\t\t\"Departure\": \"重庆大学A区(南1门)\",\n" +
                "\t\t\"DestLatitude\": 29557360\n" +
                "\t}, {\n" +
                "\t\t\"OrderTime\": 20220712153439,\n" +
                "\t\t\"Destination\": \"沙坪坝(地铁站)\",\n" +
                "\t\t\"CompanyId\": \"huwochuxing\",\n" +
                "\t\t\"Address\": 370100,\n" +
                "\t\t\"DestLongitude\": 106459644,\n" +
                "\t\t\"FareType\": \"6269686671933024\",\n" +
                "\t\t\"OrderId\": \"2207124471520086611\",\n" +
                "\t\t\"DepLongitude\": 106469925,\n" +
                "\t\t\"Encrypt\": 1,\n" +
                "\t\t\"DepLatitude\": 29559939,\n" +
                "\t\t\"DepartTime\": 20220712153439,\n" +
                "\t\t\"Departure\": \"重庆大学A区(南1门)\",\n" +
                "\t\t\"DestLatitude\": 29557360\n" +
                "\t}, {\n" +
                "\t\t\"OrderTime\": 20220712153439,\n" +
                "\t\t\"Destination\": \"沙坪坝(地铁站)\",\n" +
                "\t\t\"CompanyId\": \"huwochuxing\",\n" +
                "\t\t\"Address\": 370100,\n" +
                "\t\t\"DestLongitude\": 106459644,\n" +
                "\t\t\"FareType\": \"6269686671933024\",\n" +
                "\t\t\"OrderId\": \"2207124471520086611\",\n" +
                "\t\t\"DepLongitude\": 106469925,\n" +
                "\t\t\"Encrypt\": 1,\n" +
                "\t\t\"DepLatitude\": 29559939,\n" +
                "\t\t\"DepartTime\": 20220712153439,\n" +
                "\t\t\"Departure\": \"重庆大学A区(南1门)\",\n" +
                "\t\t\"DestLatitude\": 29557360\n" +
                "\t}, {\n" +
                "\t\t\"OrderTime\": 20220712153439,\n" +
                "\t\t\"Destination\": \"沙坪坝(地铁站)\",\n" +
                "\t\t\"CompanyId\": \"huwochuxing\",\n" +
                "\t\t\"Address\": 370100,\n" +
                "\t\t\"DestLongitude\": 106459644,\n" +
                "\t\t\"FareType\": \"6269686671933024\",\n" +
                "\t\t\"OrderId\": \"2207124471520086611\",\n" +
                "\t\t\"DepLongitude\": 106469925,\n" +
                "\t\t\"Encrypt\": 1,\n" +
                "\t\t\"DepLatitude\": 29559939,\n" +
                "\t\t\"DepartTime\": 20220712153439,\n" +
                "\t\t\"Departure\": \"重庆大学A区(南1门)\",\n" +
                "\t\t\"DestLatitude\": 29557360\n" +
                "\t}],\n" +
                "\t\"Source\": \"huwochuxing\"\n" +
                "}";
        RequestBody requestBody = RequestBody.create(jsonStr, MediaType.parse("application/json; charset=utf-8"));
        builder.url("https://123.233.250.83/dev_test/orderCreate/addTrafficOrderCreateList")
                .post(requestBody);
        req = builder.build();
        Call call = okHttpClient.newCall(req);
        //异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                String response = "{\"status\":\"failure\",\"message\":\"" + e.getMessage() + "\",\"result\":null}";
                if (e instanceof SocketTimeoutException) {
                    response = "{\"status\":\"failure\",\"message\":\"Socket网络请求超时\",\"result\":null,\"code\":0}";
                }
                if (e instanceof ConnectException) {
                    response = "{\"status\":\"failure\",\"message\":\"网络请求超时\",\"result\":null,\"code\":0}";
                }
                log.debug("with response: [{}] ", response);
                System.out.println(response);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                String ret;
                if (response.isSuccessful()) {
                    String message = Objects.requireNonNull(response.body()).string();
                    message = message.startsWith("\uFEFF") ? message.replace("\uFEFF", "") : message;
                    if ("".equals(message) || "OK".equalsIgnoreCase(message)) {
                        ret = String.format("{\"code\":\"%s\", \"message\":\"%s\"}", response.code(), response.message());
                    } else {
                        try {

                            ret = message;
                        } catch (Exception e) {
                            ret = String.format("{\"code\":\"%s\", \"message\":\"%s\"}", response.code(), response.message());
                        }
                    }
                } else {
                    ret = "{\"code\":" + code + ",\"message\":\"" + response.message() + "\",\"result\":\"response is false\"}";
                }
                System.out.println(ret);
                log.debug("with result: {}", ret);

                Objects.requireNonNull(response.body()).close();
                response.close();

            }
        });

    }

    /**
     * getPlkformCer: 从CER文件中获取公钥
     *
     * @param strCer 文件存储目录
     */
    private static PublicKey getPlkformCer(String strCer) {
        FileInputStream bais;
        try {
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            bais = new FileInputStream(strCer);
            X509Certificate Cert = (X509Certificate) certificatefactory.generateCertificate(bais);
            PublicKey pk = Cert.getPublicKey();
            BASE64Encoder bse = new BASE64Encoder();
            System.out.println("public key:" + bse.encode(pk.getEncoded()));
            return pk;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getSslSocketFactoryAndTrustManagerFactory(InputStream certificates) {
        SSLContext sslContext = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            Certificate ca;
            try {
                ca = certificateFactory.generateCertificate(certificates);

            } finally {
                certificates.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
            javax.net.ssl.TrustManager[] trustManagers = tmf.getTrustManagers();
            TrustManager trustManager = trustManagers[0];

            // Create an SSLContext that uses our TrustManager
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);


            SSLSocketFactory sslSocketFactory = sslContext != null ? sslContext.getSocketFactory() : null;

            sslSocketFactory1 = sslSocketFactory;
            x509TrustManager = (X509TrustManager)trustManager;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
