package com.example.linksphere_be.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "comments")
data class Comment(
    @Id
    @Column(nullable = false)
    val id: String = generateId(),

    @Column(nullable = false, name = "user_id")
    val userId: String,

    @Column(nullable = false, name = "post_id")
    val postId: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    val user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    val post: Post? = null
) {
    companion object {
        private var counter = 0L
        fun generateId(): String = "comment_${System.currentTimeMillis()}_${counter++}"
    }
}
