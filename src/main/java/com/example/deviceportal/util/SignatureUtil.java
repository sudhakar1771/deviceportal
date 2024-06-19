package com.example.deviceportal.util;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

public class SignatureUtil {

    public static String sign(String data, String algorithm, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signedData = signature.sign();
        return Base64.getEncoder().encodeToString(signedData);
    }
}
