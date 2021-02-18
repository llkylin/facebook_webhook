package com.example.facebookwebhookdemo.api.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName WebhookMessageResponseParam
 * @Description TODO
 * @Author Kylin
 * @Date 2021/1/22 21:21
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookResponseParam {
  @JsonProperty("messaging_type")
  private String messagingType;
  @JsonProperty("message")
  private WebhookResponseMessageParam message;
  @JsonProperty("recipient")
  private WebhookRecipientParam recipient;
  @JsonProperty("sender_action")
  private String senderAction;

  public String getMessagingType() {
    return messagingType;
  }

  public void setMessagingType(String messagingType) {
    this.messagingType = messagingType;
  }

  public WebhookResponseMessageParam getMessage() {
    return message;
  }

  public void setMessage(WebhookResponseMessageParam message) {
    this.message = message;
  }

  public WebhookRecipientParam getRecipient() {
    return recipient;
  }

  public void setRecipient(WebhookRecipientParam recipient) {
    this.recipient = recipient;
  }

  public String getSenderAction() {
    return senderAction;
  }

  public void setSenderAction(String senderAction) {
    this.senderAction = senderAction;
  }
}
