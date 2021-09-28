package com.tclab.igtools.media.repository;

import com.tclab.igtools.media.entity.Post;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

  Post findFirstByPostedAndIgBusinessAccountIdOrderByLikeCountDesc(boolean posted, Long igBusinessAccountId);
  void deleteByTimestampBefore(ZonedDateTime days);

}
