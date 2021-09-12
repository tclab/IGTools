package com.tclab.igtools.ig.service;

import com.tclab.igtools.commons.igclient.dto.MediaPublishResponseDto;
import com.tclab.igtools.commons.igclient.dto.PostMediaResponseDto;
import com.tclab.igtools.commons.igclient.dto.PublishStatusResponseDto;
import com.tclab.igtools.commons.igclient.service.IgFeignService;
import com.tclab.igtools.commons.oauth.service.OauthService;
import com.tclab.igtools.ig.dto.IgPostDto;
import com.tclab.igtools.ig.dto.IgResponseDto;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IgServiceImpl implements IgService {

  @Value("${post.retries}")
  private Integer postRetries;

  private static final String FIELDS = "status_code";
  private static final String FINISHED_STATUS = "FINISHED";

  private final OauthService oauthService;
  private final IgFeignService igFeignService;

  @Override
  public IgResponseDto postImage(IgPostDto postDto) throws Exception {

    IgResponseDto igResponseDto = IgResponseDto.builder().build();
    try {

      // Get fb graph api token
      log.info("Getting token...");
      String graphApiToken = oauthService.getToken();

      // Post image
      log.info("Posting image");
      PostMediaResponseDto postMediaResponseDto = igFeignService.postImage(
          postDto.getIgBussinesAccount(),
          graphApiToken,
          postDto.getUrl(),
          postDto.getCaption());

      // Check post status
      log.info("Checking image post status");
      PublishStatusResponseDto publishStatusResponseDto = null;
      for (int i = 0; i < postRetries; i++) {
        publishStatusResponseDto = igFeignService.publishStatus(
            postMediaResponseDto.getCreationId(),
            graphApiToken,
            FIELDS);

        Thread.sleep(1000);
        if (FINISHED_STATUS.equals(publishStatusResponseDto.getStatus_code())) {
          break;
        }
        Thread.sleep(2000);
      }

      // If publish status is not finish, throw an exception.
      if (Objects.nonNull(publishStatusResponseDto)
          && !FINISHED_STATUS.equals(publishStatusResponseDto.getStatus_code())) {
        log.error("Post did not reached FINISHED status");
        igResponseDto.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
        igResponseDto.setMessage("Post did not reached FINISHED status");
      }

      log.info("Publishing image");
      MediaPublishResponseDto mediaPublishResponseDto = igFeignService.mediaPublish(
          postDto.getIgBussinesAccount(), graphApiToken,
          postMediaResponseDto.getCreationId());

      log.info("Image posted!!");
      igResponseDto.setStatus(HttpStatus.OK.value());
      igResponseDto.setMessage("Image posted!!");

    } catch (Exception e) {
      log.error("Something went wrong when publishing the image :(");
      igResponseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      igResponseDto.setMessage("Something went wrong when publishing the image :(");
    }
    return igResponseDto;

  }
}
