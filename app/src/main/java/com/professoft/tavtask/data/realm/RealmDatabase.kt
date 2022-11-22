package com.professoft.tavtask.data.realm

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.professoft.tavtask.data.model.User
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

class RealmDatabase {

    private val config = RealmConfiguration.Builder(schema = setOf(User::class))
        .build()
    private val realm: Realm = Realm.open(config)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun writeRealm(mailWrite : String,
                   passwordWrite : String,
                   stateWrite : Boolean
                   ) {
        realm.writeBlocking {
            copyToRealm(User().apply {
                mail = mailWrite
                password = passwordWrite
                state = stateWrite
            })
        }
    }

    fun getUser(): RealmResults<User> {
        return realm.query<User>()
                .find()
    }

    fun checkUser(mail: String,password: String): Boolean {
        return (realm.query<User>("mail = '$mail' AND password = '$password'")
            .find()).size > 0
    }

    suspend fun logInUser(mail: String ) {
        realm.write {
            val user: User? =
                this.query<User>("mail = '$mail'").first().find()
            user?.state = true
        }
    }

    fun findActiveUser(): RealmResults<User> {
        return realm.query<User>("state = true")
            .find()
    }
}