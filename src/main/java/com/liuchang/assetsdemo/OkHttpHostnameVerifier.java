package com.liuchang.assetsdemo;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class OkHttpHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession sslSession) {
        if ("123.233.250.83".equals(hostname)) {
            System.out.println("好样的。。。。。。");
            return true;
        } else {
            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify(hostname, sslSession);
        }

    }
}
