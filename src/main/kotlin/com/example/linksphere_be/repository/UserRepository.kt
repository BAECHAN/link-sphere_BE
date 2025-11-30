package com.example.linksphere_be.repository

import com.example.linksphere_be.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun findByEmail(email: String): User?
    fun findByNickname(nickname: String): User?
}
