package com.whyranoid.domain.usecase

import com.whyranoid.domain.model.post.Post
import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.PostRepository
import kotlinx.coroutines.flow.first

class GetFollowingsPostsUseCase(
    private val accountRepository: AccountRepository,
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(isEveryPost: Boolean): Result<List<Post>> {
        val myUid = requireNotNull(accountRepository.walkieId.first())
        return if (isEveryPost) postRepository.getEveryPost(myUid) else postRepository.getMyFollowingsPost(myUid)
    }
}