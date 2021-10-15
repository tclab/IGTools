package com.tclab.igtools.media.controller;

import com.tclab.igtools.media.dto.HydrateMediaDto;
import com.tclab.igtools.media.dto.PostDto;
import com.tclab.igtools.media.dto.MediaResponseDto;
import com.tclab.igtools.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/media")
@RequiredArgsConstructor
public class MediaController {

  private final MediaService igService;

  @PostMapping("/post/manual")
  public ResponseEntity<MediaResponseDto> postResourceManually(@RequestBody PostDto postInfo)  throws Exception{
    return ResponseEntity.ok(igService.postResource(postInfo));
  }

  @PostMapping("/post/{igBusinessAccountId}")
  public ResponseEntity<MediaResponseDto> postResource(@PathVariable Long igBusinessAccountId) {
    return ResponseEntity.ok(igService.post(igBusinessAccountId));
  }

  @PostMapping("/hydrate")
  public ResponseEntity<MediaResponseDto> hydratePosts(@RequestBody(required = false) HydrateMediaDto hydrateMediaDto) throws Exception {
    return ResponseEntity.ok(igService.hydrate(hydrateMediaDto));
  }

}
