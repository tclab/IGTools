package com.tclab.igtools.commons.slack.service.feign;

import com.tclab.igtools.commons.slack.config.SlackFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${api.slack.name}", url = "${api.slack.url}", configuration = SlackFeignConfig.class)
public interface SlackFeignClient  extends SlackFeignService {

}
