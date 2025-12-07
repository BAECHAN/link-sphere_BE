package com.example.linksphere_be.controller

import com.example.linksphere_be.dto.CreateReactionRequest
import com.example.linksphere_be.dto.ErrorResponse
import com.example.linksphere_be.dto.ReactionResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Reactions", description = "Reaction (Like) Management API")
interface ReactionApi {

        @Operation(summary = "Toggle like/reaction on a post")
        @ApiResponses(
                value =
                        [
                                ApiResponse(
                                        responseCode = "200",
                                        description = "Reaction toggled successfully"
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
        fun toggleLike(
                @RequestBody request: CreateReactionRequest
        ): ResponseEntity<ReactionResponse>
}
