package com.example.linksphere_be.service

import com.example.linksphere_be.dto.*
import com.example.linksphere_be.entity.*
import com.example.linksphere_be.repository.*
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val tagRepository: TagRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val postLikeRepository: PostLikeRepository
) {

    @Transactional
    fun createPost(request: CreatePostRequest, userId: String): PostResponse {
        // Create post
        val post = Post(
            userId = userId,
            url = request.url,
            title = request.title,
            description = request.description,
            content = request.content
        )
        
        val savedPost = postRepository.save(post)
        
        // Handle tags
        if (request.tags.isNotEmpty()) {
            request.tags.forEach { tagName ->
                val tag = tagRepository.findByName(tagName) ?: tagRepository.save(
                    Tag(name = tagName)
                )
                
                val postTag = PostTag(
                    id = PostTagId(savedPost.id, tag.id),
                    post = savedPost,
                    tag = tag
                )
                savedPost.postTags.add(postTag)
            }
            postRepository.save(savedPost)
        }
        
        return toPostResponse(savedPost, userId)
    }

    fun getPosts(
        search: String?,
        tag: String?,
        sort: String = "createdAt",
        page: Int = 0,
        size: Int = 20,
        userId: String? = null
    ): PostListResponse {
        val sortOrder = when (sort) {
            "viewCount" -> Sort.by(Sort.Direction.DESC, "viewCount")
            "createdAt" -> Sort.by(Sort.Direction.DESC, "createdAt")
            else -> Sort.by(Sort.Direction.DESC, "createdAt")
        }
        
        val pageable = PageRequest.of(page, size, sortOrder)
        val searchPattern = search?.let { "%$it%" }
        val postsPage = postRepository.findBySearchAndTag(searchPattern, tag, pageable)
        
        val posts = postsPage.content.map { toPostResponse(it, userId) }
        
        return PostListResponse(
            posts = posts,
            totalElements = postsPage.totalElements,
            totalPages = postsPage.totalPages,
            currentPage = page,
            pageSize = size
        )
    }

    @Transactional
    fun getPostById(id: String, userId: String?): PostResponse {
        val post = postRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Post not found with id: $id")
        
        // Increment view count
        val updatedPost = post.copy(viewCount = post.viewCount + 1)
        postRepository.save(updatedPost)
        
        return toPostResponse(updatedPost, userId)
    }

    @Transactional
    fun updatePost(id: String, request: UpdatePostRequest, userId: String): PostResponse {
        val post = postRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Post not found with id: $id")
        
        // TODO: Add authorization check when authentication is implemented
        // if (post.userId != userId) throw UnauthorizedException("Not authorized to update this post")
        
        val updatedPost = post.copy(
            title = request.title ?: post.title,
            description = request.description ?: post.description,
            content = request.content ?: post.content
        )
        
        // Handle tags update
        if (request.tags != null) {
            // Clear existing tags
            updatedPost.postTags.clear()
            
            // Add new tags
            request.tags.forEach { tagName ->
                val tag = tagRepository.findByName(tagName) ?: tagRepository.save(
                    Tag(name = tagName)
                )
                
                val postTag = PostTag(
                    id = PostTagId(updatedPost.id, tag.id),
                    post = updatedPost,
                    tag = tag
                )
                updatedPost.postTags.add(postTag)
            }
        }
        
        val savedPost = postRepository.save(updatedPost)
        return toPostResponse(savedPost, userId)
    }

    @Transactional
    fun deletePost(id: String, userId: String) {
        val post = postRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Post not found with id: $id")
        
        // TODO: Add authorization check when authentication is implemented
        // if (post.userId != userId) throw UnauthorizedException("Not authorized to delete this post")
        
        postRepository.delete(post)
    }

    @Transactional
    fun toggleBookmark(postId: String, userId: String): Boolean {
        val post = postRepository.findByIdOrNull(postId)
            ?: throw NoSuchElementException("Post not found with id: $postId")
        
        val isBookmarked = bookmarkRepository.existsByIdUserIdAndIdPostId(userId, postId)
        
        if (isBookmarked) {
            bookmarkRepository.deleteByIdUserIdAndIdPostId(userId, postId)
            return false
        } else {
            val user = userRepository.findByIdOrNull(userId)
                ?: throw NoSuchElementException("User not found with id: $userId")
            
            val bookmark = Bookmark(
                id = BookmarkId(userId, postId),
                user = user,
                post = post
            )
            bookmarkRepository.save(bookmark)
            return true
        }
    }

    private fun toPostResponse(post: Post, currentUserId: String?): PostResponse {
        val author = post.user?.let {
            AuthorInfo(
                id = it.id,
                nickname = it.nickname,
                profileImageUrl = it.profileImageUrl
            )
        } ?: AuthorInfo(
            id = post.userId,
            nickname = "Unknown",
            profileImageUrl = null
        )
        
        val tags = post.postTags.mapNotNull { it.tag?.name }
        val likeCount = postLikeRepository.countByIdPostId(post.id).toInt()
        val bookmarkCount = bookmarkRepository.countByIdPostId(post.id).toInt()
        val commentCount = post.comments.size
        
        val isLiked = currentUserId?.let {
            postLikeRepository.existsByIdUserIdAndIdPostId(it, post.id)
        } ?: false
        
        val isBookmarked = currentUserId?.let {
            bookmarkRepository.existsByIdUserIdAndIdPostId(it, post.id)
        } ?: false
        
        return PostResponse(
            id = post.id,
            url = post.url,
            title = post.title,
            description = post.description,
            ogImage = post.ogImage,
            aiSummary = post.aiSummary,
            content = post.content,
            viewCount = post.viewCount,
            createdAt = post.createdAt,
            author = author,
            tags = tags,
            likeCount = likeCount,
            bookmarkCount = bookmarkCount,
            commentCount = commentCount,
            isLiked = isLiked,
            isBookmarked = isBookmarked
        )
    }
}
