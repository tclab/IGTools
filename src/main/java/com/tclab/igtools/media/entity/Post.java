package com.tclab.igtools.media.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tclab.igtools.commons.igclient.dto.MediaContentResponseDto;
import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "POST")
public class Post {

  @Id
  private Long mediaId;

  @Column
  private Long igBusinessAccountId;

  @Column
  private String username;

  @Column
  private String mediaType;

  @Column
  private Long commentsCount;

  @Column
  private Long likeCount;

  @Column(length = 1000)
  private String mediaUrl;

  @Column
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") // example: 2021-06-30T14:39:17+0000 (ISO 8601)
  private ZonedDateTime timestamp;

  @Column
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") // example: 2021-06-30T14:39:17+0000 (ISO 8601)
  private ZonedDateTime created;

  @Column
  private boolean posted;

  public static Post fromData(MediaContentResponseDto.Data data, Long igBusinessAccountId, String username, boolean posted, ZonedDateTime created) {
    return Post.builder()
        .mediaId(data.getId())
        .igBusinessAccountId(igBusinessAccountId)
        .username(username)
        .mediaType(data.getMedia_type())
        .commentsCount(data.getComments_count())
        .likeCount(data.getLike_count())
        .mediaUrl(data.getMedia_url())
        .timestamp(data.getTimestamp())
        .posted(posted)
        .created(created)
        .build();
  }

}


