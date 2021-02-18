package com.example.facebookwebhookdemo.api.service;

import com.example.facebookwebhookdemo.api.param.WebhookParam;

public interface WebhookHandleService {
  void handle(WebhookParam param);
}
