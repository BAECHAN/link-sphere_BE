package com.example.linksphere_be.api

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ReactionApiTest
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

        describe("Reaction API") {
            context("Toggle Like") {
                it("should toggle like status") {
                    val postId = testData!!.posts[1].id
                    val request =
                            com.example.linksphere_be.dto.CreateReactionRequest(postId = postId)

                    // First toggle: Like
                    mockMvc.perform(
                                    post("/api/reactions")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(request))
                            )
                            .andExpect(status().isOk)
                            .andExpect(jsonPath("$.isLiked").value(true))

                    // Second toggle: Unlike
                    mockMvc.perform(
                                    post("/api/reactions")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(objectMapper.writeValueAsString(request))
                            )
                            .andExpect(status().isOk)
                            .andExpect(jsonPath("$.isLiked").value(false))
                }
            }
        }
    }
}
