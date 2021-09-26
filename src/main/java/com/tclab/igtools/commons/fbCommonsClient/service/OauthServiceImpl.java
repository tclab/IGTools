package com.tclab.igtools.commons.fbCommonsClient.service;

import com.tclab.igtools.commons.fbCommonsClient.service.feign.FbCommonsFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService {

  @Value("${api.fb-commons.pss}")
  private String fbCommonsPss;

  @Value("${api.fb-commons.api-version}")
  private String fbCommonsApiVersion;

  private final FbCommonsFeignService fbCommonsFeignService;

  @Override
  public String getToken() {
    return fbCommonsFeignService.getToken(fbCommonsApiVersion, fbCommonsPss).getAccessToken();
  }
}
