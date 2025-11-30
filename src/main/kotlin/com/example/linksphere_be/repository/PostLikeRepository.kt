package com.example.linksphere_be.repository

import com.example.linksphere_be.entity.PostLike
import com.example.linksphere_be.entity.PostLikeId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostLikeRepository : JpaRepository<PostLike, PostLikeId> {
    fun existsByIdUserIdAndIdPostId(userId: String, postId: String): Boolean
    fun deleteByIdUserIdAndIdPostId(userId: String, postId: String)
    fun countByIdPostId(postId: String): Long
}
