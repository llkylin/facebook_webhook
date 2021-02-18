package com.example.facebookwebhookdemo.api.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * @ClassName WebhookGetStartedParam
 * @Description TODO
 * @Author Kylin
 * @Date 2021/1/24 20:31
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookGetStartedParam {
  private GetStarted getStarted;
  private List<Greeting> greeting;
  private List<PersistentMenu> persistentMenu;

  public GetStarted getGetStarted() {
    return getStarted;
  }

  public void setGetStarted(GetStarted getStarted) {
    this.getStarted = getStarted;
  }

  public List<Greeting> getGreeting() {
    return greeting;
  }

  public void setGreeting(List<Greeting> greeting) {
    this.greeting = greeting;
  }

  public List<PersistentMenu> getPersistentMenu() {
    return persistentMenu;
  }

  public void setPersistentMenu(List<PersistentMenu> persistentMenu) {
    this.persistentMenu = persistentMenu;
  }

  public static class GetStarted {
    private String payload;

    public String getPayload() {
      return payload;
    }

    public void setPayload(String payload) {
      this.payload = payload;
    }
  }


  public static class PersistentMenu {
    private String locale;
    private Boolean composerInputDisabled;
    private List<CallToAction> callToActions;

    public String getLocale() {
      return locale;
    }

    public void setLocale(String locale) {
      this.locale = locale;
    }

    public Boolean getComposerInputDisabled() {
      return composerInputDisabled;
    }

    public void setComposerInputDisabled(Boolean composerInputDisabled) {
      this.composerInputDisabled = composerInputDisabled;
    }

    public List<CallToAction> getCallToActions() {
      return callToActions;
    }

    public void setCallToActions(List<CallToAction> callToActions) {
      this.callToActions = callToActions;
    }
  }

  public static class CallToAction {
    private String title;
    private String type;
    private String url;
    private String webviewHeightRatio;
    private String payload;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getWebviewHeightRatio() {
      return webviewHeightRatio;
    }

    public void setWebviewHeightRatio(String webviewHeightRatio) {
      this.webviewHeightRatio = webviewHeightRatio;
    }

    public String getPayload() {
      return payload;
    }

    public void setPayload(String payload) {
      this.payload = payload;
    }
  }

  public static class Greeting {
    private String locale;
    private String text;

    public String getLocale() {
      return locale;
    }

    public void setLocale(String locale) {
      this.locale = locale;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }
  }
}
