package com.example.facebookwebhookdemo.api.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * @ClassName WebhookResponseMessageParam
 * @Description TODO
 * @Author Kylin
 * @Date 2021/1/22 22:15
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookResponseMessageParam {
  private String text;
  private ResponseAttachment attachment;
  private List<QuickReply> quickReplies;


  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public ResponseAttachment getAttachment() {
    return attachment;
  }

  public void setAttachment(ResponseAttachment attachment) {
    this.attachment = attachment;
  }

  public List<QuickReply> getQuickReplies() {
    return quickReplies;
  }

  public void setQuickReplies(List<QuickReply> quickReplies) {
    this.quickReplies = quickReplies;
  }

  public static class ResponseAttachment {
    private String type;
    private ResponsePayload payload;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public ResponsePayload getPayload() {
      return payload;
    }

    public void setPayload(ResponsePayload payload) {
      this.payload = payload;
    }
  }

  public static class ResponsePayload {
    private String templateType;
    private List<ResponseElement> elements;

    public String getTemplateType() {
      return templateType;
    }

    public void setTemplateType(String templateType) {
      this.templateType = templateType;
    }

    public List<ResponseElement> getElements() {
      return elements;
    }

    public void setElements(List<ResponseElement> elements) {
      this.elements = elements;
    }
  }

  public static class ResponseElement {
    private String title;
    private String subtitle;
    private String imageUrl;
    private List<ResponseButton> buttons;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getSubtitle() {
      return subtitle;
    }

    public void setSubtitle(String subtitle) {
      this.subtitle = subtitle;
    }

    public String getImageUrl() {
      return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
    }

    public List<ResponseButton> getButtons() {
      return buttons;
    }

    public void setButtons(List<ResponseButton> buttons) {
      this.buttons = buttons;
    }
  }

  public static class ResponseButton {
    private String type;
    private String title;
    private String payload;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

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

  public static class QuickReply {
    private String contentType;
    private String title;
    private String payload;
    private String imageUrl;

    public String getContentType() {
      return contentType;
    }

    public void setContentType(String contentType) {
      this.contentType = contentType;
    }

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

    public String getImageUrl() {
      return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
    }
  }
}
