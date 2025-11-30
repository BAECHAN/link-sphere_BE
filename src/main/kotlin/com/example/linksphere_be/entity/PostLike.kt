package com.example.linksphere_be.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.time.LocalDateTime

@Embeddable
data class PostLikeId(
    @Column(name = "user_id")
    val userId: String = "",

    @Column(name = "post_id")
    val postId: String = ""
) : Serializable

@Entity
@Table(name = "post_likes")
data class PostLike(
    @EmbeddedId
    val id: PostLikeId = PostLikeId(),

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    val post: Post? = null
)
