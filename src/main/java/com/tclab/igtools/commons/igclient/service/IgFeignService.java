package com.tclab.igtools.commons.igclient.service;

import com.tclab.igtools.commons.igclient.dto.MediaPublishResponseDto;
import com.tclab.igtools.commons.igclient.dto.PostMediaResponseDto;
import com.tclab.igtools.commons.igclient.dto.PublishStatusResponseDto;
import com.tclab.igtools.commons.igclient.dto.MediaContentResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface IgFeignService {

    // POST IMAGE

    @PostMapping("/${api.graph.api-version}/{igBussinesAccount}/media")
    PostMediaResponseDto postImage(
        @PathVariable String igBussinesAccount,
        @RequestParam(name = "access_token") String token,
        @RequestParam(name = "image_url") String imageUrl,
        @RequestParam(name = "caption") String caption);

    @PostMapping("/${api.graph.api-version}/{igBussinesAccount}/media")
    PostMediaResponseDto postVideo(
        @PathVariable String igBussinesAccount,
        @RequestParam(name = "access_token") String token,
        @RequestParam(name = "video_url") String videoUrl,
        @RequestParam(name = "caption") String caption,
        @RequestParam(name = "media_type") String mediaType);

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



    // GET MEDIA

    @GetMapping("/${api.graph.api-version}/{igBussinesAccount}")
    MediaContentResponseDto getMedia(
        @PathVariable String igBussinesAccount,
        @RequestParam(name = "access_token") String token,
        @RequestParam(name = "fields") String fields);


}
