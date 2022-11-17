package com.example.webclienttutorial.client;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class MakeSignature implements CloudAuthentication {

    protected String accessKey;
    protected String secretKey;

    @Override
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
    @Override
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public HttpHeaders getNCloudHttpHeaders(HttpMethod method, String url)
        throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        String timeMillisString = String.valueOf(
            System.currentTimeMillis()
        );


        HttpHeaders ncloudHeaders = new HttpHeaders();

        String signature = makeSignature(method.name(), url, timeMillisString);

        ncloudHeaders.add("Content-Type", "application/xml; charset=utf-8");
        ncloudHeaders.add("x-ncp-apigw-timestamp", timeMillisString);
        ncloudHeaders.add("x-ncp-iam-access-key", accessKey);
        ncloudHeaders.add("x-ncp-apigw-signature-v2", signature);


        return ncloudHeaders;
    }

    protected String makeSignature(
        String method,
        String url,
        String timeMillisString
    ) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String space = " ";          // one space
        String newLine = "\n";          // new line

        String message = new StringBuilder()
            .append(method)
            .append(space)
            .append(url)
            .append(newLine)
            .append(timeMillisString)
            .append(newLine)
            .append(accessKey)
            .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }


}
