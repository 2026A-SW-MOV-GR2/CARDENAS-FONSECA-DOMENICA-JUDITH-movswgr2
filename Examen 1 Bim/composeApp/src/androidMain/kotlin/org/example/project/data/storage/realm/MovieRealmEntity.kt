package org.example.project.data.storage.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class MovieRealmEntity : RealmObject {
    @PrimaryKey
    var id: Long = 0
    var title: String = ""
    var director: String = ""
    var year: Int = 0
}
