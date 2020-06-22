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
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppConstant
import com.akshar.one.view.fragment.BaseFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.login.LoginWithPhoneViewModel
import kotlinx.android.synthetic.main.fragment_login_with_phone.*

class LoginWithPhoneFragment : BaseFragment(), View.OnClickListener {

    private var containerView: View? = null
    private var loginWithPhoneViewModel: LoginWithPhoneViewModel? = null
    private lateinit var loginActivity: LoginActivity

    companion object {

        @JvmStatic
        fun newInstance() = LoginWithPhoneFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        containerView = inflater.inflate(R.layout.fragment_login_with_phone, container, false)
        return containerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.application?.let {
            loginWithPhoneViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(LoginWithPhoneViewModel::class.java)
        }

        observers()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnContinue -> {
                AndroidUtil.hideKeyboard(context, btnContinue)
                if (validate()) {
                    loginWithPhoneViewModel?.let {
                        if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {

                            edtPhoneNumber?.text?.toString()
                                ?.let { phoneNumber -> it.loginServiceGetOTP(phoneNumber) }
                        } else
                            Toast.makeText(
                                context,
                                getString(R.string.no_internet_available),
                                Toast.LENGTH_LONG
                            ).show()
                    }
                }

            }

            R.id.txtUsername -> {
                loginActivity.goToLoginScreen()
            }

            R.id.txtTermAndCondition -> {
                loginActivity.goToTermsAndConditionScreen()
            }
        }
    }

    private fun observers() {
        loginWithPhoneViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AndroidUtil.showMessageDialog(context, it.message)
            }
        })

        loginWithPhoneViewModel?.getMutableLiveDataOTPResponse()?.observe(this, Observer {
            Log.d(AppConstant.TAG, "OTP status : " + it.status)
            loginActivity.goToOTPScreen(edtPhoneNumber?.text.toString())
        })

        loginWithPhoneViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

    }

    private fun initView() {

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

        setListeners()
    }

    private fun setListeners() {
        btnContinue.setOnClickListener(this)
        txtUsername.setOnClickListener(this)
        txtTermAndCondition.setOnClickListener(this)
    }

    private fun validate(): Boolean {

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
        return true
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

}
