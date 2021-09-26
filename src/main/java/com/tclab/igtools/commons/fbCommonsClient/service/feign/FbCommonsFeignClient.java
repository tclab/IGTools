package com.tclab.igtools.commons.fbCommonsClient.service.feign;

import com.tclab.igtools.commons.fbCommonsClient.config.FbCommonsFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${api.fb-commons.name}", url = "${api.fb-commons.url}", configuration = FbCommonsFeignConfig.class)
public interface FbCommonsFeignClient extends FbCommonsFeignService {
}
