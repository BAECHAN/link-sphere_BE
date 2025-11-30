package com.example.linksphere_be.service

import com.example.linksphere_be.dto.AuthorInfo
import com.example.linksphere_be.dto.CommentResponse
import com.example.linksphere_be.dto.CreateCommentRequest
import com.example.linksphere_be.dto.UpdateCommentRequest
import com.example.linksphere_be.entity.Comment
import com.example.linksphere_be.repository.CommentRepository
import com.example.linksphere_be.repository.PostRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository
) {

    @Transactional
    fun createComment(request: CreateCommentRequest, userId: String): CommentResponse {
        // Verify post exists
        postRepository.findByIdOrNull(request.postId)
            ?: throw NoSuchElementException("Post not found with id: ${request.postId}")
        
        val comment = Comment(
            userId = userId,
            postId = request.postId,
            content = request.content
        )
        
        val savedComment = commentRepository.save(comment)
        return toCommentResponse(savedComment)
    }

    @Transactional
    fun updateComment(id: String, request: UpdateCommentRequest, userId: String): CommentResponse {
        val comment = commentRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Comment not found with id: $id")
        
        // TODO: Add authorization check when authentication is implemented
        // if (comment.userId != userId) throw UnauthorizedException("Not authorized to update this comment")
        
        comment.content = request.content
        val updatedComment = commentRepository.save(comment)
        
        return toCommentResponse(updatedComment)
    }

    @Transactional
    fun deleteComment(id: String, userId: String) {
        val comment = commentRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Comment not found with id: $id")
        
        // TODO: Add authorization check when authentication is implemented
        // if (comment.userId != userId) throw UnauthorizedException("Not authorized to delete this comment")
        
        commentRepository.delete(comment)
    }

    private fun toCommentResponse(comment: Comment): CommentResponse {
        val author = comment.user?.let {
            AuthorInfo(
                id = it.id,
                nickname = it.nickname,
                profileImageUrl = it.profileImageUrl
            )
        } ?: AuthorInfo(
            id = comment.userId,
            nickname = "Unknown",
            profileImageUrl = null
        )
        
        return CommentResponse(
            id = comment.id,
            postId = comment.postId,
            content = comment.content,
            createdAt = comment.createdAt,
            updatedAt = comment.updatedAt,
            author = author
        )
    }
}
