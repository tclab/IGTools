package com.tclab.igtools.ig.controller;

import com.tclab.igtools.ig.dto.IgPostDto;
import com.tclab.igtools.ig.dto.IgResponseDto;
import com.tclab.igtools.ig.service.IgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/ig")
@RequiredArgsConstructor
public class IgController {

  private final IgService igService;

  @PostMapping("/post/image")
  public ResponseEntity<IgResponseDto> postImage(@RequestBody IgPostDto postInfo) throws Exception {
    return ResponseEntity.ok(igService.postImage(postInfo));
  }

}
