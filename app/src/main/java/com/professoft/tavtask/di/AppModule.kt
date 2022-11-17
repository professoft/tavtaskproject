package com.professoft.tavtask.di

import android.app.Application
import android.content.Context
import com.professoft.tavtask.data.realm.RealmDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }


    @Provides
    @Singleton
    internal fun provideRealmDatabase() : RealmDatabase {
        return RealmDatabase()
    }

}

