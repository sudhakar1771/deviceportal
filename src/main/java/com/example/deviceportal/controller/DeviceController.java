package com.example.deviceportal.controller;

import com.example.deviceportal.model.Device;
import com.example.deviceportal.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Controller
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("device", new Device());
        return "index";
    }

    @PostMapping("/submit")
    @ResponseBody
    public String submit(@RequestParam String serialNumber,
                         @RequestParam String portNumber,
                         @RequestParam String type) throws Exception {
        Device device = new Device();
        device.setSerialNumber(serialNumber);
        device.setPortNumber(portNumber);
        device.setType(type);

        String privateKeyPem = "-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----";
        String algorithm = "SHA256withRSA";

        PrivateKey privateKey = getPrivateKeyFromPem(privateKeyPem);
        String signature = deviceService.generateSignature(device, algorithm, privateKey);

        return signature;
    }

    private PrivateKey getPrivateKeyFromPem(String pem) throws Exception {
        String privateKeyPem = pem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPem);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
