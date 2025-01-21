package com.remonsinnema.read2understand.application.config;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChainConfig {

  @Bean
  public OkHttpClient okHttpClient() {
    return new OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.MINUTES) // Connection timeout
        .readTimeout(30, TimeUnit.MINUTES) // Read timeout
        .writeTimeout(30, TimeUnit.MINUTES) // Write timeout
        .build();
  }
}
