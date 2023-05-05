package com.github.tomakehurst.wiremock.extension.pubsub;

public class RedisConnectionException extends RuntimeException {
  public RedisConnectionException(String message) {
    super(message);
  }
}
