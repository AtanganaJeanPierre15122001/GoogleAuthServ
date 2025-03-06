package com.example.di


import com.example.data.repositoryImpl.UserDataSourceImpl
import com.example.repository.UserDataSource
import com.example.repository.UserRepository
import com.example.util.Constants.DATABASE_NAME
import com.example.util.Constants.MONGO_URI
import org.koin.dsl.module
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.coroutine.coroutine


val koinModule = module {
    single{
        KMongo.createClient(MONGO_URI)
            .coroutine
            .getDatabase(DATABASE_NAME)
    }
    single<UserDataSource> {
        UserDataSourceImpl(get())
    }

    single { UserRepository(get()) }

}

