package com.whyranoid.presentation.theme

import androidx.compose.ui.graphics.Color
import com.whyranoid.domain.model.challenge.ChallengeType

// TODO: secondary / Tertiary
object WalkieColor {
    val Primary = Color(0xFFFB8947)
    val Secondary = Color(0xFFE75300)
    val Tertiary = Color(0xFFFAC03A)
    val PrimarySurface = Color(0xFFFEE9DC)
    val GrayBackground = Color(0xFFF8F8F8)
    val GrayDisable = Color(0xFFECECEC)
    val GrayDefault = Color(0xFFD9D9D9)
    val GrayBorder = Color(0xFFB9BBB8)
    val GrayStatusBar = Color(0xFFB4B4B4)
    val GrayBlack = Color(0xFF333333)
}

object SystemColor {
    val Error = Color(0xFFFF3257)
    val Positive = Color(0xFF414EF5)
    val Negative = Color(0xFF999999)
}


object ChallengeColor {

    interface ChallengeColorInterface {
        val backgroundColor: Color
        val progressBarColor: Color
        val progressBackgroundColor: Color
    }

    val Life = object : ChallengeColorInterface {
        override val backgroundColor = Color(0xFFEBF5FF)
        override val progressBarColor = Color(0xFF50ABFF)
        override val progressBackgroundColor = Color(0xFFB8DDFF)
    }

    val Calorie = object : ChallengeColorInterface {
        override val backgroundColor = Color(0xFFE5FBF8)
        override val progressBarColor = Color(0xFF49E0CE)
        override val progressBackgroundColor = Color(0xFF9FEFE5)
    }

    val Distance = object : ChallengeColorInterface {
        override val backgroundColor = Color(0xFFE6FCDE)
        override val progressBarColor = Color(0xFF7CF053)
        override val progressBackgroundColor = Color(0xFFC2F8AF)
    }

    fun ChallengeType.getColor() : ChallengeColorInterface {
        return when(this) {
            ChallengeType.LIFE -> Life
            ChallengeType.CALORIE -> Calorie
            ChallengeType.DISTANCE -> Distance
        }
    }
}
