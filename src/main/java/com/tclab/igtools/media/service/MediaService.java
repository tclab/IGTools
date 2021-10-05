package com.tclab.igtools.media.service;

import com.tclab.igtools.media.dto.HydrateMediaDto;
import com.tclab.igtools.media.dto.PostDto;
import com.tclab.igtools.media.dto.MediaResponseDto;

public interface MediaService {

  MediaResponseDto post(Long igBusinessAccountId);
  MediaResponseDto postResource(PostDto postDto) throws Exception;
  MediaResponseDto hydrate(HydrateMediaDto igGetMediaDto) throws Exception;
  MediaResponseDto hydratePostRepo(HydrateMediaDto igGetMediaDto) throws Exception;

}
