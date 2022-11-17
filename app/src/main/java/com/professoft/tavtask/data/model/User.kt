package com.professoft.tavtask.data.model

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class User() : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var mail: String = ""
    var password: String = ""
    var state: Boolean = false


}