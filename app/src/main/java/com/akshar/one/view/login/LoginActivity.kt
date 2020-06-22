package com.akshar.one.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.akshar.one.view.activity.MainActivity
import com.akshar.one.R
import com.akshar.one.util.AndroidUtil
import com.akshar.one.view.activity.BaseActivity
import com.akshar.one.view.browser.InAppBrowserActivity

class LoginActivity : BaseActivity() {

    private var SHOW_USERNAME_SCREEN = true

    companion object{
        val SHOW_LOGIN_WITH_USERNAME_SCREEN = "showLoginWithUsernameScreen"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        intent.extras?.let { bundle ->  SHOW_USERNAME_SCREEN = bundle.getBoolean(SHOW_LOGIN_WITH_USERNAME_SCREEN,true) }

        if(SHOW_USERNAME_SCREEN) {
            goToLoginScreen()
        }else{
            goToLoginWithPhoneScreen()
        }

    }

    override fun onBackPressed() {
        val lastFragmentFromBackStack = getLastFragmentFromBackStack()
        if (lastFragmentFromBackStack != null) {
            if (lastFragmentFromBackStack is LoginFragment || lastFragmentFromBackStack is LoginWithPhoneFragment) {
                finish()
            }else {
                supportFragmentManager.popBackStack()
            }
        } else {
            finish()
        }
    }

    private fun isLastAddedFragment(fragmentName: String?): Boolean {
        val index = supportFragmentManager.backStackEntryCount - 1
        if (index >= 0) {
            val backEntry =
                supportFragmentManager.getBackStackEntryAt(index)
            if (backEntry != null) {
                val tag = backEntry.name
                return tag != null && tag.equals(fragmentName, ignoreCase = true)
            }
        }
        return false
    }

    private fun getLastFragmentFromBackStack(): Fragment? {
        var lastFragment: Fragment? = null
        val backStackCount = supportFragmentManager.backStackEntryCount
        if (backStackCount > 0) {
            val backStackEntry =
                supportFragmentManager.getBackStackEntryAt(backStackCount - 1)
            if (backStackEntry.name != null && !backStackEntry.name!!.isEmpty()) {
                lastFragment =
                    supportFragmentManager.findFragmentByTag(backStackEntry.name)
            }
        }
        return lastFragment
    }

    private fun replaceFragment(fragment: Fragment, name: String, addToBackStack: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frContainer, fragment, name)

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(name)
        }

        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun addFragment(fragment: Fragment, name: String, addToBackStack: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frContainer, fragment, name)

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(name)
        }

        fragmentTransaction.commitAllowingStateLoss()
    }

    fun goToLoginWithPhoneScreen() {
        replaceFragment(LoginWithPhoneFragment(), LoginFragment::class.java.name, true)
    }

    fun goToLoginScreen(){
        replaceFragment(LoginFragment.newInstance(), LoginFragment::class.java.name, true)
    }

    fun goToMainActivity() {
        AndroidUtil.startActivity(this, MainActivity::class.java)
        finish()
    }

    fun goToOTPScreen(mobileNumber: String){
        val bundle = Bundle()
        bundle.putString(OTPFragment.MOBILE_NUMBER, mobileNumber)
        val fragment = OTPFragment.newInstance()
        fragment.arguments = bundle
        addFragment(fragment, LoginFragment::class.java.name, true)
    }

    fun goToTermsAndConditionScreen() {
        val bundle = Bundle()
        bundle.putString(InAppBrowserActivity.TITLE, getString(R.string.title_terms_and_condition))
        bundle.putString(InAppBrowserActivity.URL, getString(R.string.TERMS_AND_CONDITION_URL))
        AndroidUtil.startActivity(this, InAppBrowserActivity::class.java, bundle)
    }
}