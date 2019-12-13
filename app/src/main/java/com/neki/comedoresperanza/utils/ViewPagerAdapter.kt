package com.neki.comedoresperanza.utils

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.neki.comedoresperanza.R

class ViewPagerAdapter(val context: Context, val images:ArrayList<String>): PagerAdapter() {

    private var layoutInflater:LayoutInflater? = null

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 === p1
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(R.layout.image_view_holder, null)
        val viewer = v.findViewById<View>(R.id.image_slider_holder) as ImageView

        Glide.with(context).load( "https://res.cloudinary.com/djlzeapiz/image/upload/v1573315852/" + images[position] ).into(viewer)

        val vp = container as ViewPager
        vp.addView(v, 0)

        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)

        //val vp = container as ViewPager

    }

}