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
import com.akshar.one.viewmodels.login.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_trouble_login.*
import kotlinx.android.synthetic.main.fragment_trouble_login.edtPhoneNumber
import kotlinx.android.synthetic.main.fragment_trouble_login.linProgressIndicator
import kotlinx.android.synthetic.main.fragment_trouble_login.txtErrorPhoneNumber

class TroubleLoginFragment : BaseFragment(), View.OnClickListener {

    private var containerView: View? = null
    private var loginViewModel: LoginViewModel? = null
    private lateinit var loginActivity: LoginActivity

    companion object {

        @JvmStatic
        fun newInstance(): TroubleLoginFragment = TroubleLoginFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        containerView = inflater.inflate(R.layout.fragment_trouble_login, container, false)
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

        AndroidUtil.showKeyboard(activity, edtPhoneNumber)
        observers()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnContinue -> {
                AndroidUtil.hideKeyboard(context, edtPhoneNumber)
                if (validate()) {
                    loginViewModel?.let { viewModel ->
                        if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                            viewModel.loginServiceGetOTP(
                                edtPhoneNumber?.text.toString()
                            )
                        } else
                            Toast.makeText(
                                context,
                                getString(R.string.no_internet_available),
                                Toast.LENGTH_LONG
                            ).show()
                    }
                }
            }

            R.id.txtGoBack -> {
                loginActivity.onBackPressed()
            }
        }
    }

    private fun observers() {

        loginViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

        loginViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AndroidUtil.showMessageDialog(context, it.message)
            }
        })

        loginViewModel?.getMutableLiveDataOTPResponse()?.observe(this, Observer {
            Log.d(AppConstant.TAG, "OTP status : " + it.status)
            loginActivity.goToOTPScreen(edtPhoneNumber?.text.toString(),true)
        })
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
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

        setClickListeners()
    }

    private fun setClickListeners() {
        btnContinue.setOnClickListener(this)
        txtGoBack.setOnClickListener(this)
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

}