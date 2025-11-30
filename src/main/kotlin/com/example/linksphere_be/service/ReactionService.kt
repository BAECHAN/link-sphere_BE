package com.example.linksphere_be.service

import com.example.linksphere_be.dto.CreateReactionRequest
import com.example.linksphere_be.dto.ReactionResponse
import com.example.linksphere_be.entity.PostLike
import com.example.linksphere_be.entity.PostLikeId
import com.example.linksphere_be.repository.PostLikeRepository
import com.example.linksphere_be.repository.PostRepository
import com.example.linksphere_be.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReactionService(
    private val postLikeRepository: PostLikeRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun toggleLike(request: CreateReactionRequest, userId: String): ReactionResponse {
        val post = postRepository.findByIdOrNull(request.postId)
            ?: throw NoSuchElementException("Post not found with id: ${request.postId}")
        
        val isLiked = postLikeRepository.existsByIdUserIdAndIdPostId(userId, request.postId)
        
        if (isLiked) {
            postLikeRepository.deleteByIdUserIdAndIdPostId(userId, request.postId)
        } else {
            val user = userRepository.findByIdOrNull(userId)
                ?: throw NoSuchElementException("User not found with id: $userId")
            
            val postLike = PostLike(
                id = PostLikeId(userId, request.postId),
                user = user,
                post = post
            )
            postLikeRepository.save(postLike)
        }
        
        val likeCount = postLikeRepository.countByIdPostId(request.postId).toInt()
        
        return ReactionResponse(
            isLiked = !isLiked,
            likeCount = likeCount
        )
    }
}
