package com.example.facebookwebhookdemo.api.service.impl;

import static java.util.Objects.nonNull;

import com.example.facebookwebhookdemo.api.param.WebhookEntryParam;
import com.example.facebookwebhookdemo.api.param.WebhookGetStartedParam;
import com.example.facebookwebhookdemo.api.param.WebhookGetStartedParam.CallToAction;
import com.example.facebookwebhookdemo.api.param.WebhookGetStartedParam.GetStarted;
import com.example.facebookwebhookdemo.api.param.WebhookGetStartedParam.Greeting;
import com.example.facebookwebhookdemo.api.param.WebhookGetStartedParam.PersistentMenu;
import com.example.facebookwebhookdemo.api.param.WebhookMessageParam;
import com.example.facebookwebhookdemo.api.param.WebhookMessagingParam;
import com.example.facebookwebhookdemo.api.param.WebhookMessagingParam.Postback;
import com.example.facebookwebhookdemo.api.param.WebhookParam;
import com.example.facebookwebhookdemo.api.param.WebhookRecipientParam;
import com.example.facebookwebhookdemo.api.param.WebhookResponseMessageParam;
import com.example.facebookwebhookdemo.api.param.WebhookResponseMessageParam.QuickReply;
import com.example.facebookwebhookdemo.api.param.WebhookResponseMessageParam.ResponseAttachment;
import com.example.facebookwebhookdemo.api.param.WebhookResponseMessageParam.ResponseButton;
import com.example.facebookwebhookdemo.api.param.WebhookResponseMessageParam.ResponseElement;
import com.example.facebookwebhookdemo.api.param.WebhookResponseMessageParam.ResponsePayload;
import com.example.facebookwebhookdemo.api.param.WebhookResponseParam;
import com.example.facebookwebhookdemo.api.service.WebhookHandleService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.corba.se.spi.ior.ObjectId;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * @ClassName WebhookHandleServiceImpl
 * @Description TODO
 * @Author Kylin
 * @Date 2021/1/22 18:08
 **/
@Service
public class WebhookHandleServiceImpl implements WebhookHandleService {
  private static final Logger LOGGER = LoggerFactory.getLogger(WebhookHandleServiceImpl.class);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final MediaType JSON = MediaType.parse("application/json");
  private static final String WEBHOOK_API_URL = "https://graph.facebook.com/v2.6/me/messages?access_token=";
  private static final String WEBHOOK_GET_STARTED_URL = "https://graph.facebook.com/v2.6/me/messenger_profile?access_token=";
  private static final String TOKEN_URL = "https://graph.facebook.com/oauth/access_token?";
  private static final OkHttpClient OK_HTTP_WEBHOOK_API = new OkHttpClient.Builder()
      .callTimeout(Duration.ofSeconds(5L))
      .retryOnConnectionFailure(true)
      .build();

  private static final String VERIFY_TOKEN = "EAAKTg9FZBZAZC8BANdLeKaYg3INlz9MJKhA06aNXWVz40k7zhJSpUwWhdOzp6ZBR6tYkXXPMOJiAHkXIulPH0gcAdMd6xtSNKZCWZCtwFw6WkAT4yxvcsI63ypAf6wpFlqNJrwzxgZAIEExES5zvZBQX36Ooz1Wik9V9AwBCYNMWCWcJyxNDyMa9qwfh6oz3Sg0ZD";

  static {
    OBJECT_MAPPER.registerModule(new JavaTimeModule());
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
  }

  /**
   * 电话号码验证
   */
  private static final String PHONE_REGEX = "^[0-9\\+\\-\\(\\)]{9,20}$";
  public static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);


  @PostConstruct
  public void postConstruct() {
    this.getStartedAndButton();
  }


  @Override
  public void handle(WebhookParam param) {
    List<WebhookEntryParam> entry = param.getEntry();
    //获取 messages 事件
    List<WebhookEntryParam> messageEventList = entry.stream()
        .filter(e -> e.getMessaging().stream().anyMatch(me -> nonNull(me.getMessage())))
        .collect(Collectors.toList());

    if (!messageEventList.isEmpty()) {
      for (WebhookEntryParam webhookEntryParam : messageEventList) {
        List<WebhookMessagingParam> messaging = webhookEntryParam.getMessaging();
        for (WebhookMessagingParam webhookMessagingParam : messaging) {
          //开启 mark_seen
          WebhookResponseParam responseParam = new WebhookResponseParam();
          responseParam.setSenderAction("typing_on");
          WebhookRecipientParam recipient = new WebhookRecipientParam();
          recipient.setId(webhookMessagingParam.getSender().getId());
          responseParam.setRecipient(recipient);
          WebhookResponseParam responseParam1 = new WebhookResponseParam();
          responseParam1.setSenderAction("mark_seen");
          WebhookRecipientParam recipient1 = new WebhookRecipientParam();
          recipient1.setId(webhookMessagingParam.getSender().getId());
          responseParam1.setRecipient(recipient1);
          this.callSendAPI(responseParam);
          //处理 messages 事件 - 文本类消息
          this.handleMessageEvent(webhookMessagingParam.getSender().getId(), webhookMessagingParam.getMessage());
        }
      }
    }

    //处理回调
    List<WebhookEntryParam> postbackList = entry.stream()
        .filter(e -> e.getMessaging().stream().anyMatch(me -> nonNull(me.getPostback())))
        .collect(Collectors.toList());
    if (!postbackList.isEmpty()) {
      WebhookEntryParam postbackParam = postbackList.get(0);
      List<WebhookMessagingParam> messaging = postbackParam.getMessaging();
      WebhookMessagingParam webhookMessagingParam = messaging.get(0);
      this.handlePostback(webhookMessagingParam.getSender().getId(), webhookMessagingParam.getPostback());
    }

  }


  //处理 message是事件，并回调
  private void handleMessageEvent(String senderId, WebhookMessageParam messageParam) {
    String responseValue;
    WebhookResponseParam responseParam = new WebhookResponseParam();
    WebhookResponseMessageParam message = new WebhookResponseMessageParam();
    if (StringUtils.hasText(messageParam.getText())) {
      if ("Pick a color".equals(messageParam.getText())) {
        responseParam.setMessagingType("RESPONSE");
        message.setText("Pick a color");
        QuickReply quickReply = new QuickReply();
        quickReply.setContentType("text");
        quickReply.setTitle("Red");
        quickReply.setPayload("<POSTBACK_PAYLOAD>");
        quickReply.setImageUrl("http://example.com/img/red.png");
        List<QuickReply> quickReplies = new ArrayList<>();
        quickReplies.add(quickReply);
        message.setQuickReplies(quickReplies);
      } else if (messageParam.getText().indexOf("phone") >= 0) {
        responseParam.setMessagingType("RESPONSE");
        message.setText("phone number");
        QuickReply quickReply = new QuickReply();
        quickReply.setContentType("user_phone_number");
        List<QuickReply> quickReplies = new ArrayList<>();
        quickReplies.add(quickReply);
        message.setQuickReplies(quickReplies);
      } else if (PHONE_PATTERN.matcher(messageParam.getText()).matches()) {
        responseValue = "绑定成功!!";
        message.setText(responseValue);
      } else {
        responseValue = String.format("You sent the message: %s . Now send me an image!", messageParam.getText());
        message.setText(responseValue);
      }
    } else if (nonNull(messageParam.getAttachments())) {
      String url = messageParam.getAttachments().get(0).getPayload().getUrl();
      ResponseAttachment attachment = new ResponseAttachment();
      attachment.setType("template");
      ResponsePayload payload = new ResponsePayload();
      payload.setTemplateType("generic");

      List<ResponseElement> elements = new ArrayList<>();

      ResponseElement element = new ResponseElement();
      element.setTitle("Is this the right picture?");
      element.setSubtitle("Tap a button to answer");
      element.setImageUrl(url);

      List<ResponseButton> buttons = new ArrayList<>();
      ResponseButton button = new ResponseButton();
      button.setType("postback");
      button.setTitle("YES!");
      button.setPayload("yes");
      buttons.add(button);
      ResponseButton button1 = new ResponseButton();
      button1.setType("postback");
      button1.setTitle("NO!");
      button1.setPayload("no");
      buttons.add(button1);
      element.setButtons(buttons);
      elements.add(element);
      payload.setElements(elements);
      attachment.setPayload(payload);
      message.setAttachment(attachment);
    }

    responseParam.setMessage(message);
    WebhookRecipientParam recipient = new WebhookRecipientParam();
    recipient.setId(senderId);
    responseParam.setRecipient(recipient);
    this.callSendAPI(responseParam);

  }

  private void handlePostback(String senderId, Postback postback) {
    WebhookResponseParam responseParam = new WebhookResponseParam();
    WebhookResponseMessageParam message = new WebhookResponseMessageParam();
    String responseValue = "";
    if (postback.getPayload().equals("yes")) {
      responseValue = "Thanks";
      message.setText(responseValue);
    } else if ("no".equals(postback.getPayload())) {
      responseValue = "Oops, try sending another image";
      message.setText(responseValue);
    } else if ("getStarted".equals(postback.getPayload())) {
      responseValue = "欢迎光临!! 请选择固定菜单列表里的功能";
      message.setText(responseValue);
    } else if ("Bingding_phone_number".equals(postback.getPayload())) {
      responseParam.setMessagingType("RESPONSE");
      message.setText("你确定绑定当前手机号吗？");
      QuickReply quickReply = new QuickReply();
      quickReply.setContentType("user_phone_number");
      List<QuickReply> quickReplies = new ArrayList<>();
      quickReplies.add(quickReply);
      message.setQuickReplies(quickReplies);
    }

    responseParam.setMessage(message);
    WebhookRecipientParam recipient = new WebhookRecipientParam();
    recipient.setId(senderId);
    responseParam.setRecipient(recipient);
    this.callSendAPI(responseParam);

  }


  //发送API消息
  private void callSendAPI(WebhookResponseParam responseParam) {
    // -- 回调
    String paramJson;
    try {
      paramJson = OBJECT_MAPPER.writeValueAsString(responseParam);
      RequestBody requestBody = RequestBody.create(JSON, paramJson);
      Request.Builder requestBuilder = new Request.Builder()
          .header("Content-Type", "application/json")
          .url(WEBHOOK_API_URL + VERIFY_TOKEN)
          .post(requestBody);
      Call call = OK_HTTP_WEBHOOK_API.newCall(requestBuilder.build());
      LOGGER.info("webhook event send API success! paramJson: {}", paramJson);
      Response response = call.execute();
      if (nonNull(response.body())) {
        LOGGER.info("webhook event response body: {}", response.body().string());
      }
    } catch (IOException e) {
      LOGGER.error("webhook event call send API failed !, message: {}", e.toString(), e);
    }
  }

  //get_started
  private void getStartedAndButton() {
    // -- 回调
    String paramJson;
    try {
      WebhookGetStartedParam getStartedParam = new WebhookGetStartedParam();
      //获取启动按钮
      GetStarted getStarted = new GetStarted();
      getStarted.setPayload("getStarted");
      getStartedParam.setGetStarted(getStarted);

      //欢迎语
      Greeting greeting = new Greeting();
      greeting.setLocale("default");
      greeting.setText("Hello {{user_first_name}},欢迎加入 Flash Express Club！！点击 get_started 进入");
      List<Greeting> greetings = new ArrayList<>();
      greetings.add(greeting);
      getStartedParam.setGreeting(greetings);

      // 设置下拉菜单
      PersistentMenu persistentMenu = new PersistentMenu();
      persistentMenu.setLocale("default");
      persistentMenu.setComposerInputDisabled(false);

      List<CallToAction> callToActions = new ArrayList<>();
      CallToAction callToAction = new CallToAction();
      callToAction.setTitle("手机号绑定");
      callToAction.setType("postback");
      callToAction.setWebviewHeightRatio("full");
      callToAction.setPayload("Bingding_phone_number");
      callToActions.add(callToAction);

      CallToAction callToAction1 = new CallToAction();
      callToAction1.setTitle("运单查询");
      callToAction1.setType("web_url");
      callToAction1.setUrl("https://www.flashexpress.co.th/zh/check-price");
      callToAction1.setWebviewHeightRatio("full");
      callToActions.add(callToAction1);

      CallToAction callToAction2 = new CallToAction();
      callToAction2.setTitle("运费估算");
      callToAction2.setType("web_url");
      callToAction2.setUrl("https://www.flashexpress.co.th/zh/check-price");
      callToAction2.setWebviewHeightRatio("full");
      callToActions.add(callToAction2);

      persistentMenu.setCallToActions(callToActions);
      List<PersistentMenu> persistentMenus = new ArrayList<>();
      persistentMenus.add(persistentMenu);
      getStartedParam.setPersistentMenu(persistentMenus);
      paramJson = OBJECT_MAPPER.writeValueAsString(getStartedParam);
      RequestBody requestBody = RequestBody.create(JSON, paramJson);
      Request.Builder requestBuilder = new Request.Builder()
          .header("Content-Type", "application/json")
          .url(WEBHOOK_GET_STARTED_URL + VERIFY_TOKEN)
          .post(requestBody);
      Call call = OK_HTTP_WEBHOOK_API.newCall(requestBuilder.build());
      LOGGER.info("webhook get_started send success! paramJson: {}", paramJson);
      Response response = call.execute();
      if (nonNull(response.body())) {
        LOGGER.info("webhook get_started response body: {}", response.body().string());
      }
    } catch (IOException e) {
      LOGGER.error("webhook get_started send API failed !, message: {}", e.toString(), e);
    }

    // 获取token地址
    String getAccessTokenUrl = TOKEN_URL
        // 1. grant_type为固定参数
        + "grant_type=client_credentials"
        // 2. 官网获取的 API Key
        + "&client_id=" + "725144318142463"
        // 3. 官网获取的 Secret Key
        + "&client_secret=" + "e1795b99ef85c58c651c799356b4d966";
    //请求
    Request.Builder requestBuilder = new Request.Builder().url(getAccessTokenUrl).get();

    Call call = OK_HTTP_WEBHOOK_API.newCall(requestBuilder.build());
    try (Response response = call.execute()) {
      if (response.isSuccessful()) {
        LOGGER.info("webhook oauth_token success, response body: {}", response.body().string());
      } else {
        LOGGER.error("webhook oauth_token failed, response body: {}", response.body().string());
      }
    } catch (IOException e) {
      LOGGER.error("webhook get_started send API failed !, message: {}", e.toString(), e);
    }
  }
}
