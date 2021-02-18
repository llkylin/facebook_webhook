package com.example.facebookwebhookdemo.api.controller;


import com.example.facebookwebhookdemo.api.param.WebhookParam;
import com.example.facebookwebhookdemo.api.service.WebhookHandleService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName WebhookController
 * @Description TODO
 * @Author Kylin
 * @Date 2021/1/22 18:00
 **/
@RestController
@RequestMapping(value = "/api/pub/v1/facebook")
public class WebhookController {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebhookController.class);
  private static final String VERIFY_TOKEN = "abcde";

  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
  }

  @Autowired
  private WebhookHandleService webhookHandleService;

  @PostMapping("/webhook")
  public ResponseEntity postWebhook(HttpServletRequest request) {

    BufferedReader br;

    ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
    try {
      br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
      StringBuffer sb = new StringBuffer("");
      String temp;
      while ((temp = br.readLine()) != null) {
        sb.append(temp);
      }
      br.close();

      String params = sb.toString();

      LOGGER.info("paramsjson:" + params);
      String s1 = Base64.getEncoder().encodeToString(sb.toString().getBytes());
      System.out.println(s1);
      String header = request.getHeader("X-Hub-Signature");
      System.out.println(header);

      String s = WebhookController.HMACSha1(params, "e1795b99ef85c58c651c799356b4d966");
      System.out.println(s);

      WebhookParam webhookParam = null;
      // 转化参数。
      try {
        webhookParam = objectMapper.readValue(params, WebhookParam.class);
      } catch (IOException e) {
        LOGGER.error("WebhookController this.objectMapper.readValue formString, {}", params, e);
      }

      if (Objects.isNull(webhookParam) || !"page".equalsIgnoreCase(webhookParam.getObject())) {
        return responseEntity;
      } else {
        this.webhookHandleService.handle(webhookParam);
        return ResponseEntity.ok("EVENT_RECEIVED");
      }

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return responseEntity;
    }
  }


  @GetMapping("/webhook")
  public ResponseEntity getWebhook(@RequestParam("hub.mode") String mode,
      @RequestParam("hub.verify_token") String verifyToken,
      @RequestParam("hub.challenge") String challenge, HttpServletRequest request) {
    LOGGER.info("mode:" + mode + "\n verifyToken:" + verifyToken + "\n challenge:" + challenge);
    System.out.println("----getWebhook-----");
    String header = request.getHeader("X-Hub-Signature");
    System.out.println(header);
    //需要看过来的日志
    if (mode.equalsIgnoreCase("subscribe") && VERIFY_TOKEN.equals(verifyToken)) {
      return ResponseEntity.ok(challenge);
    } else {
      return new ResponseEntity(HttpStatus.FORBIDDEN);
    }
  }


  private static String getFormattedText(byte[] bytes) {
    char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    char[] buf = new char[bytes.length * 2];
    int index = 0;
    for (byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
      buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
      buf[index++] = HEX_CHAR[b & 0xf];
    }

    return new String(buf);
  }

  private static String HMACSha1(String data, String key) {
    SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
    Mac mac;
    try {
      mac = Mac.getInstance("HmacSHA1");
      mac.init(signingKey);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
    byte[] rawHmac = mac.doFinal(data.getBytes());
    return getFormattedText(rawHmac);
  }
}
