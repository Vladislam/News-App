package com.example.news.data.moshiadapters

import com.squareup.moshi.*

class SourceNameAdapter {
    private val options: JsonReader.Options = JsonReader.Options.of("id", "name")

    private val stringAdapter: JsonAdapter<String> = Moshi.Builder().build().adapter(
        String::class.java, emptySet(),
        "name"
    )

    @FromJson
    @SourceName
    fun fromJson(reader: JsonReader): String? {
        var name: String? = null
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> {}
                1 -> name = stringAdapter.fromJson(reader)
                -1 -> {
                    reader.skipValue()
                }
            }
        }
        reader.endObject()
        return name
    }

    @ToJson
    fun toJson(writer: JsonWriter, @SourceName value_: String?) {
        return
    }
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class SourceName