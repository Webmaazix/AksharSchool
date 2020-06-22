package com.akshar.one.view.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.akshar.one.R
import com.akshar.one.util.AndroidUtil
import com.akshar.one.util.AppConstant
import com.akshar.one.view.fragment.BaseFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.login.OTPViewModel
import kotlinx.android.synthetic.main.fragment_otp.*
import java.lang.StringBuilder

class OTPFragment : BaseFragment(), View.OnClickListener {

    private var containerView: View? = null
    private var otpViewModel: OTPViewModel? = null
    private lateinit var loginActivity: LoginActivity
    private var mobileNumber: String? = null

    companion object {

        val MOBILE_NUMBER = "mobileNumber"

        @JvmStatic
        fun newInstance() = OTPFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        containerView = inflater.inflate(R.layout.fragment_otp, container, false)
        return containerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.application?.let {
            otpViewModel = ViewModelProvider(
                ViewModelStore(),
                ViewModelFactory(it)
            ).get(OTPViewModel::class.java)
        }

        observers()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSubmit -> {
                AndroidUtil.hideKeyboard(context, btnSubmit)
                if (validate()) {
                    otpViewModel?.let {
                        if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                            val stringBuilder = StringBuilder()
                            stringBuilder.append(edt1.text.toString())
                            stringBuilder.append(edt2.text.toString())
                            stringBuilder.append(edt3.text.toString())
                            stringBuilder.append(edt4.text.toString())
                            stringBuilder.append(edt5.text.toString())
                            stringBuilder.append(edt6.text.toString())
                            mobileNumber?.let { phoneNumber -> it.loginServiceWithOTP(phoneNumber,stringBuilder.toString()) }
                        } else
                            Toast.makeText(
                                context,
                                getString(R.string.no_internet_available),
                                Toast.LENGTH_LONG
                            ).show()
                    }
                }

            }

            R.id.txtResendCode -> {
                AndroidUtil.hideKeyboard(context, btnSubmit)

                otpViewModel?.let {
                    if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {
                            mobileNumber?.let { phoneNumber -> it.loginServiceGetOTP(phoneNumber) }
                    } else
                        Toast.makeText(
                            context,
                            getString(R.string.no_internet_available),
                            Toast.LENGTH_LONG
                        ).show()
                }

            }

            R.id.txtGoBack -> {
                loginActivity.onBackPressed()
            }

            R.id.txtTermAndCondition -> {
                loginActivity.goToTermsAndConditionScreen()
            }
        }
    }

    private fun observers() {
        otpViewModel?.getErrorMutableLiveData()?.observe(this, Observer {
            it?.let {
                AndroidUtil.showMessageDialog(context, it.message)
            }
        })

        otpViewModel?.getMutableLiveDataOTPResponse()?.observe(this, Observer {
            Log.d(AppConstant.TAG, "OTP status : " + it.status)
        })

        otpViewModel?.getIsLoading()?.observe(this, Observer {
            showProgressIndicator(it)
        })

        otpViewModel?.getMutableLiveDataLoginModel()?.observe(this, Observer {
            loginActivity.goToMainActivity()
        })

    }

    private fun initView() {

        mobileNumber = arguments?.getString(MOBILE_NUMBER)

        txtDesc?.text = String.format(getString(R.string.we_have_sent_you_sms_on),mobileNumber)

        val listOfEditText = listOf(edt1, edt2, edt3, edt4, edt5, edt6)

        edt1.addTextChangedListener(OTPTextWatcher(edt1,listOfEditText))
        edt2.addTextChangedListener(OTPTextWatcher(edt2,listOfEditText))
        edt3.addTextChangedListener(OTPTextWatcher(edt3,listOfEditText))
        edt4.addTextChangedListener(OTPTextWatcher(edt4,listOfEditText))
        edt5.addTextChangedListener(OTPTextWatcher(edt5,listOfEditText))
        edt6.addTextChangedListener(OTPTextWatcher(edt6,listOfEditText))

        setListeners()
    }

    private fun setListeners() {
        btnSubmit.setOnClickListener(this)
        txtResendCode.setOnClickListener(this)
        txtGoBack.setOnClickListener(this)
        txtTermAndCondition.setOnClickListener(this)
    }

    private fun validate(): Boolean {

        if (edt1?.text.isNullOrEmpty()
            || edt2?.text.isNullOrEmpty()
            || edt3?.text.isNullOrEmpty()
            || edt4?.text.isNullOrEmpty()
            || edt5?.text.isNullOrEmpty()
            || edt6?.text.isNullOrEmpty()) {
            txtErrorCode.text = getString(R.string.error_please_enter_otp)
            txtErrorCode.visibility = View.VISIBLE
            return false
        }
        return true
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

}
