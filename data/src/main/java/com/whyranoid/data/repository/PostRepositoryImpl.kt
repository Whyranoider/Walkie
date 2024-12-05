package com.whyranoid.data.repository

import com.whyranoid.domain.datasource.PostDataSource
import com.whyranoid.domain.model.post.Comment
import com.whyranoid.domain.model.post.Post
import com.whyranoid.domain.model.post.PostPreview
import com.whyranoid.domain.repository.PostRepository

class PostRepositoryImpl(
    private val postDataSource: PostDataSource,
) : PostRepository {
    override suspend fun getUserPostPreviews(uid: Long): Result<List<PostPreview>> {
        return postDataSource.getPostPreviews(uid)
    }

    override suspend fun getUserPostPreviews(
        uid: Long,
        year: Int,
        month: Int,
        day: Int,
    ): Result<List<PostPreview>> {
        return postDataSource.getPostPreviews(uid, year, month, day)
    }

    override suspend fun getMyPostPreviews(uid: Long): Result<List<PostPreview>> {
        return postDataSource.getMyPostPreviews(uid)
    }

    override suspend fun getMyPostPreviews(
        uid: Long,
        year: Int,
        month: Int,
        day: Int,
    ): Result<List<PostPreview>> {
        return postDataSource.getMyPostPreviews(uid, year, month, day)
    }

    override suspend fun getMyPosts(uid: Long, myUid: Long): Result<List<Post>> {
        return postDataSource.getMyPosts(uid, myUid)
    }

    override suspend fun getPost(postId: Long): Result<Post> {
        return postDataSource.getPost(postId)
    }

    override suspend fun uploadPost(
        uid: Long,
        content: String,
        colorMode: Int,
        history: String,
        imagePath: String,
    ): Result<String> {
        return postDataSource.uploadPost(uid, content, colorMode, history, imagePath)
    }

    override suspend fun getMyFollowingsPost(uid: Long): Result<List<Post>> {
        return postDataSource.getMyFollowingsPost(uid)
    }

    override suspend fun getEveryPost(uid: Long): Result<List<Post>> {
        return postDataSource.getEveryPost(uid)
    }

    override suspend fun getComments(postId: Long): Result<List<Comment>> {
        return postDataSource.getComments(postId)
    }

    override suspend fun sendComment(
        postId: Long,
        commenterId: Long,
        date: String,
        content: String,
    ): Result<Unit> {
        return postDataSource.sendComment(postId, commenterId, date, content)
    }
}
