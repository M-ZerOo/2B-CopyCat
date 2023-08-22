package com.melfouly.bestbuycopycat.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.melfouly.bestbuycopycat.R
import com.melfouly.bestbuycopycat.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var callBackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment.
        binding = FragmentAccountBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)

        // Create a callbackManager to handle login responses.
        callBackManager = CallbackManager.Factory.create()
        binding.facebookLogin.setPermissions("email", "public_profile")
        binding.facebookLogin.setFragment(this)
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        Log.d(
            "TAG",
            "access token: ${accessToken?.token}, isActive: ${AccessToken.isCurrentAccessTokenActive()}"
        )

        // Check if it's logged in, then logout (Which is not recommended but that's what I want)
        if (isLoggedIn) {
            LoginManager.getInstance().logOut()
        }

        binding.changeLanguageButton.setOnClickListener {
            LocaleHelper.changeLocale(requireActivity())
            requireActivity().recreate()
        }

        binding.facebookLogin
            .registerCallback(callBackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    Log.d("TAG", "onCancel: registerCallback")
                }

                override fun onError(error: FacebookException) {
                    Log.d("TAG", "onError: registerCallback: ${error.message}")
                }

                override fun onSuccess(result: LoginResult) {
                    // Create a new request to retrieve user's profile.
                    try {
                        Log.d("TAG", "onSuccess: access token: ${result.accessToken.token} ")
                        val request =
                            GraphRequest.newMeRequest(result.accessToken) { `object`, response ->
                                binding.apply {
                                    signedInAs.visibility = View.VISIBLE
                                    fullName.visibility = View.VISIBLE
                                    fullName.text = `object`?.getString("name")
                                    email.visibility = View.VISIBLE
                                    email.text = `object`?.getString("email")
                                    facebookLogin.visibility = View.GONE
                                    facebookLogout.visibility = View.VISIBLE
                                    googleLogin.visibility = View.GONE
                                }
                            }
                        val parameter = Bundle()
                        parameter.putString("fields", "name,email")
                        request.parameters = parameter
                        request.executeAsync()
                    } catch (e: Exception) {
                        Log.d("TAG", "onSuccess: Catch called: ${e.message}")
                    }

                }

            })

        binding.facebookLogin.setOnClickListener {
            // Tries to log the user with the requested read permissions.
            LoginManager.getInstance()
                .logInWithReadPermissions(this, callBackManager, listOf("public_profile,email"))
        }

        binding.googleLogin.setOnClickListener {
            googleSignIn()
        }

        binding.facebookLogout.setOnClickListener {
            LoginManager.getInstance().logOut()
            binding.apply {
                signedInAs.visibility = View.GONE
                fullName.visibility = View.GONE
                email.visibility = View.GONE
                facebookLogin.visibility = View.VISIBLE
                facebookLogout.visibility = View.GONE
                googleLogin.visibility = View.VISIBLE
            }
        }

        binding.googleLogout.setOnClickListener {
            firebaseAuth.signOut()
            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.apply {
                        signedInAs.visibility = View.GONE
                        fullName.visibility = View.GONE
                        email.visibility = View.GONE
                        googleLogin.visibility = View.VISIBLE
                        googleLogout.visibility = View.GONE
                        facebookLogin.visibility = View.VISIBLE
                    }
                } else {
                    Log.d("TAG", "onCreateView: google logout failed")
                }
            }
        }


        return binding.root
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 1811)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callBackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1811) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("TAG", "handleSignInResult: ${account.displayName}")
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseAuthWithGoogle(credential)
            } catch (e: ApiException) {
                Log.d("TAG", "handleSignInResult: ${e.message}, ${e.statusCode}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(credential: AuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    updateUiAsLoggedIn(user!!)
                } else {
                    Toast.makeText(requireActivity(), "Authentication failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun updateUiAsLoggedIn(account: FirebaseUser) {
        binding.signedInAs.visibility = View.VISIBLE
        binding.email.visibility = View.VISIBLE
        binding.fullName.visibility = View.VISIBLE
        binding.email.text = account.email
        binding.fullName.text = account.displayName
        binding.googleLogin.visibility = View.GONE
        binding.googleLogout.visibility = View.VISIBLE
        binding.facebookLogin.visibility = View.GONE
    }


    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            Log.d("TAG", "onStart: ${currentUser.email}")
            updateUiAsLoggedIn(currentUser)
        }
    }


}