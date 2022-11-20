package com.professoft.tavtask.di

import android.app.Application
import android.content.Context
import com.professoft.tavtask.data.datastore.DataStoreManager
import com.professoft.tavtask.data.datastore.DataStoreRepo
import com.professoft.tavtask.data.realm.RealmDatabase
import com.professoft.tavtask.utils.UtilityClass
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    internal fun provideRealmDatabase(): RealmDatabase {
        return RealmDatabase()
    }

    @Provides
    @Singleton
    internal fun provideUtilityClass(): UtilityClass {
        return UtilityClass
    }


    @Singleton
    @Provides
    fun provideDataStoreRepository(@ApplicationContext context: Context) = DataStoreManager(context)

    @Singleton
    @Provides
    fun providesDatstoreRepo(
        @ApplicationContext context: Context
    ): DataStoreRepo = DataStoreManager(context)
}

