package com.wonrax.mybk.repository

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
import java.io.File

class ExamsRepository(
    override val filesDir: File,
    override val storageFileName: String = "mybk_exams.txt"
) : BaseRepository<SemesterExam> {

    override var data = mutableStateOf<Array<SemesterExam>?>(null)

    override fun deserialize(data: String): Array<SemesterExam> {
        val gson = Gson()
        val semesters: MutableList<SemesterExam> = mutableListOf()

        /* Because the response contains unknown fields,
        we'll need to convert them into an Json array */
        try {
            val examsJson: JsonObject = JsonParser.parseString(data).asJsonObject
            /* The "lichthi" field is sometimes an Json array and sometimes an Json object.
            So I have to manually convert object "lichthi" to array "lichthi" */
            for ((_, value) in examsJson.entrySet()) {
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
                semesters.add(obj)
            }
        } catch (e: Exception) {
            throw JsonSyntaxException("Wrong data schemes.")
        }
        return semesters.toTypedArray()
    }

    override suspend fun requestData(token: String): Response {
        val body: RequestBody = FormBody.Builder().apply {
            add("_token", token)
        }.build()

        return Cookuest().post(
            "https://mybk.hcmut.edu.vn/stinfo/lichthi/ajax_lichthi",
            body
        ).await()
    }
}
