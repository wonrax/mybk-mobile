package com.wonrax.mybk.repository

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.wonrax.mybk.model.grade.SemesterGrade
import com.wonrax.mybk.network.Cookuest
import com.wonrax.mybk.network.Response
import com.wonrax.mybk.network.await
import okhttp3.FormBody
import okhttp3.RequestBody

class GradesRepository(
    override val context: Context,
    override val storageFileName: String = "mybk_grades.txt"
) : BaseRepository<SemesterGrade> {

    override var data = mutableStateOf<Array<SemesterGrade>?>(null)

    override fun deserialize(data: String): Array<SemesterGrade> {
        val gson = Gson()
        val semesters: MutableList<SemesterGrade> = mutableListOf()

        /* Because the response contains unknown fields,
        we'll need to convert them into an Json array */
        try {
            val examsJson: JsonObject = JsonParser.parseString(data).asJsonObject
            /* The "diem" field is sometimes an Json array and sometimes an Json object.
            So I have to manually convert object "diem" to array "diem" */
            for ((_, value) in examsJson.entrySet()) {
                val v = value as JsonObject
                if (v["diem"].isJsonObject) {
                    val courseExams: MutableList<JsonElement> = mutableListOf()
                    for ((_, value1) in (v["diem"] as JsonObject).entrySet()) {
                        courseExams.add(value1)
                    }
                    v.remove("diem")
                    v.add("diem", gson.toJsonTree(courseExams).asJsonArray)
                }

                val obj: SemesterGrade = gson.fromJson(v, SemesterGrade::class.java)
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
            "https://mybk.hcmut.edu.vn/stinfo/grade/ajax_grade",
            body
        ).await()
    }
}
