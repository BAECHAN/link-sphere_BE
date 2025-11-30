package com.example.linksphere_be.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.time.LocalDateTime

@Embeddable
data class PostTagId(
    @Column(name = "post_id")
    val postId: String = "",

    @Column(name = "tag_id")
    val tagId: String = ""
) : Serializable

@Entity
@Table(name = "post_tags")
data class PostTag(
    @EmbeddedId
    val id: PostTagId = PostTagId(),

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    val post: Post? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    val tag: Tag? = null
)
