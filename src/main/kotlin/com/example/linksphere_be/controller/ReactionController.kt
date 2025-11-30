package com.example.linksphere_be.controller

import com.example.linksphere_be.dto.CreateReactionRequest
import com.example.linksphere_be.dto.ReactionResponse
import com.example.linksphere_be.service.ReactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reactions")
@CrossOrigin(origins = ["*"]) // Configure CORS as needed
class ReactionController(
    private val reactionService: ReactionService
) {

    @PostMapping
    fun toggleLike(
        @RequestBody request: CreateReactionRequest,
        // TODO: Uncomment when authentication is implemented
        // @AuthenticationPrincipal user: UserDetails
    ): ResponseEntity<ReactionResponse> {
        // TODO: Replace with actual user ID from authentication
        val userId = "temp-user-id"
        
        val response = reactionService.toggleLike(request, userId)
        return ResponseEntity.ok(response)
    }
}
