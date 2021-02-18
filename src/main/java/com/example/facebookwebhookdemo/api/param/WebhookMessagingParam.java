package com.example.facebookwebhookdemo.api.param;

/**
 * @ClassName WebhookMessagingParam
 * @Description TODO
 * @Author Kylin
 * @Date 2021/1/22 17:15
 **/
public class WebhookMessagingParam {
  private Long timestamp;
  private WebhookSenderParam sender;
  private WebhookRecipientParam recipient;
  /** 
   * @Description: 消息类事件
   * @Author kylin
   * @Param: 
   * @Return: 
   * @Date 2021/1/24 16:00
   */
  private WebhookMessageParam message;
  private Postback postback;

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public WebhookSenderParam getSender() {
    return sender;
  }

  public void setSender(WebhookSenderParam sender) {
    this.sender = sender;
  }

  public WebhookRecipientParam getRecipient() {
    return recipient;
  }

  public void setRecipient(WebhookRecipientParam recipient) {
    this.recipient = recipient;
  }

  public WebhookMessageParam getMessage() {
    return message;
  }

  public void setMessage(WebhookMessageParam message) {
    this.message = message;
  }

  public Postback getPostback() {
    return postback;
  }

  public void setPostback(Postback postback) {
    this.postback = postback;
  }

  public static class Postback{
    private String title;
    private String payload;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getPayload() {
      return payload;
    }

    public void setPayload(String payload) {
      this.payload = payload;
    }
  }
}
