package com.noice.todo_app.ui.fragments.auth_fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.noice.todo_app.R
import com.noice.todo_app.databinding.LoginFragmentBinding
import com.noice.todo_app.ui.activities.MainActivity
import com.noice.todo_app.ui.viewmodels.auth_viewmodels.LoginAndRegisterViewModel
import com.noice.todo_app.utils.Validate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val TAG = "login fragment"
class LoginFragment : Fragment() {



    private lateinit var viewModel: LoginAndRegisterViewModel
    private lateinit var bind: LoginFragmentBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        bind = LoginFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[LoginAndRegisterViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bind.loginRefreshLayout.isEnabled = false

        viewModel.counter.observe(viewLifecycleOwner){
            if(it != null){
                var msg = "check your inbox and spam folder for verification email, \n Click here to try again in ${it}s"
                if(it == 1){
                    bind.alertTv.isClickable = true
                    msg = "Click here to send verification email again"
                }
                bind.alertTv.text = msg
            }
        }
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null){
            val args = LoginFragmentArgs.fromBundle(requireArguments())
            if(args.msg != null){
                bind.alertTv.text = args.msg
            }
        }



        bind.loginBtn.setOnClickListener {
            val credentials = credentialsValid()
            authenticateUser(credentials)
        }

        bind.goToRegisterBtn.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
        
        bind.alertTv.setOnClickListener {
            if (viewModel.getUser() == null || (!viewModel.getUser()!!.isEmailVerified)) {
                bind.alertTv.isClickable = false
                bind.loginRefreshLayout.apply {
                    isEnabled = true
                    isRefreshing = true
                }

                lifecycleScope.launch(Dispatchers.IO) {
                    val result = viewModel.sendEmailVerfication()
                    if (result.isSuccess) {
                        withContext(Dispatchers.Main) {
                            bind.alertTv.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.purple_200
                                )
                            )
                        }
                        viewModel.startCounter()
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "Couldn't send verification email, Reason [ ${result.exceptionOrNull()!!.message} ]",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    withContext(Dispatchers.Main) {
                        bind.loginRefreshLayout.apply {
                            isEnabled = false
                            isRefreshing = false
                        }
                    }
                }
            }else{
                bind.alertTv.text = "Email has already been verified, Login to proceed"
            }
        }



    }

    private fun authenticateUser(credentials:Triple<Boolean,String?,String?>) {
        bind.loginRefreshLayout.apply {
            isEnabled = true
            isRefreshing = true
        }
        if(credentials.first){
            lifecycleScope.launch {
                val result = viewModel.loginUser(credentials.second!!,credentials.third!!)

                if(result.isSuccess){
                    if (viewModel.getUser()!!.isEmailVerified){
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }else{
                        withContext(Dispatchers.Main){
                            bind.alertTv.text = "Your email has not been verified. \nClick here to send Verification email"
                        }
                    }
                }else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, "Login Failed, [ Reason : ${result.exceptionOrNull()?.message} ]", Toast.LENGTH_LONG).show()
                    }
                }
                withContext(Dispatchers.Main){
                    bind.loginRefreshLayout.apply {
                        isEnabled = false
                        isRefreshing = false
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //drawable required for TextInputLayouts
        val checkDrawable = ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_checked_circle_24)

        bind.emailTiet.doAfterTextChanged {
            val email = it.toString()
            if(Validate.email(email).first){
                bind.emailTil.apply{
                    error = null
                    endIconDrawable = checkDrawable
                }
            }else{
                bind.emailTil.endIconMode = END_ICON_CLEAR_TEXT
            }
        }

        bind.passwordTiet.doAfterTextChanged {
            val password = it.toString()
            if(Validate.password(password).first){
                bind.passwordTil.apply{
                    error = null
                    endIconDrawable = checkDrawable
                }
            }else{
                bind.passwordTil.endIconDrawable = ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_task_divider_24)
            }
        }
    }

    private fun credentialsValid():Triple<Boolean,String?,String?> {
        var credentialsValid = true
        val email = bind.emailTiet.text.toString()
        val pass = bind.passwordTiet.text.toString()
        val emailResult = Validate.email(email)
        val passResult = Validate.password(pass)
        if(!emailResult.first){
            credentialsValid = false
            bind.emailTil.error = emailResult.second
        }
        if (!passResult.first){
            credentialsValid = false
            bind.passwordTil.error =passResult.second
        }
        if (credentialsValid){
            return Triple(credentialsValid,email,pass)
        }

        return Triple(credentialsValid,null,null)
    }

}