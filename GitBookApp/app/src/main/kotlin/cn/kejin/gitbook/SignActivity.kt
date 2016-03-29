package cn.kejin.gitbook

import android.os.Bundle
import android.support.design.widget.Snackbar
import cn.kejin.gitbook.common.dismissSoftInputMethod
import cn.kejin.gitbook.common.snack
import cn.kejin.gitbook.networks.*
import kotlinx.android.synthetic.main.activity_login.*

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
        forgotPwdBtn?.setOnClickListener({startBrowser(Network.RESET_PWD_URL)})
        signUp?.setOnClickListener({startBrowser(Network.REGISTER_URL)})

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
        progressDialog?.setCanceledOnTouchOutside(false)
        val call = RestApiImpl.instance.signIn(username.toString(), password.toString(),
                object : HttpCallback<Models.MyAccount>(Models.MyAccount::class.java) {
                    override fun onResponse(model: Models.MyAccount?, exception: HttpException?) {
                        if (exception == null) {
                            UserAccount.set(model!!)
                            snack(userNameEdit, R.string.login_success)
                            postDelay({ setResult(RESULT_OK); finish() }, 500)
                        }
                        else {
                            snack(userNameEdit, R.string.login_failed, Snackbar.LENGTH_LONG)
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