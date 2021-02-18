package com.example.facebookwebhookdemo.api.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * @ClassName WebhookMessageParam
 * @Description TODO
 * @Author Kylin
 * @Date 2021/1/22 17:16
 **/
public class WebhookMessageParam {
  private String mid;
  private String text;
  private WebhookQuickReplyParam quickReply;
  private Integer seq;
  private List<Attachment> attachments;


  public String getMid() {
    return mid;
  }

  public void setMid(String mid) {
    this.mid = mid;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public WebhookQuickReplyParam getQuickReply() {
    return quickReply;
  }

  public void setQuickReply(WebhookQuickReplyParam quickReply) {
    this.quickReply = quickReply;
  }

  public Integer getSeq() {
    return seq;
  }

  public void setSeq(Integer seq) {
    this.seq = seq;
  }

  public List<Attachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<Attachment> attachments) {
    this.attachments = attachments;
  }

  public static class Attachment {
    private String type;
    private Payload payload;
    private String title;
    @JsonProperty("URL")
    private String url;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public Payload getPayload() {
      return payload;
    }

    public void setPayload(Payload payload) {
      this.payload = payload;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public static class Payload{
      private String url;

      public String getUrl() {
        return url;
      }

      public void setUrl(String url) {
        this.url = url;
      }
    }
  }
}
