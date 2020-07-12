package com.akshar.one.adapter

import android.content.Context
import android.widget.TextView
import android.view.ViewGroup
import android.graphics.Typeface
import android.view.View
import android.widget.ArrayAdapter
import android.R.attr.font
import androidx.core.content.res.ResourcesCompat
import com.akshar.one.R


class MySpinnerAdapter(context: Context, resource: Int, items: Array<String>) :
    ArrayAdapter<String>(context, resource, items) {
    // Initialise custom font, for example:
//    internal var font = Typeface.createFromAsset(
//        getContext().assets,
//        "font/opensans_bold.ttf"
//    )

    // Affects default (closed) state of the spinner
    public override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.typeface = ResourcesCompat.getFont(context, R.font.opensans_bold)
        return view
    }

    // Affects opened state of the spinner
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.setTypeface(ResourcesCompat.getFont(context, R.font.opensans_bold))
        return view
    }
}