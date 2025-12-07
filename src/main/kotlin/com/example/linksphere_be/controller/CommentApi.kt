package com.example.linksphere_be.controller

import com.example.linksphere_be.dto.CommentResponse
import com.example.linksphere_be.dto.CreateCommentRequest
import com.example.linksphere_be.dto.ErrorResponse
import com.example.linksphere_be.dto.UpdateCommentRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Comments", description = "Comment Management API")
interface CommentApi {

    @Operation(summary = "Create a new comment")
    @ApiResponses(
            value =
                    [
                            ApiResponse(
                                    responseCode = "201",
                                    description = "Comment created successfully"
                            ),
                            ApiResponse(
                                    responseCode = "400",
                                    description = "Invalid input",
                                    content =
                                            [
                                                    Content(
                                                            schema =
                                                                    Schema(
                                                                            implementation =
                                                                                    ErrorResponse::class
                                                                    )
                                                    )]
                            ),
                            ApiResponse(
                                    responseCode = "404",
                                    description = "Post or User not found",
                                    content =
                                            [
                                                    Content(
                                                            schema =
                                                                    Schema(
                                                                            implementation =
                                                                                    ErrorResponse::class
                                                                    )
                                                    )]
                            ),
                            ApiResponse(
                                    responseCode = "500",
                                    description = "Internal server error",
                                    content =
                                            [
                                                    Content(
                                                            schema =
                                                                    Schema(
                                                                            implementation =
                                                                                    ErrorResponse::class
                                                                    )
                                                    )]
                            )]
    )
    @PostMapping
    fun createComment(@RequestBody request: CreateCommentRequest): ResponseEntity<CommentResponse>

    @Operation(summary = "Update an existing comment")
    @ApiResponses(
            value =
                    [
                            ApiResponse(
                                    responseCode = "200",
                                    description = "Comment updated successfully"
                            ),
                            ApiResponse(
                                    responseCode = "400",
                                    description = "Invalid input",
                                    content =
                                            [
                                                    Content(
                                                            schema =
                                                                    Schema(
                                                                            implementation =
                                                                                    ErrorResponse::class
                                                                    )
                                                    )]
                            ),
                            ApiResponse(
                                    responseCode = "404",
                                    description = "Comment not found",
                                    content =
                                            [
                                                    Content(
                                                            schema =
                                                                    Schema(
                                                                            implementation =
                                                                                    ErrorResponse::class
                                                                    )
                                                    )]
                            ),
                            ApiResponse(
                                    responseCode = "500",
                                    description = "Internal server error",
                                    content =
                                            [
                                                    Content(
                                                            schema =
                                                                    Schema(
                                                                            implementation =
                                                                                    ErrorResponse::class
                                                                    )
                                                    )]
                            )]
    )
    @PatchMapping("/{id}")
    fun updateComment(
            @PathVariable id: String,
            @RequestBody request: UpdateCommentRequest
    ): ResponseEntity<CommentResponse>

    @Operation(summary = "Delete a comment")
    @ApiResponses(
            value =
                    [
                            ApiResponse(
                                    responseCode = "204",
                                    description = "Comment deleted successfully"
                            ),
                            ApiResponse(
                                    responseCode = "404",
                                    description = "Comment not found",
                                    content =
                                            [
                                                    Content(
                                                            schema =
                                                                    Schema(
                                                                            implementation =
                                                                                    ErrorResponse::class
                                                                    )
                                                    )]
                            ),
                            ApiResponse(
                                    responseCode = "500",
                                    description = "Internal server error",
                                    content =
                                            [
                                                    Content(
                                                            schema =
                                                                    Schema(
                                                                            implementation =
                                                                                    ErrorResponse::class
                                                                    )
                                                    )]
                            )]
    )
    @DeleteMapping("/{id}")
    fun deleteComment(@PathVariable id: String): ResponseEntity<Void>

    @Operation(summary = "Get comments by Post ID")
    @ApiResponses(
            value =
                    [
                            ApiResponse(
                                    responseCode = "200",
                                    description = "Comments retrieved successfully"
                            ),
                            ApiResponse(
                                    responseCode = "400",
                                    description = "Invalid Post ID",
                                    content =
                                            [
                                                    Content(
                                                            schema =
                                                                    Schema(
                                                                            implementation =
                                                                                    ErrorResponse::class
                                                                    )
                                                    )]
                            ),
                            ApiResponse(
                                    responseCode = "500",
                                    description = "Internal server error",
                                    content =
                                            [
                                                    Content(
                                                            schema =
                                                                    Schema(
                                                                            implementation =
                                                                                    ErrorResponse::class
                                                                    )
                                                    )]
                            )]
    )
    @GetMapping
    fun getComments(@RequestParam postId: String): ResponseEntity<List<CommentResponse>>
}
