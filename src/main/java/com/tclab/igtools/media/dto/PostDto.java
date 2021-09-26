package com.tclab.igtools.media.dto;

import com.tclab.igtools.account.dto.AccountDto;
import com.tclab.igtools.account.entity.Account;
import com.tclab.igtools.media.entity.Post;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDto {

  private String url;
  private String caption;
  private String igBussinesAccount;
  private String type;

  public static PostDto fromPost(Post post, String caption) {
    return PostDto.builder()
        .url(post.getMediaUrl())
        .caption(caption)
        .igBussinesAccount(String.valueOf(post.getIgBusinessAccountId()))
        .type(post.getMediaType())
        .build();
  }

}
