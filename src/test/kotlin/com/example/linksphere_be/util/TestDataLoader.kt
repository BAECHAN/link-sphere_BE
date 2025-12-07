package com.example.linksphere_be.util

import com.example.linksphere_be.entity.Bookmark
import com.example.linksphere_be.entity.BookmarkId
import com.example.linksphere_be.entity.Comment
import com.example.linksphere_be.entity.Post
import com.example.linksphere_be.entity.PostLike
import com.example.linksphere_be.entity.PostLikeId
import com.example.linksphere_be.entity.User
import com.example.linksphere_be.repository.BookmarkRepository
import com.example.linksphere_be.repository.CommentRepository
import com.example.linksphere_be.repository.PostLikeRepository
import com.example.linksphere_be.repository.PostRepository
import com.example.linksphere_be.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Component
class TestDataLoader(
        private val userRepository: UserRepository,
        private val postRepository: PostRepository,
        private val commentRepository: CommentRepository,
        private val bookmarkRepository: BookmarkRepository,
        private val postLikeRepository: PostLikeRepository
) {

        data class TestData(
                val users: List<User>,
                val posts: List<Post>,
                val comments: List<Comment>
        )

        @Transactional
        fun loadData(): TestData {
                // Clean up existing data to prevent duplicates (Dependency Order)
                // postLikeRepository.deleteAll()
                // bookmarkRepository.deleteAll()
                // commentRepository.deleteAll()
                // postRepository.deleteAll()
                // userRepository.deleteAll()

                // Create Users
                val tempUser =
                        userRepository.save(
                                User(
                                        id = "temp-user-id",
                                        email = "temp@example.com",
                                        password = "password",
                                        nickname = "TempUser",
                                        profileImageUrl = "https://example.com/temp.jpg"
                                )
                        )

                val user1 =
                        userRepository.save(
                                User(
                                        email = "user1@example.com",
                                        password = "password", // In real app should be encoded
                                        nickname = "UserOne",
                                        profileImageUrl = "https://example.com/user1.jpg"
                                )
                        )

                val user2 =
                        userRepository.save(
                                User(
                                        email = "user2@example.com",
                                        password = "password",
                                        nickname = "UserTwo",
                                        profileImageUrl = "https://example.com/user2.jpg"
                                )
                        )

                // Create Posts
                val post1 =
                        postRepository.save(
                                Post(
                                        user = user1,
                                        userId = user1.id,
                                        url = "https://example.com/post1",
                                        title = "First Post",
                                        description = "This is the first post",
                                        content = "Content of the first post"
                                )
                        )

                val post2 =
                        postRepository.save(
                                Post(
                                        user = user2,
                                        userId = user2.id,
                                        url = "https://example.com/post2",
                                        title = "Second Post",
                                        description = "This is the second post",
                                        content = "Content of the second post"
                                )
                        )

                // Create Comments
                val comment1 =
                        commentRepository.save(
                                Comment(
                                        postId = post1.id,
                                        userId = user2.id,
                                        content = "Nice post!"
                                )
                        )

                val comment2 =
                        commentRepository.save(
                                Comment(postId = post1.id, userId = user1.id, content = "Thanks!")
                        )

                // Create Likes and Bookmarks
                postLikeRepository.save(
                        PostLike(id = PostLikeId(user2.id, post1.id), user = user2, post = post1)
                )

                bookmarkRepository.save(
                        Bookmark(id = BookmarkId(user1.id, post2.id), user = user1, post = post2)
                )

                return TestData(
                        users = listOf(user1, user2, tempUser),
                        posts = listOf(post1, post2),
                        comments = listOf(comment1, comment2)
                )
        }
}
