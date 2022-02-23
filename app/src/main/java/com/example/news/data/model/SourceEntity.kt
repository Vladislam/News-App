package com.example.news.data.model

import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
open class SourceEntity(
    @PrimaryKey
    var _uuid: String = UUID.randomUUID().toString(),
    var id: String? = null,
    var name: String = "",
) : RealmObject(), Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SourceEntity) return false

        if (_uuid != other._uuid) return false
        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    final override fun hashCode(): Int {
        var result = _uuid.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + name.hashCode()
        return result
    }

    fun copy(): SourceEntity =
        SourceEntity(
            _uuid = _uuid,
            id = id,
            name = name,
        )
}