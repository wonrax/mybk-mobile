package com.wonrax.mybk.repository

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.wonrax.mybk.model.exam.SemesterExam
import com.wonrax.mybk.network.Cookuest
import com.wonrax.mybk.network.Response
import com.wonrax.mybk.network.await
import okhttp3.FormBody
import okhttp3.RequestBody

class ExamsRepository(
    override val context: Context,
    override val storageFileName: String = "mybk_exams.txt"
) : BaseRepository<SemesterExam> {

    override var data = mutableStateOf<Array<SemesterExam>?>(null)

    override fun deserialize(data: String): Array<SemesterExam> {
        val gson = Gson()
        val list: MutableList<SemesterExam> = mutableListOf()
        try {
            val examsJson: JsonObject = JsonParser.parseString(data.replace("\n", "")).asJsonObject
            for ((key, value) in examsJson.entrySet()) {
                val v = value as JsonObject
                if (v["lichthi"].isJsonObject) {
                    val courseExams: MutableList<JsonElement> = mutableListOf()
                    for ((_, value1) in (v["lichthi"] as JsonObject).entrySet()) {
                        courseExams.add(value1)
                    }
                    v.remove("lichthi")
                    v.add("lichthi", gson.toJsonTree(courseExams).asJsonArray)
                }
                val obj: SemesterExam = gson.fromJson(v, SemesterExam::class.java)
                list.add(obj)
            }
        } catch (e: Exception) {
            throw JsonSyntaxException("Wrong data schemes.")
        }
        return list.toTypedArray()
    }

    override suspend fun requestData(token: String): Response {
        val body: RequestBody = FormBody.Builder().apply {
            add("_token", token)
        }.build()

        return Cookuest.post(
            "https://mybk.hcmut.edu.vn/stinfo/lichthi/ajax_lichthi",
            body
        ).await()
    }
}
