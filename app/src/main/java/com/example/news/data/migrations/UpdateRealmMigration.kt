package com.example.news.data.migrations

import io.realm.DynamicRealm
import io.realm.RealmMigration
import java.lang.RuntimeException

class UpdateRealmMigration : RealmMigration {

    companion object {
        const val SCHEMA_VERSION = 0L
    }

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        if (oldVersion != SCHEMA_VERSION) {
            throw RuntimeException("Unexpected scheme version. Expected: $SCHEMA_VERSION, actual: $oldVersion")
        }
    }
}