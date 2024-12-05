package com.whyranoid.walkie

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.whyranoid.data.API
import com.whyranoid.data.AccountDataStore
import com.whyranoid.data.AppDatabase
import com.whyranoid.data.datasource.challenge.ChallengeDataSourceImpl
import com.whyranoid.data.datasource.OtherUserPagingSource
import com.whyranoid.data.datasource.UserDataSourceImpl
import com.whyranoid.data.datasource.account.AccountDataSourceImpl
import com.whyranoid.data.datasource.account.AccountService
import com.whyranoid.data.datasource.challenge.ChallengeService
import com.whyranoid.data.datasource.community.CommunityDataSource
import com.whyranoid.data.datasource.community.CommunityDataSourceImpl
import com.whyranoid.data.datasource.community.CommunityService
import com.whyranoid.data.datasource.follow.FollowDataSourceImpl
import com.whyranoid.data.datasource.follow.FollowService
import com.whyranoid.data.datasource.post.PostDataSourceImpl
import com.whyranoid.data.datasource.post.PostService
import com.whyranoid.data.datasource.running.RunningControlDataSourceImpl
import com.whyranoid.data.datasource.running.RunningService
import com.whyranoid.data.repository.AccountRepositoryImpl
import com.whyranoid.data.repository.ChallengeRepositoryImpl
import com.whyranoid.data.repository.CommunityRepositoryImpl
import com.whyranoid.data.repository.FollowRepositoryImpl
import com.whyranoid.data.repository.GpsRepositoryImpl
import com.whyranoid.data.repository.NetworkRepositoryImpl
import com.whyranoid.data.repository.OtherUserRepositoryImpl
import com.whyranoid.data.repository.PostRepositoryImpl
import com.whyranoid.data.repository.RunningHistoryRepositoryImpl
import com.whyranoid.data.repository.RunningRepositoryImpl
import com.whyranoid.data.repository.UserRepositoryImpl
import com.whyranoid.domain.datasource.AccountDataSource
import com.whyranoid.domain.datasource.ChallengeDataSource
import com.whyranoid.domain.datasource.FollowDataSource
import com.whyranoid.domain.datasource.PostDataSource
import com.whyranoid.domain.datasource.RunningControlDataSource
import com.whyranoid.domain.datasource.UserDataSource
import com.whyranoid.domain.repository.AccountRepository
import com.whyranoid.domain.repository.ChallengeRepository
import com.whyranoid.domain.repository.CommunityRepository
import com.whyranoid.domain.repository.FollowRepository
import com.whyranoid.domain.repository.GpsRepository
import com.whyranoid.domain.repository.NetworkRepository
import com.whyranoid.domain.repository.OtherUserRepository
import com.whyranoid.domain.repository.PostRepository
import com.whyranoid.domain.repository.RunningHistoryRepository
import com.whyranoid.domain.repository.RunningRepository
import com.whyranoid.domain.repository.UserRepository
import com.whyranoid.domain.usecase.ChangeChallengeStatusUseCase
import com.whyranoid.domain.usecase.CompleteChallengeUseCase
import com.whyranoid.domain.usecase.GetChallengeDetailUseCase
import com.whyranoid.domain.usecase.GetChallengePreviewsByTypeUseCase
import com.whyranoid.domain.usecase.GetChallengingPreviewsUseCase
import com.whyranoid.domain.usecase.GetFollowerUseCase
import com.whyranoid.domain.usecase.GetFollowingUseCase
import com.whyranoid.domain.usecase.GetFollowingsPostsUseCase
import com.whyranoid.domain.usecase.GetMyFollowingUseCase
import com.whyranoid.domain.usecase.GetMyUidUseCase
import com.whyranoid.domain.usecase.GetNewChallengePreviewsUseCase
import com.whyranoid.domain.usecase.GetPostUseCase
import com.whyranoid.domain.usecase.GetTopRankChallengePreviewsUseCase
import com.whyranoid.domain.usecase.GetUserBadgesUseCase
import com.whyranoid.domain.usecase.GetUserDetailUseCase
import com.whyranoid.domain.usecase.GetUserPostPreviewsUseCase
import com.whyranoid.domain.usecase.GetUserPostsUseCase
import com.whyranoid.domain.usecase.GetUserUseCase
import com.whyranoid.domain.usecase.LikePostUseCase
import com.whyranoid.domain.usecase.RequestLoginUseCase
import com.whyranoid.domain.usecase.SignOutUseCase
import com.whyranoid.domain.usecase.StartChallengeUseCase
import com.whyranoid.domain.usecase.UploadPostUseCase
import com.whyranoid.domain.usecase.broadcast.AddGpsListener
import com.whyranoid.domain.usecase.broadcast.AddNetworkListener
import com.whyranoid.domain.usecase.broadcast.GetGpsState
import com.whyranoid.domain.usecase.broadcast.GetNetworkState
import com.whyranoid.domain.usecase.broadcast.RemoveGpsListener
import com.whyranoid.domain.usecase.broadcast.RemoveNetworkListener
import com.whyranoid.domain.usecase.community.FollowUseCase
import com.whyranoid.domain.usecase.community.GetSearchedUserUseCase
import com.whyranoid.domain.usecase.community.RemoveFollowerUseCase
import com.whyranoid.domain.usecase.community.SendCommentUseCase
import com.whyranoid.domain.usecase.community.UnFollowUseCase
import com.whyranoid.domain.usecase.running.GetRunningFollowerUseCase
import com.whyranoid.domain.usecase.running.RunningFinishUseCase
import com.whyranoid.domain.usecase.running.RunningStartUseCase
import com.whyranoid.domain.usecase.running.SendLikeUseCase
import com.whyranoid.presentation.screens.mypage.editprofile.EditProfileViewModel
import com.whyranoid.presentation.screens.mypage.following.FollowingViewModel
import com.whyranoid.presentation.screens.setting.SettingViewModel
import com.whyranoid.presentation.viewmodel.AddPostViewModel
import com.whyranoid.presentation.util.ApiResponseDialog
import com.whyranoid.presentation.viewmodel.CommunityScreenViewModel
import com.whyranoid.presentation.viewmodel.RunningEditViewModel
import com.whyranoid.presentation.viewmodel.RunningViewModel
import com.whyranoid.presentation.viewmodel.SearchFriendViewModel
import com.whyranoid.presentation.viewmodel.SelectHistoryViewModel
import com.whyranoid.presentation.viewmodel.SignInViewModel
import com.whyranoid.presentation.viewmodel.SplashViewModel
import com.whyranoid.presentation.viewmodel.TotalBadgeViewModel
import com.whyranoid.presentation.viewmodel.UserPageViewModel
import com.whyranoid.presentation.viewmodel.UserPostsViewModel
import com.whyranoid.presentation.viewmodel.challenge.ChallengeDetailViewModel
import com.whyranoid.presentation.viewmodel.challenge.ChallengeExitViewModel
import com.whyranoid.presentation.viewmodel.challenge.ChallengeMainViewModel
import com.whyranoid.walkie.walkiedialog.DialogViewModel
import com.whyranoid.walkie.walkiedialog.NetworkInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val viewModelModule =
    module {
        viewModel { ChallengeMainViewModel(get(), get(), get(), get(), get()) }
        viewModel { ChallengeDetailViewModel(get(), get(), get()) }
        viewModel { ChallengeExitViewModel(get(), get()) }
        viewModel {
            UserPageViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get()
            )
        }
        viewModel { RunningViewModel(get(), get(), get(), get(), get(), get()) }
        viewModel { RunningEditViewModel() }
        viewModel { SplashViewModel(get()) }
        viewModel { SignInViewModel(get()) }
        viewModel { SelectHistoryViewModel(get()) }
        viewModel { EditProfileViewModel(get()) }
        viewModel { AddPostViewModel(get()) }
        viewModel { SearchFriendViewModel(get(), get(), get()) }
        viewModel { DialogViewModel(get(), get(), get(), get(), get(), get()) }
        viewModel { CommunityScreenViewModel(get(), get(), get(), get(), get(), get(), get()) }
        viewModel { FollowingViewModel(get(), get(), get(), get(), get(), get()) }
        viewModel { SettingViewModel(get(), get()) }
        viewModel { TotalBadgeViewModel(get(), get()) }
        viewModel { UserPostsViewModel(get(), get()) }
    }

val repositoryModule =
    module {
        single<ChallengeRepository> { ChallengeRepositoryImpl(get()) }
        single<PostRepository> { PostRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
        single<RunningRepository> { RunningRepositoryImpl(get(), get()) }
        single<RunningHistoryRepository> { RunningHistoryRepositoryImpl(get(), get()) }
        single<AccountRepository> { AccountRepositoryImpl(get(), get()) }
        single<FollowRepository> { FollowRepositoryImpl(get()) }
        single<NetworkRepository> { NetworkRepositoryImpl(get()) }
        single<GpsRepository> { GpsRepositoryImpl(get()) }
        single<OtherUserRepository> { OtherUserRepositoryImpl(OtherUserPagingSource()) }
        single<CommunityRepository> { CommunityRepositoryImpl(get()) }
    }

val dataSourceModule =
    module {
        single<ChallengeDataSource> { ChallengeDataSourceImpl(get()) }
        single<PostDataSource> { PostDataSourceImpl(get()) }
        single<UserDataSource> { UserDataSourceImpl(get()) }
        single<AccountDataSource> { AccountDataSourceImpl(get(),get()) }
        single<FollowDataSource> { FollowDataSourceImpl(get()) }
        single<RunningControlDataSource> { RunningControlDataSourceImpl(get()) }
        single<CommunityDataSource> { CommunityDataSourceImpl(get()) }
    }

val useCaseModule =
    module {
        single { GetNewChallengePreviewsUseCase(get()) }
        single { GetChallengingPreviewsUseCase(get()) }
        single { GetChallengeDetailUseCase(get(), get()) }
        single { CompleteChallengeUseCase(get(), get())}
        single { GetChallengePreviewsByTypeUseCase(get(), get()) }
        single { GetTopRankChallengePreviewsUseCase(get()) }
        single { StartChallengeUseCase(get(), get()) }
        single { GetPostUseCase(get()) }
        single { GetUserPostPreviewsUseCase(get()) }
        single { GetUserBadgesUseCase(get()) }
        single { GetUserDetailUseCase(get(), get()) }
        single { GetRunningFollowerUseCase(get(), get()) }
        single { RunningFinishUseCase(get(), get()) }
        single { RunningStartUseCase(get(), get()) }
        single { SignOutUseCase(get()) }
        single { UploadPostUseCase(get(), get()) }
        single { SendLikeUseCase(get(), get()) }
        single { GetSearchedUserUseCase(get(), get()) }
        single { AddGpsListener(get()) }
        single { AddNetworkListener(get()) }
        single { RemoveGpsListener(get()) }
        single { RemoveNetworkListener(get()) }
        single { GetGpsState(get()) }
        single { GetNetworkState(get()) }
        single { FollowUseCase(get(), get()) }
        single { UnFollowUseCase(get(), get()) }
        single { GetFollowingUseCase(get()) }
        single { GetFollowerUseCase(get()) }
        single { GetFollowingsPostsUseCase(get(), get()) }
        single { LikePostUseCase(get(), get()) }
        single { RequestLoginUseCase(get()) }
        single { GetMyUidUseCase(get()) }
        single { RemoveFollowerUseCase(get(), get()) }
        single { GetMyFollowingUseCase(get(), get()) }
        single { SendCommentUseCase(get(), get()) }
        single { GetUserUseCase(get()) }
        single { ChangeChallengeStatusUseCase(get(), get()) }
        single { GetUserPostsUseCase(get(), get()) }
    }

val databaseModule =
    module {
        single {
            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                "walkie_database",
            ).build()
        }

        single {
            get<AppDatabase>().runningHistoryDao()
        }

        single { Gson() }

        single { AccountDataStore(get()) }
    }

val networkModule =
    module {
        class WalkieInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val newRequest = chain.request()
                return chain.proceed(newRequest)
            }
        }

        single {
            OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(WalkieInterceptor())
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    },
                ).addInterceptor(
                    NetworkInterceptor(
                        onRequest = { ApiResponseDialog.startLoading() },
                        onResponse = { ApiResponseDialog.finishLoad(it) },
                        excludedUrls = listOf(
                            Regex("/api/follow/(\\d+)/following"), // polling 방식 업데이트
                            Regex("/api/follow/(\\d+)/walking-followings"), // polling 방식 업데이트
                            Regex("/api/community/listup-post"), // 커뮤니티 탭, 자체 로딩바 있음
                            Regex("/api/community/upload-post") // 게시글 업로드, 자체 로딩바 있음
                        )
                    )
                )
                .build()
        }

        single {
            Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .client(get())
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder()
                            .setLenient()
                            .create(),
                    ),
                )
                .build()
        }

        single {
            get<Retrofit>().create(AccountService::class.java)
        }

        single { get<Retrofit>().create(FollowService::class.java) }

        single { get<Retrofit>().create(CommunityService::class.java) }

        single { get<Retrofit>().create(ChallengeService::class.java) }

        single { get<Retrofit>().create(PostService::class.java) }

        single { get<Retrofit>().create(RunningService::class.java) }

        single { get<Retrofit>().create(CommunityService::class.java) }
    }
