package com.example.facebookwebhookdemo.api.param;

import java.util.List;

/**
 * @ClassName WebhookEntryParam
 * @Description TODO
 * @Author Kylin
 * @Date 2021/1/22 17:15
 **/
public class WebhookEntryParam {
  private String id;
  private Long time;
  private List<WebhookMessagingParam> messaging;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public List<WebhookMessagingParam> getMessaging() {
    return messaging;
  }

  public void setMessaging(List<WebhookMessagingParam> messaging) {
    this.messaging = messaging;
  }
}
