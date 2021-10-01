package com.tclab.igtools.media.service;

import com.tclab.igtools.account.dto.AccountDto;
import com.tclab.igtools.account.entity.Account;
import com.tclab.igtools.account.service.AccountService;
import com.tclab.igtools.commons.dto.ResultPage;
import com.tclab.igtools.commons.igclient.dto.MediaContentResponseDto;
import com.tclab.igtools.commons.igclient.dto.MediaPublishResponseDto;
import com.tclab.igtools.commons.igclient.dto.PostMediaResponseDto;
import com.tclab.igtools.commons.igclient.dto.PublishStatusResponseDto;
import com.tclab.igtools.commons.igclient.service.FieldsParamBuilder;
import com.tclab.igtools.commons.igclient.service.IgFeignService;
import com.tclab.igtools.commons.fbCommonsClient.service.OauthService;
import com.tclab.igtools.commons.slack.dto.SlackMessageBuilder;
import com.tclab.igtools.commons.slack.dto.SlackMessageBuilder.MessageType;
import com.tclab.igtools.commons.slack.service.SlackService;
import com.tclab.igtools.commons.utils.Utils;
import com.tclab.igtools.media.dto.HydrateMediaDto;
import com.tclab.igtools.media.dto.PostDto;
import com.tclab.igtools.media.dto.MediaResponseDto;
import com.tclab.igtools.media.entity.Post;
import com.tclab.igtools.media.enumerator.MediaType;
import com.tclab.igtools.media.repository.PostRepository;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

  @Value("${post.retries}")
  private Integer postRetries;

  @Value("${post.longevity}")
  private Integer longevity;

  @Value("${post.media_count}")
  private Long mediaCount;

  @Value("${post.caption}")
  private String caption;

  @Value("${spring.application.name}")
  private String serviceName;

  private static final String FIELDS = "status_code";
  private static final String FINISHED_STATUS = "FINISHED";

  private final OauthService oauthService;
  private final IgFeignService igFeignService;
  private final AccountService accountService;
  private final PostRepository postRepository;
  private final SlackService slackService;


  @Override
  public MediaResponseDto post(Long igBusinessAccountId) {
    MediaResponseDto igResponseDto = MediaResponseDto.builder().build();

    try {
      // Get appropiate post from database (first non-published with most likes)
      Post post = postRepository.findFirstByPostedAndIgBusinessAccountIdOrderByLikeCountDesc(false, igBusinessAccountId);
      PostDto postDto = PostDto.fromPost(post,caption);

      // Udate posted status in database
      post.setPosted(true);
      postRepository.save(post);

      // Post resource
      postResource(postDto);

      // Send slack message
      AccountDto accountDto = accountService.findById(igBusinessAccountId);
      slackService.send(SlackMessageBuilder.builder()
          .service(serviceName)
          .type(MessageType.POST)
          .message("Post created!!")
          .account(accountDto.getUsername())
          .igBusinessAccountId(String.valueOf(igBusinessAccountId))
          .resource(postDto.getUrl())
          .build());

      // Success response
      log.info("Post created!!");
      igResponseDto.setStatus(HttpStatus.OK.value());
      igResponseDto.setMessage("Post created!!");
    } catch (Exception e) {
      // Send slack message
      slackService.send(SlackMessageBuilder.builder()
          .service(serviceName)
          .type(MessageType.ERROR)
          .message("Something went wrong creating the post")
          .stackTrace(e.getMessage())
          .build());

      log.error("Something went wrong creating the post", e);
      igResponseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      igResponseDto.setMessage(String.format("Something went wrong creating the post: %s", e.getMessage()));
    }

    return igResponseDto;
  }

  @Override
  public MediaResponseDto postResource(PostDto postDto) throws Exception {
    Assert.isTrue(Objects.nonNull(postDto), "Body information is null!");
    Assert.isTrue(Utils.isNotEmpty(postDto.getType()), "Content type to post is not specified!");

    MediaResponseDto igResponseDto = MediaResponseDto.builder().build();
    // Get fb graph api token
    log.info("Getting token...");
    String graphApiToken = oauthService.getToken();

    PostMediaResponseDto postMediaResponseDto;
    // Post content
    postMediaResponseDto = postContent(postDto, graphApiToken);

    // Check post status
    log.info("Checking resource post status");
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

    // Publish the image
    log.info("Publishing resource");
    MediaPublishResponseDto mediaPublishResponseDto = igFeignService.mediaPublish(
        postDto.getIgBussinesAccount(), graphApiToken,
        postMediaResponseDto.getCreationId());

    // Success response
    log.info("Resource posted!!");
    igResponseDto.setStatus(HttpStatus.OK.value());
    igResponseDto.setMessage("Resource posted!!");

    return igResponseDto;
  }

  private PostMediaResponseDto postContent(PostDto postDto, String graphApiToken)
      throws Exception {
    PostMediaResponseDto postMediaResponseDto;
    if (MediaType.IMAGE.name().equalsIgnoreCase(postDto.getType())) {
      log.info("Posting image");
      postMediaResponseDto = igFeignService.postImage(
          postDto.getIgBussinesAccount(),
          graphApiToken,
          postDto.getUrl(),
          postDto.getCaption());
    } else if (MediaType.VIDEO.name().equalsIgnoreCase(postDto.getType())) {
      log.info("Posting video");
      postMediaResponseDto = igFeignService.postVideo(
          postDto.getIgBussinesAccount(),
          graphApiToken,
          postDto.getUrl(),
          postDto.getCaption(),
          MediaType.VIDEO.name());
    } else {
      throw new Exception(String.format("Content type %s not supported!", postDto.getType()));
    }
    return postMediaResponseDto;
  }

  @Override
  public MediaResponseDto hydratePostRepo(HydrateMediaDto hydrateMediaDto) {

    MediaResponseDto igResponseDto = MediaResponseDto.builder().build();

    try {
      // depurate old data
      log.info("Depurate old data...");
      try {
        postRepository.deleteByTimestampBefore(ZonedDateTime.now().minusDays(longevity));
      } catch (Exception e) {
        log.error("Error cleaning old posts... ");
      }

      // Get feed accounts for specified igBusinessAccountId
      log.info("Getting feed accounts...");
      Long igBusinessAccountId = Long.valueOf(hydrateMediaDto.getIgBusinessAccountId());
      AccountDto filter = AccountDto.builder().parentIgBusinessAccountId(igBusinessAccountId).build();
      ResultPage<AccountDto> feedAccountList = accountService.findAccount(0, mediaCount.intValue(), filter);
      List<AccountDto> feedAccounts = feedAccountList.getContent();
      if (CollectionUtils.isEmpty(feedAccounts))
        throw new Exception(String.format("Feed account list is empty!. Add feed accounts for %s", hydrateMediaDto.getIgBusinessAccountId()));

      // Get first posts from all feed accounts and save them in post table
      log.info("Getting token...");
      String graphApiToken = oauthService.getToken();
      feedAccounts.forEach(accountDto -> processFeedAccountMedia(hydrateMediaDto, graphApiToken, accountDto));

      // Send slack message
      AccountDto accountDto = accountService.findById(igBusinessAccountId);
      slackService.send(SlackMessageBuilder.builder()
          .service(serviceName)
          .type(MessageType.HYDRATE)
          .message("Hydrate process finish")
          .account(accountDto.getUsername())
          .igBusinessAccountId(hydrateMediaDto.getIgBusinessAccountId())
          .build());

      // Success response
      log.info("Posts for account {} where processed successfully!!", hydrateMediaDto.getIgBusinessAccountId());
      igResponseDto.setStatus(HttpStatus.OK.value());
      igResponseDto.setMessage(String.format("Posts for account %s where processed successfully!!", hydrateMediaDto.getIgBusinessAccountId()));

    } catch (Exception e) {
      log.error("Something went wrong when hydrating post database for account {} :(", hydrateMediaDto.getIgBusinessAccountId(), e);
      igResponseDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      igResponseDto.setMessage("Something went wrong when hydrating post database :(");
    }

    return igResponseDto;
  }

  private void processFeedAccountMedia(HydrateMediaDto igGetMediaDto, String graphApiToken, AccountDto accountDto) {
    log.info("Processing account: {}", accountDto.getUsername());

    Long igBusinessAccountId = Long.valueOf(igGetMediaDto.getIgBusinessAccountId());
    FieldsParamBuilder fields = FieldsParamBuilder.builder()
        .username(accountDto.getUsername())
        .limit(mediaCount)
        .build();

    MediaContentResponseDto feedAccountMedia = igFeignService.getMedia(
        String.valueOf(igGetMediaDto.getIgBusinessAccountId()),
        graphApiToken,
        fields.processFieldsParamValue().getFields());

    List<Post> allPosts = postRepository.findAll();
    List<Post> postList = new ArrayList<>();

    if (!CollectionUtils.isEmpty(allPosts)) {
      Map<Long, Post> postIds = allPosts.stream().collect(Collectors.toMap(Post::getMediaId, Function.identity()));
      postList = feedAccountMedia.getBusinessDiscovery().getMedia().getData().stream()
          .filter(data -> !postIds.containsKey(data.getId()) || data.getMedia_type().equals(MediaType.VIDEO.name()) )
          .filter(data -> Utils.isNotEmpty(data.getMedia_url()))
          .filter(data -> data.getMedia_type().equals(MediaType.IMAGE.name()) || data.getMedia_type().equals(MediaType.VIDEO.name()))
          .filter(data -> data.getTimestamp().toInstant().isAfter(ZonedDateTime.now().minusDays(longevity).toInstant()))
          .filter(data -> data.getLike_count() > 1000)
          .map(data -> Post.fromData(data, igBusinessAccountId, accountDto.getUsername(), isPosted(postIds, data.getId()), getCreatedTime(postIds, data.getId())))
          .collect(Collectors.toList());
    }

    postRepository.saveAll(postList);

    log.info("... {} post processed for account {}", postList.size(), accountDto.getUsername());
  }

  // Get the original posted status in case of video content type
  private boolean isPosted(Map<Long, Post> postsMap, Long postId) {
    return postsMap.containsKey(postId) && postsMap.get(postId).getMediaType()
        .equals(MediaType.VIDEO.name()) && postsMap.get(postId).isPosted();
  }

  // Get the original posted status in case of video content type
  private ZonedDateTime getCreatedTime(Map<Long, Post> postsMap, Long postId) {
    return postsMap.containsKey(postId) ? postsMap.get(postId).getCreated() : ZonedDateTime.now();
  }
}
