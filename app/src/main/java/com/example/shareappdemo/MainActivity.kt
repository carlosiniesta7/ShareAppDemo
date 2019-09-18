package com.example.shareappdemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException



const val RC_SIGN_IN: Int = 17

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity : AppCompatActivity() {

    private var callbackManager: CallbackManager? = null
    var mGoogleSignInClient :GoogleSignInClient? = null

    var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()


    @SuppressLint("SetWorldReadable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fb_login_button.setOnClickListener {
            signInFacebook()
        }

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        gl_login_button.setOnClickListener {
            signInGoogle()
        }

    }

    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
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
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: com.google.android.gms.tasks.Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            Toast.makeText(this, account?.displayName.toString(), Toast.LENGTH_LONG).show()
            // Signed in successfully, show authenticated UI.
            startActivity(Intent(applicationContext, SecondActivity::class.java))

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(FragmentActivity.TG, "signInResult:failed code=" + e.statusCode)
            ///updateUI(null)
            Toast.makeText(this, "FAILED", Toast.LENGTH_LONG).show()
        }

    }
}
