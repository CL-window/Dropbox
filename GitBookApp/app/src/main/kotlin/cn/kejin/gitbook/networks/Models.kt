//package cn.kejin.gitbook.networks
//
//import cn.kejin.gitbook.common.GSON
//import com.google.gson.annotations.SerializedName
//
//import java.util.ArrayList
//
///**
// * Author: Kejin ( Liang Ke Jin )
// * Date: 2016/3/8
// */
//
///**
// * Json Models
// */
//class Models {
//    /**
//     * just for generic
//     */
//    open class BaseReq//
//
//
//    /**
//     * Base Response Model
//     */
//    open class BaseResp {
//        var code = 0
//        var error = ""
//    }
//
//    /**
//     * Pagination Model
//     */
//    open class Pagination<Item> : BaseResp() {
//        var list = ArrayList<Item>()
//        var page = 0
//        var limit = 0
//        var total = 0
//    }
//
//    /**
//     * get details about the instance
//     */
//    class EndPoint : BaseResp() {
//        var type = "public"
//        var version = ""
//
//        var urls = URLS()
//
//        inner class URLS {
//            var main = ""
//            var api = ""
//        }
//    }
//
//    /**
//     * Basic Account Info
//     */
//    open class Account {
//        var id = ""
//        var type = ""
//        var username = ""
//        var name = ""
//        var location = ""
//        var website = ""
//
//        var email = ""
//
//        var urls = URLS()
//
//        inner class URLS {
//            var profile = ""
//            var avatar = ""
//        }
//    }
//
//
//    /**
//     * My Account Info, include token
//     */
//    class MyAccount : Account() {
//        var token = ""
//        var github = GitHub()
//
//        inner class GitHub {
//            var username = ""
//            var token = ""
//        }
//
//        fun copy() : MyAccount {
//            var a = MyAccount()
//            a.id = id;
//            a.type = type;
//            a.username = username;
//            a.name = name;
//            a.location = location;
//            a.website = website;
//            a.email = email;
//            a.urls.avatar = urls.avatar
//            a.urls.profile = urls.profile
//            a.token = token;
//            a.github.username = github.username;
//            a.github.token = github.token;
//
//            return a;
//        }
//
//        fun isSingedIn() = !token.isNullOrEmpty()
//
//        override fun equals(other: Any?): Boolean {
//            if (other is MyAccount) {
//                try {
//                    return GSON.toJson(this).equals(GSON.toJson(other))
//                }
//                catch (e : Exception) {
//                    return false
//                }
//            }
//            return false
//        }
//
//        override fun hashCode(): Int{
//            var result = token.hashCode()
//            result += 31 * result + github.hashCode()
//            return result
//        }
//
//    }
//
//    /**
//     * One Book Model
//     */
//    open class ABook {
//        var id = ""
//        var name = ""
//        var title = ""
//        var description = ""
//
//        @SerializedName("public")
//        var isPublic = true
//
//        inner class GitHub {
//            var id = ""
//        }
//
//        var github = GitHub()
//
//        inner class Cover {
//            var large = ""
//            var small = ""
//        }
//
//        var cover = Cover()
//
//        inner class BookUrls {
//            var git = ""
//            var access = ""
//            var homepage = ""
//            var read = ""
//
//            inner class Download {
//                var epub = ""
//                var mobi = ""
//                var pdf = ""
//            }
//
//            var download = Download()
//        }
//
//        var urls = BookUrls()
//
//        var author = Account()
//    }
//
//    /**
//     * Details of a book
//     */
//    class ABookDetail : ABook() {
//        inner class License {
//            var id = ""
//            var title = ""
//            var category = ""
//            var types = ArrayList<String>()
//            var url = ""
//            var required = ArrayList<String>()
//            var permitted = ArrayList<String>()
//            var forbidden = ArrayList<String>()
//        }
//
//        var license = License()
//
//        inner class Language {
//            var code = ""
//            var name = ""
//            var nativeName = ""
//        }
//
//        var language = Language()
//
//        inner class Dates {
//            var build = ""
//            var created = ""
//        }
//
//        var dates = Dates()
//
//        inner class Permissions {
//            var read = false
//            var write = false
//            var manage = false
//        }
//
//        var permissions = Permissions()
//
//        inner class LatestBuild {
//            var version = ""
//            var finished = ""
//            var started = ""
//        }
//
//        var latestBuild = LatestBuild()
//    }
//
//    class Books : Pagination<ABook>()//
//
//    /**
//     * A topic
//     */
//    class ATopic{
//        var id = ""
//        var name = ""
//        var books = 0
//    }
//
//    class Topics : Pagination<ATopic>()//
//
//    /**
//     * A Book branch
//     */
//    class ABranch {
//        var name = ""
//        var urls = URLs()
//
//        inner class URLs {
//            var website = ""
//            var epub = ""
//            var pdf = ""
//            var mobi = ""
//        }
//
//        var current = false
//    }
//
//    /**
//     * Book Contents
//     */
//    class BookContents {
//        inner class Next {
//            var path = ""
//            var title = ""
//            var level = "0"
//            var exists = false
//            var external = false
//            var introduction = false
//        }
//        inner class Chapter {
//            var index = 0
//            var title = ""
//            var introduction = false
//            var level = "0"
//            var path = ""
//            var percent = 0.0
//            var done = false
//
//            var prev = Next()
//            var next = Next()
//        }
//
//        inner class Progress {
//            var prevPercent : Double = 0.0
//            var percent : Double = 0.0
//
//            var chapters : List<Chapter> = listOf()
//
//            var current = Chapter()
//        }
//
//        inner class Sections {
//            var type = ""
//            var content = ""
//        }
//
//        var progress = Progress()
//        var sections = Sections()
//    }
//
//    /////////////////////////// Parse Html -> Model //////////////////////////////
//
//    /**
//     * WWW Author
//     */
//    class WWWAuthor {
//        var avatar = "" // avatar url
//        var name = ""
//        var url = ""
//        var join_time = ""
//            set(value) {
//                field = value.removePrefix("Joined On ")
//            }
//
//        override fun toString(): String {
//            return "[$name, $join_time, $url, $avatar]"
//        }
//    }
//    /**
//     * WWW book
//     */
//    class WWWBook {
//        var title = "" // title
//        var details = ""; // details url
//        var star_num = "0" // number of star
//            set(value) {
//                field = value.trim() //getNumString(value)
//            }
//
//        var pub_time = "" // publish time
//        var summary = ""
//
//        var author : WWWAuthor = WWWAuthor()
//
//        override fun toString(): String {
//            return "Book ($title, $details, $star_num, $pub_time, $summary, $author)"
//        }
//    }
//
//    class WWWTopic {
//        var url = "" // topic url
//
//        var name = ""
//
//        var num = "0"
//            set(value) {
//                field = getNumString(value)
//            }
//
//        var letter = ""
//
//        override fun toString(): String {
//            return "Topic ($name, $num, $letter, $url)"
//        }
//    }
//
//    /**
//     * Parse explore html
//     * https://www.gitbook.com/explore
//     */
//    class WWWExplorePage {
//        var books : MutableList<WWWBook> = mutableListOf()
//        var topics : MutableList<WWWTopic> = mutableListOf()
//
//        override fun toString(): String {
//            var str = "ExplorePage (${books.size}:Books, ${topics.size}:Topics){\n"
//            books.forEach { str += "$it\n" }
//            str += "\n";
//            topics.forEach { str += "$it\n" }
//            str += "}\n"
//            return str
//        }
//    }
//
//    /**
//     * https://www.gitbook.com/topics
//     */
//    class WWWTopicsPage {
//        var topics : MutableList<WWWTopic> = mutableListOf()
//
//        override fun toString(): String {
//            var str = "TopicPage (${topics.size}:Topics){\n"
//            topics.forEach { str += "$it\n" }
//            str += "}\n"
//
//            return str
//        }
//    }
//
//    /**
//     * https://
//     */
//    class WWWSearchBookPage {
//        var books : MutableList<WWWBook> = mutableListOf()
//
//        var sum_book = "0"
//            set(value) {
//                field = getNumString(value)
//            }
//
//        var sum_author = "0"
//            set(value) {
//                field = getNumString(value)
//            }
//
//        override fun toString(): String {
//            var str = "TopicPage ($sum_book:Books, $sum_author:Authors){\n"
//            books.forEach { str += "$it\n" }
//            str += "}\n"
//
//            return str
//        }
//    }
//
//    class WWWSearchAuthorPage {
//        var authors : MutableList<WWWAuthor> = mutableListOf()
//        var sum_book = "0"
//            set(value) {
//                field = getNumString(value)
//            }
//
//        var sum_author = "0"
//            set(value) {
//                field = getNumString(value)
//            }
//
//        override fun toString(): String {
//            var str = "TopicPage ($sum_book:Books, $sum_author:Authors){\n"
//            authors.forEach { str += "$it\n" }
//            str += "}\n"
//
//            return str
//        }
//    }
//
//    companion object {
//        fun getNumString(value: String) : String
//        {
//            var n = ""
//            for(c in value) {
//                if (c.isDigit()) {
//                    n += c;
//                }
//                else if (n.isNotEmpty()) {
//                    break;
//                }
//            }
//
//            if (n.isEmpty()) n="0"
//            return n
//        }
//    }
//}
