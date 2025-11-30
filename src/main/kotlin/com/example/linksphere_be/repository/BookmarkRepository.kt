package com.example.linksphere_be.repository

import com.example.linksphere_be.entity.Bookmark
import com.example.linksphere_be.entity.BookmarkId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookmarkRepository : JpaRepository<Bookmark, BookmarkId> {
    fun existsByIdUserIdAndIdPostId(userId: String, postId: String): Boolean
    fun deleteByIdUserIdAndIdPostId(userId: String, postId: String)
    fun countByIdPostId(postId: String): Long
}
