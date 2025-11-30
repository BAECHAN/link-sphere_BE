package com.example.linksphere_be.dto

// Request DTOs
data class CreateReactionRequest(
    val postId: String
)

// Response DTOs
data class ReactionResponse(
    val isLiked: Boolean,
    val likeCount: Int
)
