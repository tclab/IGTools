package com.tclab.igtools.media.repository;

import com.tclab.igtools.media.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

  @Query(value = "select * from post where posted = false order by like_count desc"
      , countQuery = "select count(*) from post where posted = false"
      , nativeQuery = true)
  List<Post> getUnpublishedPosts();

  Post findFirstByPostedAndIgBusinessAccountIdOrderByLikeCountDesc(boolean posted, Long igBusinessAccountId);

}
