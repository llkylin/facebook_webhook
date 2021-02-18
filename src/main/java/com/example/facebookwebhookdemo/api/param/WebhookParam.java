package com.example.facebookwebhookdemo.api.param;

import java.util.List;

/**
 * @ClassName WebhookParam
 * @Description TODO
 * @Author Kylin
 * @Date 2021/1/22 17:14
 **/
public class WebhookParam {
  private String object;
  private List<WebhookEntryParam> entry;

  public String getObject() {
    return object;
  }

  public void setObject(String object) {
    this.object = object;
  }

  public List<WebhookEntryParam> getEntry() {
    return entry;
  }

  public void setEntry(List<WebhookEntryParam> entry) {
    this.entry = entry;
  }
}
