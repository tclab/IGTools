package com.tclab.igtools.commons.igclient.service;

import com.tclab.igtools.commons.igclient.dto.MediaPublishResponseDto;
import com.tclab.igtools.commons.igclient.dto.PostMediaResponseDto;
import com.tclab.igtools.commons.igclient.dto.PublishStatusResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface IgFeignService {

    @PostMapping("/${api.graph.api-version}/{igBussinesAccount}/media")
    PostMediaResponseDto postImage(
        @PathVariable String igBussinesAccount,
        @RequestParam(name = "access_token") String token,
        @RequestParam(name = "image_url") String imageUrl,
        @RequestParam(name = "caption") String caption);

    @GetMapping("/${api.graph.api-version}/{creationId}")
    PublishStatusResponseDto publishStatus(
        @PathVariable Long creationId,
        @RequestParam(name = "access_token") String token,
        @RequestParam(name = "fields") String fields);

    @PostMapping("/${api.graph.api-version}/{igBussinesAccount}/media_publish")
    MediaPublishResponseDto mediaPublish(
        @PathVariable String igBussinesAccount,
        @RequestParam(name = "access_token") String token,
        @RequestParam(name = "creation_id") Long creationId);

}
