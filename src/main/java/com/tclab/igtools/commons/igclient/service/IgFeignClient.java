package com.tclab.igtools.commons.igclient.service;

import com.tclab.igtools.commons.igclient.config.IgFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${api.graph.name}", url = "${api.graph.url}", configuration = IgFeignConfig.class)
public interface IgFeignClient extends IgFeignService {
}
