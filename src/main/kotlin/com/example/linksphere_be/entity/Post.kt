package com.example.linksphere_be.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
data class Post(
    @Id
    @Column(nullable = false)
    val id: String = generateId(),

    @Column(nullable = false, name = "user_id")
    val userId: String,

    // Link fields (URL and metadata)
    @Column(nullable = false)
    val url: String,

    @Column
    val title: String? = null,

    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "og_image")
    val ogImage: String? = null,

    @Column(name = "ai_summary", columnDefinition = "TEXT")
    val aiSummary: String? = null,

    // User's comment
    @Column(columnDefinition = "TEXT")
    val content: String? = null,

    @Column(nullable = false)
    val viewCount: Int = 0,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    val user: User? = null,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val bookmarks: MutableList<Bookmark> = mutableListOf(),

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val postLikes: MutableList<PostLike> = mutableListOf(),

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val postTags: MutableList<PostTag> = mutableListOf()
) {
    companion object {
        private var counter = 0L
        fun generateId(): String = "post_${System.currentTimeMillis()}_${counter++}"
    }
}
