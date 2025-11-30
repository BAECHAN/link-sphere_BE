package com.example.linksphere_be.dto

import java.time.LocalDateTime

// Request DTOs
data class CreateCommentRequest(
    val postId: String,
    val content: String
)

data class UpdateCommentRequest(
    val content: String
)

// Response DTOs
data class CommentResponse(
    val id: String,
    val postId: String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val author: AuthorInfo
)
