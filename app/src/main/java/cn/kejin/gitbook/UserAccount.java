package cn.kejin.gitbook;

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/8
 */

import cn.kejin.gitbook.networks.Models;

/**
 * save user account information
 */
public class UserAccount
{
    public String id = "";
    public String type = "";
    public String username = "";
    public String name = "";
    public String location = "";
    public String website = "";

    public String email = "";

    public String profile = "";
    public String avatar = "";

    public String token = "";

    public String github_username = "";
    public String github_token = "";

    public final static UserAccount mUser = new UserAccount();

    public static void setUserAccount(Models.MyAccount account)
    {
        if (account == null) {
            account = new Models.MyAccount();
        }

        mUser.id = account.id;
        mUser.type = account.type;
        mUser.username = account.username;
        mUser.name = account.name;
        mUser.location = account.location;
        mUser.website = account.website;
        mUser.email = account.email;
        mUser.profile = account.urls.profile;
        mUser.avatar = account.urls.avatar;
        mUser.token = account.token;
        mUser.github_token = account.github.token;
        mUser.github_username = account.github.username;
    }

    /**
     * clear user information
     */
    public static void signout()
    {
        setUserAccount(null);
    }
}
