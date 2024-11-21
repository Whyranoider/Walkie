package com.whyranoid.data.model.challenge

data class BadgeResponse(
    val badgeId: Int,
    val badgeName: String,
    val failureImg: String? = null,
    val img: String
) {
    fun toBadge(): com.whyranoid.domain.model.challenge.Badge {
        return com.whyranoid.domain.model.challenge.Badge(
            id = badgeId.toLong(),
            name = badgeName,
            imageUrl = img,
            failureImageUrl = failureImg
        )
    }
}