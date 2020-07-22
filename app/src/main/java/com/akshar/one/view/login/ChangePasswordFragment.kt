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
import com.akshar.one.manager.SessionManager
import com.akshar.one.util.AndroidUtil
import com.akshar.one.view.fragment.BaseFragment
import com.akshar.one.viewmodels.ViewModelFactory
import com.akshar.one.viewmodels.login.LoginViewModel
import kotlinx.android.synthetic.main.fragment_change_password.*

class ChangePasswordFragment : BaseFragment(), View.OnClickListener {

    private var containerView: View? = null
    private var loginViewModel: LoginViewModel? = null
    private lateinit var loginActivity: LoginActivity

    companion object {

        @JvmStatic
        fun newInstance(): ChangePasswordFragment = ChangePasswordFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        containerView = inflater.inflate(R.layout.fragment_change_password, container, false)
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

        AndroidUtil.showKeyboard(activity, edtNewPwd)
        observers()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnChange -> {
                AndroidUtil.hideKeyboard(context, edtNewPwd)
                if (validate()) {
                    loginViewModel?.let { viewModel ->
                        if (context?.let { ctx -> AndroidUtil.isInternetAvailable(ctx) } == true) {

                            val loginModel = SessionManager.getLoginModel()
                            loginModel?.let {
                                if (it.appsList?.isNotEmpty() == true) {
                                    val username = it.appsList[0].username
                                    val newPassword = edtNewPwd.text?.toString()?.trim()
                                    username?.let { name ->
                                        newPassword?.let { pwd ->
                                            viewModel.changePasswordService(
                                                name,
                                                pwd
                                            )
                                        }
                                    }

                                }

                            }
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

        loginViewModel?.getMutableLiveDataChangePassword()?.observe(this, Observer {
            loginActivity.goToMainActivity()
        })
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    private fun initView() {

        edtNewPwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtErrorNewPwd.visibility = View.GONE
                txtErrorConfirmPwd.visibility = View.GONE
            }

        })

        edtConfirmPwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtErrorConfirmPwd.visibility = View.GONE
            }

        })

        edtConfirmPwd.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                AndroidUtil.hideKeyboard(context, edtConfirmPwd)
                btnChange.performClick()
            }
            false
        }

        setClickListeners()
    }

    private fun setClickListeners() {
        btnChange.setOnClickListener(this)
        txtGoBack.setOnClickListener(this)
    }

    private fun validate(): Boolean {

        when {
            edtNewPwd?.text.isNullOrEmpty() -> {
                txtErrorNewPwd.text = getString(R.string.error_please_enter_new_password)
                txtErrorNewPwd.visibility = View.VISIBLE
                return false
            }
            edtConfirmPwd?.text.isNullOrEmpty() -> {
                txtErrorConfirmPwd.text = getString(R.string.error_please_enter_confirm_password)
                txtErrorConfirmPwd.visibility = View.VISIBLE
                return false
            }
            edtNewPwd?.text?.toString()
                ?.equals(edtConfirmPwd?.text?.toString(), false) == false -> {
                edtConfirmPwd?.setText("")
                txtErrorNewPwd.text =
                    getString(R.string.error_new_password_and_confirm_password_not_match)
                txtErrorNewPwd.visibility = View.VISIBLE
                return false
            }
            else -> return true
        }
    }

}