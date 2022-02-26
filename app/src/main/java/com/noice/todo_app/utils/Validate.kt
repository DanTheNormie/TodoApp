package com.noice.todo_app.utils

import android.util.Patterns

class Validate {

    companion object{

        fun password(pass: String): Pair<Boolean, String> {
            var isValid = false
            var msg = """password must contain atleast 
    1 capital letter, 
    1 small letter, 
    1 number, 
    1 of these [!,@,#,$,&,*] characters 
    and must be between 6-20 characters 
"""
            val pattern = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{6,20}$".toRegex()

            if (pass.isNotEmpty() && pattern.matches(pass)) {
                isValid = true
                msg = "password is valid"
            }


            return Pair(isValid, msg)
        }
        fun email(email:String):Pair<Boolean,String>{

            var isValid = false
            var msg = "please check your email address"

            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                isValid = true
                msg = "email is valid"
            }

            return Pair(isValid,msg)
        }
        fun userName(name:String):Pair<Boolean,String>{
            var isValid = false
            var msg ="""username must
    start with an alphabet 
    contain only alphanumericals or an underscore,  
    and must be between 6-20 characters 
"""
            val pattern = "^[A-Za-z][A-Za-z0-9_]{5,19}$".toRegex()

            if (name.isNotEmpty() && pattern.matches(name)){
                isValid = true
                msg = "username is valid"
            }


            return Pair(isValid,msg)
        }
        fun phoneNumber(num:String):Pair<Boolean,String>{
            return if (num.isEmpty()){
                Pair(true,"field is empty")
            }else{
                var isValid = false
                var msg = "please check your phone number"

                if (num.isNotEmpty() && Patterns.PHONE.matcher(num).matches() && num.length == 10){
                    isValid = true
                    msg = "phone number is valid"
                }

                Pair(isValid,msg)
            }

        }

        fun samePasswords(pass1:String,pass2:String):Pair<Boolean,String>{
            var isValid = false
            var msg = "both passwords are not the same"

            if(pass1 == pass2){
                isValid = true
                msg = "both passwords are same"
            }

            return Pair(isValid,msg)
        }
    }
}