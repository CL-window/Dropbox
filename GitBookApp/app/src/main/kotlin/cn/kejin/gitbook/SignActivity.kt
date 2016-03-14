package cn.kejin.gitbook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import cn.kejin.gitbook.common.dismissSoftInputMethod
import cn.kejin.gitbook.networks.HttpCallback
import cn.kejin.gitbook.networks.Models
import cn.kejin.gitbook.networks.NetworkManager
import kotlinx.android.synthetic.main.activity_login.*

import cn.kejin.gitbook.common.snack;

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/11
 */
class SignActivity : BaseActivity()
{
    override fun getLayoutId(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        initializeContentView()
    }

    private fun initializeContentView()
    {
        menuCloseButton?.setOnClickListener({finish()})
        forgotPwdBtn?.setOnClickListener({startBrowser(NetworkManager.RESET_PWD_URL)})
        signUp?.setOnClickListener({startBrowser(NetworkManager.REGISTER_URL)})

        signIn?.setOnClickListener({startSignInToAccount()})
    }

    private fun startSignInToAccount()
    {
        val content = findViewById(android.R.id.content)
        dismissSoftInputMethod(this, content.windowToken);

        val username = userNameEdit?.text?.trim()?: "";
        if (username.isNullOrEmpty()) {
            cn.kejin.gitbook.common.snack(content, getString(R.string.user_name_is_empty))
            return;
        }

        val password = passwordEdit?.text?:"";
        if (password.isNullOrEmpty()) {
            snack(content, R.string.password_is_empty)
            return;
        }

        showProgressDialog()
        NetworkManager.instance.signIn(username.toString(), password.toString(),
                object : HttpCallback<Models.MyAccount>(Models.MyAccount::class.java) {
                    override fun onResponse(success: Boolean, model: Models.MyAccount?, code: Int, msg: String) {
                        if (success) {
                            UserAccount.setUserAccount(model!!)
                            snack(userNameEdit, R.string.login_success)
                            postDelay({ setResult(RESULT_OK); finish() }, 500)
                        }
                        else {
                            var message = getString(R.string.login_failed)
                            when (code) {
                                E_NO_NETWORK -> message = getString(R.string.no_network);
                            }
                            snack(userNameEdit, message, Snackbar.LENGTH_LONG)
                        }
                        dismissProgressDialog()
                    }
        })
    }
}