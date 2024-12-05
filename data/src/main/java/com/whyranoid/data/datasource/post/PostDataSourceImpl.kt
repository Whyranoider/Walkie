package com.whyranoid.data.datasource.post

import android.util.Log
import com.whyranoid.data.model.post.SendCommentRequest
import com.whyranoid.domain.datasource.PostDataSource
import com.whyranoid.domain.model.post.Comment
import com.whyranoid.domain.model.post.Post
import com.whyranoid.domain.model.post.PostPreview
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.Date

class PostDataSourceImpl(private val postService: PostService) : PostDataSource {
    // TODO: change to api call
    override suspend fun getPostPreviews(uid: Long): Result<List<PostPreview>> {
        return kotlin.runCatching {
            val posts = requireNotNull(postService.getPosts(uid).body())
            posts.map { it.toPostPreview() }
        }
    }

    override suspend fun getPostPreviews(
        uid: Long,
        year: Int,
        month: Int,
        day: Int,
    ): Result<List<PostPreview>> {
        return getPostPreviews(uid).onSuccess { posts ->
            posts.filter { postPreview ->
                val date = Date(postPreview.date)
                Log.d("TODOREMOVE", "${date.year}, ${date.month + 1}, ${date.date}")
                date.month + 1 == month && year == date.year && day == date.date
            }
        }
    }

    // TODO 서버 내려갔을 때 앱이 죽지 않도록 예외처리만 했음, 추후 수정 필요
    override suspend fun getMyPostPreviews(uid: Long): Result<List<PostPreview>> {
        val response = postService.myPosts(uid)
        response.body()?.map { it.toPostPreview() }
            ?: return Result.failure(Throwable(response.message().toString()))
        return Result.success(response.body()?.map { it.toPostPreview() }!!)
    }

    override suspend fun getMyPosts(uid: Long, myUid: Long): Result<List<Post>> {
        val response = postService.myPosts(uid)
        response.body() ?: return Result.failure(Throwable(response.message().toString()))
        return Result.success(response.body()?.map { it.toPost(myUid) }!!)
    }

    override suspend fun getMyPostPreviews(
        uid: Long,
        year: Int,
        month: Int,
        day: Int,
    ): Result<List<PostPreview>> {
        return getMyPostPreviews(uid).onSuccess { posts ->
            posts.filter { postPreview ->
                val date = Date(postPreview.date)
                date.month + 1 == month && year == date.year && day == date.date
            }
        }
    }

    // TODO: change to api call
    override suspend fun getPost(postId: Long): Result<Post> {
        return Result.success(Post.DUMMY)
    }

    override suspend fun uploadPost(
        uid: Long,
        content: String,
        colorMode: Int,
        history: String,
        imagePath: String,
    ): Result<String> {
        val uidBody = RequestBody.create(MediaType.parse("text/plain"), uid.toString())
        val contentBody = RequestBody.create(MediaType.parse("text/plain"), content)
        val colorModeBody =
            RequestBody.create(MediaType.parse("text/plain"), colorMode.toString())
        val historyBody = RequestBody.create(MediaType.parse("text/plain"), history)

        val file = File(imagePath)
        val fileBody = RequestBody.create(MediaType.parse("image/*"), file)
        val imagePart = MultipartBody.Part.createFormData("image", file.name, fileBody)

        return kotlin.runCatching {
            val uploadPostResponse =
                postService.uploadPost(
                    uidBody,
                    contentBody,
                    colorModeBody,
                    historyBody,
                    imagePart,
                )
                    .body()
            uploadPostResponse?.message.toString()
        }
    }

    override suspend fun getMyFollowingsPost(uid: Long): Result<List<Post>> {
        return kotlin.runCatching {
            val posts = requireNotNull(postService.getPosts(uid).body())
            posts.map { it.toPost(uid) }
        }
    }

    override suspend fun getEveryPost(uid: Long): Result<List<Post>> {
        return kotlin.runCatching {
            val posts = requireNotNull(postService.getEveryPosts(uid).body())
            posts.map { it.toPost(uid) }
        }
    }

    override suspend fun getComments(postId: Long): Result<List<Comment>> {
        return kotlin.runCatching {
            val comments = requireNotNull(postService.getComments(postId).body())
            comments.map {
                Comment(
                    it.postId,
                    it.commenterId,
                    it.date,
                    it.content,
                    it.commentId,
                    it.commenter.toUser(),
                )
            }
        }
    }

    override suspend fun sendComment(
        postId: Long,
        commenterId: Long,
        date: String,
        content: String,
    ): Result<Unit> {
        return kotlin.runCatching {
            val sendCommentRequest = SendCommentRequest(postId, commenterId, date, content)
            return if (postService.sendComment(sendCommentRequest).isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception("fail to send comment"),
                )
            }
        }
    }
}
