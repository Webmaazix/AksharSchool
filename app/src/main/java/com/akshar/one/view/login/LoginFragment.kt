package com.akshar.one.view.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.R
import com.akshar.one.extension.gone
import com.akshar.one.extension.visible
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppConstant
import com.akshar.one.util.AppUtil
import com.akshar.one.view.fragment.BaseFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.login.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.edtPhoneNumber
import kotlinx.android.synthetic.main.fragment_login.linProgressIndicator
import kotlinx.android.synthetic.main.fragment_login.txtErrorPhoneNumber
import kotlinx.android.synthetic.main.fragment_login.txtTermAndCondition
import kotlinx.android.synthetic.main.fragment_login_with_phone.*

class LoginFragment : BaseFragment(), View.OnClickListener {

    private var containerView: View? = null
    private var loginViewModel: LoginViewModel? = null
    private lateinit var loginActivity: LoginActivity
    private var showUserScreen = true

    companion object {

        @JvmStatic
        fun newInstance(showUserScreen: Boolean): LoginFragment {
            val loginFragment = LoginFragment()
            loginFragment.showUserScreen = showUserScreen
            return loginFragment
        }

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
                    loginViewModel?.let { viewModel ->
                        if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                            if (showUserScreen) {
                                imgCheck.visibility = View.VISIBLE
                                viewModel.loginServiceWithUsername(
                                    edtUserName?.text.toString(),
                                    edtPwd?.text.toString()
                                )
                            } else {
                                edtPhoneNumber?.text?.toString()
                                    ?.let { phoneNumber -> viewModel.loginServiceGetOTP(phoneNumber) }
                            }

                        } else
                            AndroidUtil.showToast(context,getString(R.string.no_internet_available),true)
                    }
                }
            }

            R.id.txtPhone -> {
                showUserScreen = !showUserScreen
                initView()
            }

            R.id.txtTermAndCondition -> {
                loginActivity.goToTermsAndConditionScreen()
            }

            R.id.txtTroubleLogin -> {
                loginActivity.goToForgotPasswordScreen()
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

        loginViewModel?.getMutableLiveDataOTPResponse()?.observe(this, Observer {
            Log.d(AppConstant.TAG, "OTP status : " + it.status)
            loginActivity.goToOTPScreen(edtPhoneNumber?.text.toString())
        })


        loginViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AndroidUtil.showMessageDialog(context, it.message)
            }
        })
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    private fun initView() {

        if (showUserScreen) {
            clUserName?.visible()
            clPhone?.gone()
            txtPhone?.text = context?.getString(R.string.i_want_to_login_with_phone_number)
            btnLogin?.text = context?.getString(R.string.login)

        } else {
            clUserName?.gone()
            clPhone?.visible()
            txtPhone?.text = context?.getString(R.string.i_want_to_login_with_user_name)
            btnLogin?.text = context?.getString(R.string.str_continue)
        }

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

        edtPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtErrorPhoneNumber.visibility = View.GONE
            }

        })

        edtPhoneNumber.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                AndroidUtil.hideKeyboard(context, edtPhoneNumber)
                btnContinue.performClick()
            }
            false
        }

        setClickListeners()
    }

    private fun setClickListeners() {
        btnLogin.setOnClickListener(this)
        txtPhone.setOnClickListener(this)
        txtTermAndCondition.setOnClickListener(this)
        txtTroubleLogin.setOnClickListener(this)
    }

    private fun validate(): Boolean {

        if (showUserScreen) {
            if (edtUserName.text.isNullOrEmpty()) {
                txtErrorUsername.visibility = View.VISIBLE
                return false
            } else if (!AppUtil.isValidEmail(edtUserName.text.toString())) {
                txtErrorUsername.visibility = View.VISIBLE
                return false
            } else if (edtPwd.text.isNullOrEmpty()) {
                txtErrorPwd.visibility = View.VISIBLE
                return false
            }
        } else {
            if (edtPhoneNumber?.text.isNullOrEmpty()) {
                txtErrorPhoneNumber.text = getString(R.string.error_please_enter_phone_number)
                txtErrorPhoneNumber.visibility = View.VISIBLE
                return false
            } else if (edtPhoneNumber?.text?.isNotEmpty() == true && edtPhoneNumber?.text?.toString()
                    ?.trim()?.length?.let { it < 10 } == true
            ) {
                txtErrorPhoneNumber.visibility = View.VISIBLE
                txtErrorPhoneNumber.text = getString(R.string.error_please_enter_valid_phone_number)
                return false
            }
        }
        return true
    }

}