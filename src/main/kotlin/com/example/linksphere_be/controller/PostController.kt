package com.example.linksphere_be.controller

import com.example.linksphere_be.dto.CreatePostRequest
import com.example.linksphere_be.dto.PostListResponse
import com.example.linksphere_be.dto.PostResponse
import com.example.linksphere_be.dto.UpdatePostRequest
import com.example.linksphere_be.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = ["*"]) // Configure CORS as needed
class PostController(
    private val postService: PostService
) {

    @PostMapping
    fun createPost(
        @RequestBody request: CreatePostRequest,
        // TODO: Uncomment when authentication is implemented
        // @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<PostResponse> {
        // TODO: Replace with actual user ID from authentication
        val userId = "temp-user-id"
        
        val post = postService.createPost(request, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(post)
    }

    @GetMapping
    fun getPosts(
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) tag: String?,
        @RequestParam(defaultValue = "createdAt") sort: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        // TODO: Uncomment when authentication is implemented
        // @AuthenticationPrincipal user: UserDetails?
    ): ResponseEntity<PostListResponse> {
        // TODO: Replace with actual user ID from authentication (can be null for guests)
        val userId: String? = null
        
        val posts = postService.getPosts(search, tag, sort, page, size, userId)
        return ResponseEntity.ok(posts)
    }

    @GetMapping("/{id}")
    fun getPostById(
        @PathVariable id: String,
        // TODO: Uncomment when authentication is implemented
        // @AuthenticationPrincipal user: UserDetails?
    ): ResponseEntity<PostResponse> {
        // TODO: Replace with actual user ID from authentication (can be null for guests)
        val userId: String? = null
        
        val post = postService.getPostById(id, userId)
        return ResponseEntity.ok(post)
    }

    @PatchMapping("/{id}")
    fun updatePost(
        @PathVariable id: String,
        @RequestBody request: UpdatePostRequest,
        // TODO: Uncomment when authentication is implemented
        // @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<PostResponse> {
        // TODO: Replace with actual user ID from authentication
        val userId = "temp-user-id"
        
        val post = postService.updatePost(id, request, userId)
        return ResponseEntity.ok(post)
    }

    @DeleteMapping("/{id}")
    fun deletePost(
        @PathVariable id: String,
        // TODO: Uncomment when authentication is implemented
        // @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<Void> {
        // TODO: Replace with actual user ID from authentication
        val userId = "temp-user-id"
        
        postService.deletePost(id, userId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{id}/bookmark")
    fun toggleBookmark(
        @PathVariable id: String,
        // TODO: Uncomment when authentication is implemented
        // @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<Map<String, Boolean>> {
        // TODO: Replace with actual user ID from authentication
        val userId = "temp-user-id"
        
        val isBookmarked = postService.toggleBookmark(id, userId)
        return ResponseEntity.ok(mapOf("isBookmarked" to isBookmarked))
    }
}
