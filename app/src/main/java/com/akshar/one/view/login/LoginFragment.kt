package com.akshar.one.view.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.R
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppUtil
import com.akshar.one.view.fragment.BaseFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.login.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment(), View.OnClickListener {

    private var containerView: View? = null
    private var loginViewModel: LoginViewModel? = null
    private lateinit var loginActivity: LoginActivity

    companion object {

        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        containerView = inflater.inflate(R.layout.fragment_login, container, false)
        return containerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.application?.let {
            loginViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(LoginViewModel::class.java)
        }

        AndroidUtil.showKeyboard(activity, edtUserName)
        observers()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                AndroidUtil.hideKeyboard(context, btnLogin)
                if (validate()) {
                    imgCheck.visibility = View.VISIBLE
                    loginViewModel?.let {
                        if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                            it.loginServiceWithUsername(edtUserName?.text.toString(), edtPwd?.text.toString())
                        } else
                            Toast.makeText(
                                context,
                                getString(R.string.no_internet_available),
                                Toast.LENGTH_LONG
                            ).show()
                    }
                }
            }

            R.id.txtPhone -> {
                loginActivity.goToLoginWithPhoneScreen()
            }

            R.id.txtTermAndCondition -> {
                loginActivity.goToTermsAndConditionScreen()
            }
        }
    }

    private fun observers() {

        loginViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

        loginViewModel?.getMutableLiveDataLoginModel()?.observe(this, Observer {
            loginActivity.goToMainActivity()
        })

        loginViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AndroidUtil.showMessageDialog(context,it.message)
            }
        })
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    private fun initView() {

        edtUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtErrorUsername.visibility = View.GONE
            }

        })

        edtPwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtErrorPwd.visibility = View.GONE
            }

        })

        edtPwd.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                AndroidUtil.hideKeyboard(context, edtPwd)
                btnLogin.performClick()
            }
            false
        }

        setClickListeners()
    }

    private fun setClickListeners() {
        btnLogin.setOnClickListener(this)
        txtPhone.setOnClickListener(this)
        txtTermAndCondition.setOnClickListener(this)
    }

    private fun validate(): Boolean {

        if (edtUserName.text.isNullOrEmpty()) {
            txtErrorUsername.visibility = View.VISIBLE
            return false
        } else if(!AppUtil.isValidEmail(edtUserName.text.toString())){
            txtErrorUsername.visibility = View.VISIBLE
            return false
        } else if (edtPwd.text.isNullOrEmpty()) {
            txtErrorPwd.visibility = View.VISIBLE
            return false
        }
        return true
    }



}