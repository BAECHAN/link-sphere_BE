package com.example.linksphere_be.api

import com.example.linksphere_be.dto.CreateCommentRequest
import com.example.linksphere_be.dto.UpdateCommentRequest
import com.example.linksphere_be.util.TestDataLoader
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CommentApiTest
@Autowired
constructor(
        private val mockMvc: MockMvc,
        private val objectMapper: ObjectMapper,
        private val testDataLoader: TestDataLoader
) : DescribeSpec() {

    override fun extensions() = listOf(SpringExtension)

    init {
        var testData: TestDataLoader.TestData? = null

        beforeSpec { testData = testDataLoader.loadData() }

        describe("Comment API") {
            context("Create Comment") {
                it("should create a new comment") {
                    // val userId = "temp-user-id" // Using temp user (handled by controller)
                    val post = testData!!.posts[0]
                    val request = CreateCommentRequest(postId = post.id, content = "New Comment")

                    mockMvc.perform(
                                    post("/api/comments")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(request))
                            )
                            .andExpect(status().isCreated)
                            .andExpect(jsonPath("$.content").value("New Comment"))
                }
            }

            context("Get Comments by Post ID") {
                it("should return list of comments") {
                    val post = testData!!.posts[0]
                    mockMvc.perform(get("/api/comments").param("postId", post.id))
                            .andExpect(status().isOk)
                            .andExpect(jsonPath("$").isArray)
                }
            }

            context("Update Comment") {
                it("should update comment content") {
                    val comment = testData!!.comments[0]
                    val request = UpdateCommentRequest(content = "Updated Comment")

                    mockMvc.perform(
                                    patch("/api/comments/${comment.id}")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(request))
                            )
                            .andExpect(status().isOk)
                            .andExpect(jsonPath("$.content").value("Updated Comment"))
                }
            }

            context("Delete Comment") {
                it("should delete the comment") {
                    val comment = testData!!.comments[1]
                    mockMvc.perform(delete("/api/comments/${comment.id}"))
                            .andExpect(status().isNoContent)
                }
            }
        }
    }
}
