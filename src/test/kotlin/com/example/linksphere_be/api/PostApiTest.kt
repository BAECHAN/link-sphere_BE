package com.example.linksphere_be.api

import com.example.linksphere_be.dto.CreatePostRequest
import com.example.linksphere_be.dto.UpdatePostRequest
import com.example.linksphere_be.repository.PostRepository
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
class PostApiTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val testDataLoader: TestDataLoader,
    private val postRepository: PostRepository
) : DescribeSpec() {

    override fun extensions() = listOf(SpringExtension)

    init {
        var testData: TestDataLoader.TestData? = null

        beforeSpec {
            testData = testDataLoader.loadData()
        }

        describe("Post API") {

            context("Create Post") {
                it("should create a new post") {
                    val request = CreatePostRequest(
                        url = "https://new-url.com",
                        title = "New Post",
                        description = "Description",
                        content = "Content",
                        tags = listOf("new", "tag")
                    )

                    mockMvc.perform(
                        post("/api/posts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                        .andExpect(status().isCreated)
                        .andExpect(jsonPath("$.url").value("https://new-url.com"))
                        .andExpect(jsonPath("$.title").value("New Post"))
                }
            }

            context("Get Posts") {
                it("should return list of posts") {
                    mockMvc.perform(get("/api/posts"))
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.posts").isArray)
                        .andExpect(jsonPath("$.totalElements").exists())
                }
            }

            context("Get Post By ID") {
                it("should return post details") {
                    val postId = testData!!.posts[0].id
                    mockMvc.perform(get("/api/posts/$postId"))
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").value(postId))
                }
            }

            context("Update Post") {
                it("should update post details") {
                    val postId = testData!!.posts[0].id
                    val request = UpdatePostRequest(
                        title = "Updated Title",
                        content = "Updated Content",
                        aiSummary = "Updated AI Summary"
                    )

                    mockMvc.perform(
                        patch("/api/posts/$postId")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.title").value("Updated Title"))
                        .andExpect(jsonPath("$.aiSummary").value("Updated AI Summary"))
                }
            }

            context("Delete Post") {
                it("should delete the post") {
                    // Create a dedicated post for deletion
                    val postToDelete = postRepository.save(
                        com.example.linksphere_be.entity.Post(
                            userId = testData!!.users[0].id,
                            url = "https://delete-me.com",
                            title = "Delete Me",
                            content = "To be deleted"
                        )
                    )
                    
                    val postId = postToDelete.id
                    println("Created post for deletion: $postId")
                    
                    // Verify it exists in Repo
                    val exists = postRepository.existsById(postId)
                    println("Post exists in repo before delete: $exists")
                    
                    mockMvc.perform(delete("/api/posts/$postId"))
                        .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                        .andExpect(status().isNoContent)

                    mockMvc.perform(get("/api/posts/$postId"))
                        .andExpect(status().isNotFound)
                }
            }
        }
    }
}
