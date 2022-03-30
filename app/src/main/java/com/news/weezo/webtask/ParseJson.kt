package com.news.weezo.webtask


import com.google.gson.reflect.TypeToken
import com.news.weezo.MainApplication
import com.news.weezo.model.MediaFile
import org.json.JSONObject


/**
 * Created by pradeep on 26/05/18.
Mail id : pradeep.kumar@inventum.net
 */
class ParseJson {

    companion object {

        fun parseRegister(jsonData: String?): Boolean {
            val jsonObject = JSONObject(jsonData)
            if (jsonObject.optString("status").equals("1", true)) {

//                MainApplication.instance!!.showToast(jsonObject.optString("msg"))
                return true
            } else if (jsonObject.optString("status").equals("0", true)) {

//                MainApplication.instance!!.showToast(jsonObject.optString("msg"))
                return false
            }
            else {
                MainApplication.instance!!.showToast(jsonObject.optString("msg"))
            }
            return false
        }

        //
//
        fun isSuccess(response: String?): Boolean {
            val jsonObject = JSONObject(response)

            if (jsonObject.optString("status").equals("1", true)) {
//                MainApplication.instance!!.showToast(jsonObject.optString(msg()))
                return true
            }
            else if (jsonObject.optString("status").equals("0", true)) {

//                MainApplication.instance!!.showToast(jsonObject.optString("msg"))
                return false
            }
            else {
                var msg = jsonObject.optString("message")
                if (msg.isEmpty()) {
                    msg=  jsonObject.optString("result")
                }
                MainApplication.instance!!.showToast(msg)
//                {"response":{"status":302,"message":"Expired token"}}

            }
            return false

        }

        fun parseVideoList(response: String?): ArrayList<MediaFile> {
            val jsonObject = JSONObject(response).optJSONArray("data").toString()
            val gson = MainApplication.instance?.gsonLib()
            val type = object : TypeToken<List<MediaFile>>() {}.type
            return gson!!.fromJson<ArrayList<MediaFile>>(jsonObject, type)
        }

/*
        fun parseArticles(response: String?): ArrayList<ArticleModel> {
            val jsonObject = JSONObject(response).optJSONObject("response")
            val data = jsonObject.optJSONArray("result").toString();
            val gson = MainApplication.instance?.gsonLib()
            val type = object : TypeToken<List<ArticleModel>>() {}.type
            return gson!!.fromJson<ArrayList<ArticleModel>>(data, type)
        }
        fun parsePaytmCheckSum(response: String?): PaytmeResponse {
//            val jsonObject = JSONObject(response).optJSONObject("response")
//            val data = jsonObject.optJSONArray("result").toString();
            val gson = MainApplication.instance?.gsonLib()
            val type = object : TypeToken<PaytmeResponse>() {}.type
            return gson!!.fromJson<PaytmeResponse>(response, type)
        }


        fun parseBarACtCategory(arraydata: String): ArrayList<BarActCategoryModel> {
            val data = JSONArray(arraydata).toString()
            val gson = MainApplication.instance?.gsonLib()
            val type = object : TypeToken<List<BarActCategoryModel>>() {}.type
            return gson!!.fromJson<ArrayList<BarActCategoryModel>>(data, type)
        }

        fun parseBarActsNew(response: String?): ArrayList<BarActModel> {

            val data = JSONObject(response).optJSONObject("response").optJSONArray("result")// result array
            var list = ArrayList<BarActModel>()
            for (i in 0..data.length() - 1) {
                var barActModel = BarActModel()
                var actModelJson = JSONObject(data.getJSONObject(i).toString())
                barActModel.act_id = actModelJson.optString("act_id")
                barActModel.intro = actModelJson.optString("intro")
                barActModel.title = actModelJson.optString("title")
                barActModel.act_date = actModelJson.optString("act_date")
                barActModel.act_no = actModelJson.optString("act_no")
                barActModel.content_type = actModelJson.optString("content_type")
                barActModel.chapters = getChapter(actModelJson)
                list.add(barActModel)
            }

            return list


        }

        private fun getChapter(actModelJson: JSONObject): ArrayList<ChapterModel> {
            var list = ArrayList<ChapterModel>()
            var chapterArrayJson = JSONArray(actModelJson.getJSONArray("chapters").toString())
            for (j in 0..chapterArrayJson.length() - 1) {
                var chapterModel = ChapterModel()
                var chapterJson = chapterArrayJson.getJSONObject(j)
                chapterModel.chapter_title = chapterJson.optString("chapter_title")
                chapterModel.chapter_sub_title = chapterJson.optString("chapter_sub_title")
                chapterModel.total_section = chapterJson.optString("total_section")
                try {
                    chapterModel.section = getSectionList(chapterJson)
                }catch (e:Exception){

                }

                list.add(chapterModel)
            }
            return list
        }

        private fun getSectionList(chapterJson: JSONObject?): ArrayList<SectionModel> {
            val data = JSONArray(chapterJson!!.getJSONArray("section").toString()).toString()
            val gson = MainApplication.instance?.gsonLib()
            val type = object : TypeToken<List<SectionModel>>() {}.type
            return gson!!.fromJson<ArrayList<SectionModel>>(data, type)
        }
         fun getGazetteList(response: String?): ArrayList<GazetteModel> {
            val data = JSONObject(response).optJSONObject("response").optJSONArray("result")// result array

            val gson = MainApplication.instance?.gsonLib()
            val type = object : TypeToken<List<GazetteModel>>() {}.type
            return gson!!.fromJson<ArrayList<GazetteModel>>(data.toString(), type)
        }
        fun getNewsList(response: String?): ArrayList<NewsModel> {
            val data = JSONObject(response).optJSONObject("response").optJSONArray("result")// result array

            val gson = MainApplication.instance?.gsonLib()
            val type = object : TypeToken<List<NewsModel>>() {}.type
            return gson!!.fromJson<ArrayList<NewsModel>>(data.toString(), type)
        }


        public fun getProgileModel(data: JSONObject): ProfileModel {
            val gson = MainApplication.instance?.gsonLib()
            return gson!!.fromJson(data.toString(), ProfileModel::class.java)
        }

        public fun getCommonData(data: JSONObject): CommonDataModel {
            val gson = MainApplication.instance?.gsonLib()
            return gson!!.fromJson(data.toString(), CommonDataModel::class.java)
        }
        public fun getCaseDetails( response:String): CaseModel {
            val data = JSONObject(response).optJSONObject("response").optJSONObject("result").toString()
            val gson = MainApplication.instance?.gsonLib()
            return gson!!.fromJson(data, CaseModel::class.java)
        }

        fun getPlansList(response: JSONObject?): ArrayList<PaymentModel> {
            var data=response!!.optString("plan")
            val gson = MainApplication.instance?.gsonLib()
            val type = object : TypeToken<List<PaymentModel>>() {}.type
            return gson!!.fromJson<ArrayList<PaymentModel>>(data.toString(), type)
        }
        fun getAllCaseList(response: JSONObject?): ArrayList<CaseModel> {
            var data=response!!.optJSONArray("result")
            val gson = MainApplication.instance?.gsonLib()
            val type = object : TypeToken<List<CaseModel>>() {}.type
            return gson!!.fromJson<ArrayList<CaseModel>>(data.toString(), type)
        }
        fun getCurrentMonthDatat(response: JSONObject?): ArrayList<MonthDataModel> {
            var data=response!!.optJSONArray("result")
            val gson = MainApplication.instance?.gsonLib()
            val type = object : TypeToken<List<MonthDataModel>>() {}.type
            return gson!!.fromJson<ArrayList<MonthDataModel>>(data.toString(), type)
        }

        fun getMyDiaryResponse(response: JSONObject?): ArrayList<VisitModel> {
            var data=response!!.optJSONObject("result").optJSONArray("cases")
            val gson = MainApplication.instance?.gsonLib()
            val type = object : TypeToken<List<VisitModel>>() {}.type
            return gson!!.fromJson<ArrayList<VisitModel>>(data.toString(), type)
        }

        fun getMyLeave(response: JSONObject?): ArrayList<LeaveModel> {
            var data=response!!.optJSONArray("result")
            val gson = MainApplication.instance?.gsonLib()
            val type = object : TypeToken<List<LeaveModel>>() {}.type
            return gson!!.fromJson<ArrayList<LeaveModel>>(data.toString(), type)
        }
        fun getVisitmodelJsonStr(model:VisitModel):String{
            val gson = MainApplication.instance?.gsonLib()
            return  gson!!.toJson(model).toString();
        }
        fun getBulkHearingJsonStr(model: java.util.ArrayList<BulkEditDateModel>?):String{
            val gson = MainApplication.instance?.gsonLib()
            return  gson!!.toJson(model).toString();
        }
        public fun getVisitModel( response:String): VisitModel {
            val data = JSONObject(response).toString()
            val gson = MainApplication.instance?.gsonLib()
            return gson!!.fromJson(data, VisitModel::class.java)
        }*/
    }


}