package cn.kejin.gitbook.networks;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/8
 */

/**
 * Json Models
 */
public class Models
{
    /**
     * just for generic
     */
    public static class BaseReq
    {
        //
    }


    /**
     * Base Response Model
     */
    public static class BaseResp
    {
        public int code = 0;
        public String error = "";
    }

    /**
     * Pagination Model
     */
    public static class Pagination<Item> extends BaseResp
    {
        public ArrayList<Item> list = new ArrayList<>();
        public int page = 0;
        public int limit = 0;
        public int total = 0;
    }

    /**
     * get details about the instance
     */
    public static class EndPoint extends BaseResp
    {
        public String type = "public";
        public String version = "";

        public URLS urls = new URLS();

        public class URLS
        {
            public String main = "";
            public String api = "";
        }
    }

    /**
     * Basic Account Info
     */
    public static class Account extends BaseResp
    {
        public String id = "";
        public String type = "";
        public String username = "";
        public String name = "";
        public String location = "";
        public String website = "";

        public String email = "";

        public URLS urls = new URLS();
        public class URLS
        {
            public String profile = "";
            public String avatar = "";
        }
    }


    /**
     * My Account Info, include token
     */
    public static class MyAccount extends Account
    {
        public String token = "";
        public GitHub github = new GitHub();
        public class GitHub
        {
            public String username = "";
            public String token = "";
        }
    }

    /**
     * One Book Model
     */
    public static class ABook
    {
        public String id = "";
        public String name = "";
        public String title = "";
        public String description = "";

        @SerializedName("public")
        public boolean isPublic = true;

        public class GitHub
        {
            public String id = "";
        }
        public GitHub github = new GitHub();

        public class Cover
        {
            public String large = "";
            public String small = "";
        }
        public Cover cover = new Cover();

        public class BookUrls
        {
            public String git = "";
            public String access = "";
            public String homepage = "";
            public String read = "";
            public class Download
            {
                public String epub = "";
                public String mobi = "";
                public String pdf = "";
            }
            public Download download = new Download();
        }
        public BookUrls urls = new BookUrls();

        public Account author = new Account();
    }

    /**
     * Details of a book
     */
    public static class ABookDetail extends ABook
    {
        public class License
        {
            public String id = "";
            public String title = "";
            public String category = "";
            public ArrayList<String> types = new ArrayList<>();
            public String url = "";
            public ArrayList<String> required = new ArrayList<>();
            public ArrayList<String> permitted  = new ArrayList<>();
            public ArrayList<String> forbidden = new ArrayList<>();
        }
        public License license = new License();

        public class Language
        {
            public String code = "";
            public String name = "";
            public String nativeName = "";
        }
        public Language language = new Language();

        public class Dates
        {
            public String build = "";
            public String created = "";
        }
        public Dates dates = new Dates();

        public class Permissions
        {
            public boolean read = false;
            public boolean write = false;
            public boolean manage = false;
        }
        public Permissions permissions = new Permissions();

        public class LatestBuild
        {
            public String version = "";
            public String finished = "";
            public String started = "";
        }
        public LatestBuild latestBuild = new LatestBuild();
    }

    public static class Books extends Pagination<ABook>
    {
        //
    }

    /**
     * A topic
     */
    public static class ATopic
    {
        public String id = "";
        public String name = "";
        public int books = 0;
    }

    public static class Topics extends Pagination<ATopic>
    {
        //
    }
}
