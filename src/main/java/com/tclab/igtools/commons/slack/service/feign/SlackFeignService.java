package com.tclab.igtools.commons.slack.service.feign;

import com.tclab.igtools.commons.slack.dto.SlackDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface SlackFeignService {

    // SEND IMAGE

    @PostMapping("/{token}")
    void sendMessage(
        @PathVariable String token,
        @RequestBody SlackDto body);


}
