package com.tclab.igtools.commons.fbCommonsClient.service;

import com.tclab.igtools.commons.fbCommonsClient.dto.FbCommonsTokenDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface FbCommonsFeignService {

    @GetMapping("/{apiVersion}/fb/token/get")
    FbCommonsTokenDto getToken(
        @PathVariable String apiVersion,
        @RequestParam(name = "p") String pass);

}
