package io.lowapple.sparta.git.app

import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.StreamEncoder
import com.bumptech.glide.load.resource.file.FileToStreamDecoder
import com.caverock.androidsvg.SVG
import io.lowapple.sparta.git.app.util.SvgDecoder
import io.lowapple.sparta.git.app.util.SvgDrawableTranscoder
import io.lowapple.sparta.git.app.util.SvgSoftwareLayerSetter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Glide.with(this)
            .using(Glide.buildStreamModelLoader(Uri::class.java, this), InputStream::class.java)
            .from(Uri::class.java)
            .`as`(SVG::class.java)
            .transcode(SvgDrawableTranscoder(), PictureDrawable::class.java)
            .sourceEncoder(StreamEncoder())
            .cacheDecoder(FileToStreamDecoder<SVG>(SvgDecoder()))
            .decoder(SvgDecoder())
            .animate(android.R.anim.fade_in)
            .listener(SvgSoftwareLayerSetter<Uri>())
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .load(Uri.parse("https://ghchart.rshah.org/lowapple"))
            .into(main_user_green_space)
    }
}
