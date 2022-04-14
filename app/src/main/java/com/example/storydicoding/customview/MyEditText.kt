package com.example.storydicoding.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storydicoding.R

class MyEditText : AppCompatEditText {
    private lateinit var emailIcon:Drawable
    private lateinit var passwordIcon:Drawable
    private lateinit var nameIcon:Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        emailIcon=ContextCompat.getDrawable(context, R.drawable.ic_baseline_email_24) as Drawable
        passwordIcon=ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24) as Drawable
        nameIcon=ContextCompat.getDrawable(context,R.drawable.ic_baseline_person_24) as Drawable
        Log.d(TAG, "init: $inputType")
        when (inputType) {
            EMAIL_TYPE -> setDrawable(startOfTheText = emailIcon)
            PASSWORD_TYPE -> setDrawable(startOfTheText = passwordIcon)
            NAME_TYPE->setDrawable(startOfTheText = nameIcon)
        }

        addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, before: Int, after: Int, count: Int) {
                if (inputType== EMAIL_TYPE){
                    if (s.toString().isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) error="Email tidak Valid!"
                }else if (inputType== PASSWORD_TYPE){
                    if (s.toString().length < 6) error="Password harus lebih dari 6 karakter"
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (inputType== EMAIL_TYPE){
                    if (s.toString().isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) error="Email tidak Valid!"
                }else if (inputType== PASSWORD_TYPE){
                    if (s.toString().length < 6) error="Password harus lebih dari 6 karakter"
                }
            }
        })
    }

    private fun setDrawable(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText, topOfTheText, endOfTheText, bottomOfTheText
        )
        compoundDrawablePadding=8
    }

    companion object{
        private const val TAG = "MyEmailEditText"
        private const val EMAIL_TYPE=33
        private const val PASSWORD_TYPE=129
        private const val NAME_TYPE=97
    }
}