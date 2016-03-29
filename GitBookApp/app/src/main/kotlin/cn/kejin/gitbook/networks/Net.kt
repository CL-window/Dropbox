package cn.kejin.gitbook.networks

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */
interface Net
{
    companion object {

        val restApi : RestApi = RestApiImpl.instance
        val wwwApi : WWWApi = WWWApiImpl.instance

        /**
         * default http client
         */
        val defRequester = HttpRequester()


        val BASE_API_URL = "https://api.gitbook.com/"

        val BASE_WWW_URL = "https://www.gitbook.com/"

        /**
         * reset password website
         */
        val RESET_PWD_URL = getWWWAbsUrl("/settings/password/reset")

        /**
         * register website
         */
        val REGISTER_URL = getWWWAbsUrl("/join")

        /**
         * get absolute URL
         */
        fun getApiAbsUrl(uri: String): String =
                BASE_API_URL.removeSuffix("/") + "/" + uri.removePrefix("/")

        fun getWWWAbsUrl(uri: String): String =
                BASE_WWW_URL.removeSuffix("/") + "/" + uri.removePrefix("/")
    }
}