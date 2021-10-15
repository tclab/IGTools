package com.tclab.igtools.media.repository;

import com.tclab.igtools.media.entity.Post;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

  Post findFirstByPostedAndIgBusinessAccountIdOrderByLikeCountDesc(boolean posted, Long igBusinessAccountId);

  @Modifying
  @Transactional
  @Query("delete from Post p where p.created <= :days")
  void deleteByTimestampBefore(@Param("days") ZonedDateTime days);

}
