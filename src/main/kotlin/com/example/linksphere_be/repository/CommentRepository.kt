package com.example.linksphere_be.repository

import com.example.linksphere_be.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, String> {
    fun findByPostIdOrderByCreatedAtAsc(postId: String): List<Comment>
    fun findByUserId(userId: String): List<Comment>
}
