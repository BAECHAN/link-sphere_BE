package com.example.linksphere_be.repository

import com.example.linksphere_be.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, String> {
    
    // Search posts by title, description, or content
    @Query("""
        SELECT DISTINCT p FROM Post p
        LEFT JOIN p.postTags pt
        LEFT JOIN pt.tag t
        WHERE (:search IS NULL OR 
               p.title ILIKE :search OR
               p.description ILIKE :search OR
               p.content ILIKE :search)
        AND (:tag IS NULL OR t.name = :tag)
    """)
    fun findBySearchAndTag(
        @Param("search") search: String?,
        @Param("tag") tag: String?,
        pageable: Pageable
    ): Page<Post>
    
    // Find posts by user
    fun findByUserId(userId: String, pageable: Pageable): Page<Post>
    
    // Find bookmarked posts by user
    @Query("""
        SELECT p FROM Post p
        INNER JOIN p.bookmarks b
        WHERE b.id.userId = :userId
        ORDER BY b.createdAt DESC
    """)
    fun findBookmarkedByUserId(@Param("userId") userId: String, pageable: Pageable): Page<Post>
}
