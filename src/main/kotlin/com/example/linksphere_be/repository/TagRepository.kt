package com.example.linksphere_be.repository

import com.example.linksphere_be.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, String> {
    fun findByName(name: String): Tag?
}
