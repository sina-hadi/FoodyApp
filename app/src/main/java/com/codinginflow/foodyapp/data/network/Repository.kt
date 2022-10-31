package com.codinginflow.foodyapp.data.network

import com.codinginflow.foodyapp.data.LocalDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource
) {

    val remote = remoteDataSource

    val local = localDataSource

}