package com.example.linksphere_be.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(nullable = false)
    val id: String = generateId(),

    @Column(unique = true)
    val email: String? = null,

    @Column
    val password: String? = null,

    @Column(unique = true)
    val nickname: String? = null,

    @Column(name = "profile_image_url")
    val profileImageUrl: String? = null,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    // Relationships
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val posts: MutableList<Post> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val bookmarks: MutableList<Bookmark> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val postLikes: MutableList<PostLike> = mutableListOf()
) {
    companion object {
        private var counter = 0L
        fun generateId(): String = "user_${System.currentTimeMillis()}_${counter++}"
    }
}
