package io.lowapple.sparta.git.app

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.lowapple.sparta.git.app.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        //
        viewModel.updateUserProfile()
        viewModel.user.observe(this, Observer {
            // 우저 이름
            binding.mainUserName.text = "안녕하세요 ${it.name}님"
            // 유저 프로파일
            Glide
                .with(applicationContext)
                .load(it.avatar_url)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.mainUserProfileImage)
            // 유지 일수 계산

        })

        /*
        Glide
            .with(this)
            .load(Uri.parse("https://ghchart.rshah.org/lowapple"))
            .into(binding.mainUserGreenSpace)
        */

        /*
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
         */
    }
}
