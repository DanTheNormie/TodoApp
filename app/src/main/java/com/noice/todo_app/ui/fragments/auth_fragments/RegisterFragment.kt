package com.noice.todo_app.ui.fragments.auth_fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout.END_ICON_CLEAR_TEXT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.noice.todo_app.R
import com.noice.todo_app.data.model.User
import com.noice.todo_app.databinding.RegisterFragmentBinding
import com.noice.todo_app.ui.viewmodels.auth_viewmodels.LoginAndRegisterViewModel
import com.noice.todo_app.utils.DateUtil
import com.noice.todo_app.utils.Validate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var viewmodel: LoginAndRegisterViewModel
    private lateinit var bind: RegisterFragmentBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = Firebase.auth
        bind = RegisterFragmentBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(requireActivity())[LoginAndRegisterViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind.registerRefreshLayout.isEnabled = false
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.registerBtn.setOnClickListener {
            bind.registerRefreshLayout.apply {
                isEnabled = true
                isRefreshing = true
            }
            val credentials = credentialsValid()
            if(credentials.first){
                lifecycleScope.launch(Dispatchers.IO) {
                    val result = viewmodel.registerUser(credentials.second!!.Email,credentials.second!!.password)

                    if(result.isSuccess){
                        if (viewmodel.isUserSignedIn()){
                            val profile = UserProfileChangeRequest.Builder().apply {
                                displayName = credentials.second!!.Name
                            }.build()
                            viewmodel.getUser()?.updateProfile(profile)
                        }
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment("please verify your email to proceed. \n Click here to verify your email."))
                        }
                    }else{
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, "Couldn't register User, Reason [ ${result.exceptionOrNull()?.message} ]", Toast.LENGTH_SHORT).show()
                        }
                    }
                    withContext(Dispatchers.Main){
                        bind.registerRefreshLayout.apply {
                            isEnabled = false
                            isRefreshing = false
                        }
                    }
                }
            }
        }

        bind.goToLoginBtn.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }


        //drawables required for TextInputLayouts
        val checkDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.ic_baseline_checked_circle_24
        )
        val hideDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.ic_baseline_task_divider_24
        )

        bind.usernameEt.doAfterTextChanged {
            var username = it.toString()
            if (Validate.userName(username).first) {
                bind.usernameTil.apply {
                    error = null
                    endIconDrawable = checkDrawable
                }
            } else {
                bind.usernameTil.endIconMode = END_ICON_CLEAR_TEXT
            }
        }

        bind.registerEmailEt.doAfterTextChanged {
            var email = it.toString()
            if (Validate.email(email).first) {
                bind.registerEmailTil.apply {
                    error = null
                    endIconDrawable = checkDrawable
                }
            } else {
                bind.registerEmailTil.endIconMode = END_ICON_CLEAR_TEXT
            }
        }

        bind.phoneNoEt.doAfterTextChanged {
            var num = it.toString()
            if (Validate.phoneNumber(num).first){
                bind.phoneNoTil.apply {
                    error = null
                    endIconDrawable = checkDrawable
                }
            }else{
                bind.phoneNoTil.endIconMode = END_ICON_CLEAR_TEXT
            }
        }

        bind.registerPasswordEt.doAfterTextChanged {
            var password = it.toString()
            if(Validate.password(password).first){
                bind.registerPasswordTil.apply {
                    error = null
                    endIconDrawable = checkDrawable
                }
            }else{
                bind.registerPasswordTil.endIconDrawable = hideDrawable
            }
        }

        bind.confirmPasswordEt.doAfterTextChanged {
            var pass1 = it.toString()
            val pass2 = bind.registerPasswordEt.text.toString()

            if (Validate.samePasswords(pass1,pass2).first){
                bind.confirmPasswordTil.apply {
                    error = null
                    endIconDrawable = checkDrawable
                }
            }else{
                bind.confirmPasswordTil.endIconDrawable = hideDrawable
            }
        }
    }

    private fun credentialsValid():Pair<Boolean,User?> {
        var credentialsValid = true

        val username = bind.usernameEt.text.toString()
        val email = bind.registerEmailEt.text.toString()
        val phoneNumber = bind.phoneNoEt.text.toString()
        val password = bind.registerPasswordEt.text.toString()
        val password2 = bind.confirmPasswordEt.text.toString()

        val usernameResult = Validate.userName(username)
        val registerEmailResult = Validate.email(email)
        val phoneNumberResult = Validate.phoneNumber(phoneNumber)
        val registerPasswordResult = Validate.password(password)
        val confirmPasswordResult = Validate.samePasswords(password, password2)

        if (!usernameResult.first) {
            credentialsValid = false
            bind.usernameTil.error = usernameResult.second
        }
        if (!registerEmailResult.first) {
            credentialsValid = false
            bind.registerEmailTil.error = registerEmailResult.second
        }
        if (!phoneNumberResult.first) {
            credentialsValid = false
            bind.phoneNoTil.error = phoneNumberResult.second
        }
        if (!registerPasswordResult.first) {
            credentialsValid = false
            bind.registerPasswordTil.error = registerPasswordResult.second
        }
        if (!confirmPasswordResult.first) {
            credentialsValid = false
            bind.confirmPasswordTil.error = confirmPasswordResult.second
        }
        if(credentialsValid){

            val user = User(
                "someId",
                username,
                email,
                DateUtil.getCurrentDate(),
                DateUtil.getCurrentDate(),
                password,
                phoneNumber
            )

            return Pair(credentialsValid,user)
        }

        return Pair(credentialsValid,null)
    }
}
