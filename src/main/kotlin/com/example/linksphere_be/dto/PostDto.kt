package com.example.linksphere_be.dto

import java.time.LocalDateTime

// Request DTOs
data class CreatePostRequest(
    val url: String,
    val title: String? = null,
    val description: String? = null,
    val content: String? = null,
    val tags: List<String> = emptyList()
)

data class UpdatePostRequest(
    val title: String? = null,
    val description: String? = null,
    val content: String? = null,
    val tags: List<String>? = null
)

// Response DTOs
data class PostResponse(
    val id: String,
    val url: String,
    val title: String?,
    val description: String?,
    val ogImage: String?,
    val aiSummary: String?,
    val content: String?,
    val viewCount: Int,
    val createdAt: LocalDateTime,
    val author: AuthorInfo,
    val tags: List<String>,
    val likeCount: Int,
    val bookmarkCount: Int,
    val commentCount: Int,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false
)

data class AuthorInfo(
    val id: String,
    val nickname: String?,
    val profileImageUrl: String?
)

data class PostListResponse(
    val posts: List<PostResponse>,
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int,
    val pageSize: Int
)
