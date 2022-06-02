package academy.bangkit.kerani.customview

import academy.bangkit.kerani.R
import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class PasswordEditText: AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val passwordHint = resources.getString(R.string.password_hint)
        hint = passwordHint
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length < 6){
                    val passwordRequired = resources.getString(R.string.password_required)
                    error = passwordRequired
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
}
