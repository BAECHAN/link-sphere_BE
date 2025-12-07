package com.example.linksphere_be.controller

import com.example.linksphere_be.dto.CommentResponse
import com.example.linksphere_be.dto.CreateCommentRequest
import com.example.linksphere_be.dto.UpdateCommentRequest
import com.example.linksphere_be.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = ["*"]) // Configure CORS as needed
class CommentController(private val commentService: CommentService) : CommentApi {

    override fun createComment(
            @RequestBody request: CreateCommentRequest
            // TODO: Uncomment when authentication is implemented
            // @AuthenticationPrincipal user: UserDetails
            ): ResponseEntity<CommentResponse> {
        // TODO: Replace with actual user ID from authentication
        val userId = "temp-user-id"

        val comment = commentService.createComment(request, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(comment)
    }

    override fun updateComment(
            @PathVariable id: String,
            @RequestBody request: UpdateCommentRequest
            // TODO: Uncomment when authentication is implemented
            // @AuthenticationPrincipal user: UserDetails
            ): ResponseEntity<CommentResponse> {
        // TODO: Replace with actual user ID from authentication
        val userId = "temp-user-id"

        val comment = commentService.updateComment(id, request, userId)
        return ResponseEntity.ok(comment)
    }

    override fun deleteComment(
            @PathVariable id: String
            // TODO: Uncomment when authentication is implemented
            // @AuthenticationPrincipal user: UserDetails
            ): ResponseEntity<Void> {
        // TODO: Replace with actual user ID from authentication
        val userId = "temp-user-id"

        commentService.deleteComment(id, userId)
        return ResponseEntity.noContent().build()
    }

    override fun getComments(@RequestParam postId: String): ResponseEntity<List<CommentResponse>> {
        val comments = commentService.getCommentsByPostId(postId)
        return ResponseEntity.ok(comments)
    }
}
