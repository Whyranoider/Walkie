package com.whyranoid.domain.model.challenge

data class Badge(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val failureImageUrl: String? = null,
) {
}
