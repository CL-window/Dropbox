package cn.kejin.gitbook

import android.os.Bundle
import android.support.design.widget.Snackbar
import cn.kejin.gitbook.base.CustomStatusBarActivity
import cn.kejin.gitbook.common.dismissSoftInputMethod
import cn.kejin.gitbook.common.snack
import cn.kejin.gitbook.entities.AppAccount
import cn.kejin.gitbook.networks.*
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/11
 */
class LoginActivity : CustomStatusBarActivity()
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
        forgotPwdBtn?.setOnClickListener({startBrowser(Net.RESET_PWD_URL)})
        signUp?.setOnClickListener({startBrowser(Net.REGISTER_URL)})

        signIn?.setOnClickListener({startSignInToAccount()})
    }

    private fun startSignInToAccount()
    {
        val content = findViewById(android.R.id.content)
        dismissSoftInputMethod(this, content!!.windowToken);

        val username = userNameEdit?.text?.trim()?: "";
        if (username.isNullOrEmpty()) {
            snack(content, R.string.user_name_is_empty)
            return;
        }

        val password = passwordEdit?.text?:"";
        if (password.isNullOrEmpty()) {
            snack(content, R.string.password_is_empty)
            return;
        }

        showProgressDialog()
        val call = Net.restApi.signIn(username.toString(), password.toString(),
                object : HttpCallback<AppAccount>(AppAccount::class.java) {
                    override fun onResponse(model: AppAccount?, exception: HttpException?) {
                        if (MainApp.signIn(model)) {
                            snack(userNameEdit, R.string.login_success)
                            postDelay({ setResult(RESULT_OK); finish() }, 500)
                        }
                        else {
                            HttpException(HttpException.E_LOGIN_FAIL, exception?.msg?:"")
                                    .process(userNameEdit, Snackbar.LENGTH_INDEFINITE)
                        }
                        dismissProgressDialog()
                    }
        })

        progressDialog?.setOnCancelListener {
            call?.cancel()
            snack(userNameEdit, R.string.login_cancelled)
        }
    }
}