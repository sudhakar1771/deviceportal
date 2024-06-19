package com.example.deviceportal.service;

import com.example.deviceportal.model.Device;
import com.example.deviceportal.util.SignatureUtil;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;

@Service
public class DeviceService {

    public String generateSignature(Device device, String algorithm, PrivateKey privateKey) throws Exception {
        String json = toJson(device);
        return SignatureUtil.sign(json, algorithm, privateKey);
    }

    private String toJson(Device device) {
        // Convert Device object to JSON string
        return String.format("{\"serialNumber\":\"%s\",\"portNumber\":\"%s\",\"type\":\"%s\"}",
                device.getSerialNumber(), device.getPortNumber(), device.getType());
    }
}
