package com.example.linksphere_be.entity

import jakarta.persistence.*

@Entity
@Table(name = "tags")
data class Tag(
    @Id
    @Column(nullable = false)
    val id: String = generateId(),

    @Column(unique = true, nullable = false)
    val name: String,

    // Relationships
    @OneToMany(mappedBy = "tag", cascade = [CascadeType.ALL], orphanRemoval = true)
    val postTags: MutableList<PostTag> = mutableListOf()
) {
    companion object {
        private var counter = 0L
        fun generateId(): String = "tag_${System.currentTimeMillis()}_${counter++}"
    }
}
