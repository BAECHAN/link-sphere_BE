package com.example.linksphere_be.controller

import com.example.linksphere_be.dto.CreatePostRequest
import com.example.linksphere_be.dto.ErrorResponse
import com.example.linksphere_be.dto.PostListResponse
import com.example.linksphere_be.dto.PostResponse
import com.example.linksphere_be.dto.UpdatePostRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Posts", description = "Post Management API")
interface PostApi {

    @Operation(summary = "Create a new post")
    @ApiResponses(
            value =
                    [
                            ApiResponse(
                                    responseCode = "201",
                                    description = "Post created successfully"
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
    fun createPost(@RequestBody request: CreatePostRequest): ResponseEntity<PostResponse>

    @Operation(summary = "Get all posts")
    @ApiResponses(
            value =
                    [
                            ApiResponse(
                                    responseCode = "200",
                                    description = "Posts retrieved successfully"
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
    fun getPosts(
            @RequestParam(required = false) search: String?,
            @RequestParam(required = false) tag: String?,
            @RequestParam(defaultValue = "createdAt") sort: String,
            @RequestParam(defaultValue = "0") page: Int,
            @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PostListResponse>

    @Operation(summary = "Get post by ID")
    @ApiResponses(
            value =
                    [
                            ApiResponse(
                                    responseCode = "200",
                                    description = "Post retrieved successfully"
                            ),
                            ApiResponse(
                                    responseCode = "404",
                                    description = "Post not found",
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
    @GetMapping("/{id}")
    fun getPostById(@PathVariable id: String): ResponseEntity<PostResponse>

    @Operation(summary = "Update a post")
    @ApiResponses(
            value =
                    [
                            ApiResponse(
                                    responseCode = "200",
                                    description = "Post updated successfully"
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
                                    description = "Post not found",
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
    fun updatePost(
            @PathVariable id: String,
            @RequestBody request: UpdatePostRequest
    ): ResponseEntity<PostResponse>

    @Operation(summary = "Delete a post")
    @ApiResponses(
            value =
                    [
                            ApiResponse(
                                    responseCode = "204",
                                    description = "Post deleted successfully"
                            ),
                            ApiResponse(
                                    responseCode = "404",
                                    description = "Post not found",
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
    fun deletePost(@PathVariable id: String): ResponseEntity<Void>

    @Operation(summary = "Toggle bookmark on a post")
    @ApiResponses(
            value =
                    [
                            ApiResponse(
                                    responseCode = "200",
                                    description = "Bookmark toggled successfully"
                            ),
                            ApiResponse(
                                    responseCode = "404",
                                    description = "Post not found",
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
    @PostMapping("/{id}/bookmark")
    fun toggleBookmark(@PathVariable id: String): ResponseEntity<Map<String, Boolean>>
}
