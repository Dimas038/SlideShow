package com.example.slideshowfinalversion.ui;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.slideshowfinalversion.App;
import com.example.slideshowfinalversion.R;
import com.example.slideshowfinalversion.SimpleAnimationListener;
import com.example.slideshowfinalversion.SimpleImageLoadingListener;
import com.example.slideshowfinalversion.model.CellsModel;
import com.example.slideshowfinalversion.model.UrlModel;
import com.example.slideshowfinalversion.retrofit.exception.UnknownApiException;
import com.example.slideshowfinalversion.retrofit.interactor.ApiInteractor;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Inject
    ApiInteractor interactor;

    private ViewFlipper viewFlipperTop;
    private ViewFlipper viewFlipperBottom;

    private Disposable disposableAction;

    private int prepareCount;

    private final MediaPlayer.OnPreparedListener onPreparedListener = mp -> checkPrepareComplete();

    private final ImageLoadingListener imageLoadingListener = new SimpleImageLoadingListener() {
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            checkPrepareComplete();
        }
    };
    private Animation.AnimationListener animationListenerTop = new SimpleAnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            if (viewFlipperTop.getCurrentView() instanceof ScalableVideoView) {
                viewFlipperTop.stopFlipping();
                ScalableVideoView videoView = (ScalableVideoView) viewFlipperTop.getCurrentView();
                videoView.setOnCompletionListener(mp -> {
                    videoView.setOnCompletionListener(null);
                    viewFlipperTop.showNext();
                    viewFlipperTop.startFlipping();
                });
                videoView.start();
            }
        }
    };

    private Animation.AnimationListener animationListenerBottom = new SimpleAnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            if (viewFlipperBottom.getCurrentView() instanceof ScalableVideoView) {
                viewFlipperBottom.stopFlipping();
                ScalableVideoView videoView = (ScalableVideoView) viewFlipperBottom.getCurrentView();
                videoView.setOnCompletionListener(mp -> {
                    videoView.setOnCompletionListener(null);
                    viewFlipperBottom.showNext();
                    viewFlipperBottom.startFlipping();
                });
                videoView.start();
            }
        }
    };

    private void checkPrepareComplete() {
        prepareCount--;
        if (prepareCount == 0) {
            if (viewFlipperTop.getCurrentView() instanceof ScalableVideoView) {
                ScalableVideoView videoView = (ScalableVideoView) viewFlipperTop.getCurrentView();
                videoView.setOnCompletionListener(mp -> {
                    videoView.setOnCompletionListener(null);
                    viewFlipperTop.showNext();
                    viewFlipperTop.startFlipping();
                });
                videoView.start();
            } else {
                viewFlipperTop.startFlipping();
            }
            if (viewFlipperBottom.getCurrentView() instanceof ScalableVideoView) {
                ScalableVideoView videoView = (ScalableVideoView) viewFlipperBottom.getCurrentView();
                videoView.setOnCompletionListener(mp -> {
                    videoView.setOnCompletionListener(null);
                    viewFlipperBottom.showNext();
                    viewFlipperBottom.startFlipping();
                });
                videoView.start();
            } else {
                viewFlipperBottom.startFlipping();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        App.INSTANCE.getAppComponent().inject(this);

        viewFlipperTop = findViewById(R.id.viewFlipperTop);
        viewFlipperBottom = findViewById(R.id.viewFlipperBottom);

        viewFlipperTop.setInAnimation(this, R.anim.fade_in);
        viewFlipperTop.setOutAnimation(this, R.anim.fade_out);
        viewFlipperBottom.setInAnimation(this, R.anim.fade_in);
        viewFlipperBottom.setOutAnimation(this, R.anim.fade_out);

        viewFlipperTop.getInAnimation().setAnimationListener(animationListenerTop);
        viewFlipperBottom.getInAnimation().setAnimationListener(animationListenerBottom);

        load();
    }

    public void load() {
        disposableAction = Single.just(true)
                .subscribeOn(Schedulers.io())
                .map(id -> interactor.getDataInfo())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                    CellsModel modelTop = models.get(0).getCells().get(0);
                    CellsModel modelBottom = models.get(0).getCells().get(1);
                    prepareCount = modelTop.getUrls().size() + modelBottom.getUrls().size();
                    fillViewFlipper(viewFlipperTop, modelTop);
                    fillViewFlipper(viewFlipperBottom, modelBottom);
                }, throwable -> {
                    throwable.printStackTrace();
                    if (throwable instanceof IOException) {

                        return;
                    }
                    if (throwable instanceof UnknownApiException) {

                        return;
                    }
                });
    }

    private void fillViewFlipper(ViewFlipper viewFlipper, CellsModel model) {
        viewFlipper.setFlipInterval(model.getSliderInterval());
        for (int i = 0; i < model.getUrls().size(); i++) {
            UrlModel urlModel = model.getUrls().get(i);
            addView(viewFlipper, urlModel);
        }
    }

    private void addView(ViewFlipper viewFlipper, UrlModel urlModel) {
        switch (urlModel.getMimeType()) {
            case "video/mp4":
                addVideoView(viewFlipper, urlModel);
                break;
            case "image/jpeg":
                addImageView(viewFlipper, urlModel);
                break;
        }
    }

    private void addVideoView(ViewFlipper viewFlipper, UrlModel model) {
        ScalableVideoView videoView = new ScalableVideoView(this);
        videoView.setLayoutParams(new ViewFlipper.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewFlipper.addView(videoView);
        try {
            videoView.setDataSource(model.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }

        videoView.setScalableType(ScalableType.CENTER_CROP);

        videoView.prepareAsync(onPreparedListener);
    }

    private void addImageView(ViewFlipper viewFlipper, UrlModel model) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewFlipper.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage(model.getUrl(), imageView, imageLoadingListener);

        viewFlipper.addView(imageView);
    }

    protected void disposableAction() {
        if (disposableAction != null) {
            if (!disposableAction.isDisposed()) {
                disposableAction.dispose();
            }
            disposableAction = null;
        }
    }

    @Override
    protected void onDestroy() {
        disposableAction();
        if (viewFlipperTop != null) {
            viewFlipperTop.stopFlipping();
            viewFlipperTop.getAnimation().setAnimationListener(null);
            viewFlipperTop = null;
        }
        super.onDestroy();
    }
}
