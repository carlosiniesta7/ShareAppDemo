package com.example.shareappdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 17
    private var callbackManager: CallbackManager? = null //Facebook
    var mGoogleSignInClient :GoogleSignInClient? = null

    private var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        fb_login_button.setOnClickListener {
            signInFacebook()
        }

        gl_login_button.setOnClickListener {
            signInGoogle()
        }

        gl_logout_button.setOnClickListener {
            signOut()
        }
    }

    // -------------- SIGN IN GOOGLE FUNCTION ----------

    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        mGoogleSignInClient?.signOut()
        googleButtonsVisibility(true)
    }


    // -------------- SIGN IN FACEBOOK FUNCTION ----------

    private fun signInFacebook() {
        fb_login_button.setReadPermissions("email")
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("MainActivity", "Facebook token:" + loginResult.accessToken.token)
                    Toast.makeText(applicationContext, loginResult.toString(), Toast.LENGTH_LONG).show()
                    startActivity(Intent(applicationContext, SecondActivity::class.java))
                }
                override fun onCancel() {
                    Log.d("MainActivity", "Facebook Canceled")
                }
                override fun onError(exception: FacebookException) {
                    Log.d("MainActivity", "Facebook Error")
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: com.google.android.gms.tasks.Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            googleButtonsVisibility(false)
            Toast.makeText(this, account?.displayName.toString(), Toast.LENGTH_LONG).show()
            startActivity(Intent(applicationContext, SecondActivity::class.java))

        } catch (e: ApiException) {
            Toast.makeText(this, "FAILED", Toast.LENGTH_LONG).show()
        }
    }

    private fun googleButtonsVisibility(flag: Boolean) {
        if(flag) {
            gl_login_button.visibility = VISIBLE
            gl_logout_button.visibility = GONE
        }
        else {
            gl_login_button.visibility = GONE
            gl_logout_button.visibility = VISIBLE
        }
    }
}
