package com.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.config.BaseApplication;
import com.config.PrefStore;
import com.utils.GameObject;
import com.utils.Helper;
import com.utils.ResizeAnimation;

public class MyActivity extends Activity {

    boolean resultOfGame;
    int highScore = 0;
    ImageView firstImg;
    ImageView secondImg;
    ImageView resultImg;
    TextView highScoreTxt;

    RelativeLayout parentLayout;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
        parentLayout.setBackgroundColor(Color.parseColor(Helper.getRandomNiceColor()));

        firstImg = (ImageView) findViewById(R.id.first);
        secondImg = (ImageView) findViewById(R.id.second);
        resultImg = (ImageView) findViewById(R.id.result);
        highScoreTxt = (TextView) findViewById(R.id.highscore);

        highScoreTxt.setText("0");

        final ImageView progressBar = (ImageView) findViewById(R.id.progressbar);
        progressBar.setBackgroundColor(Color.parseColor("#4788f9"));

        ImageView trueImg = (ImageView) findViewById(R.id.true_btn);
        ImageView falseImg = (ImageView) findViewById(R.id.false_btn);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final ResizeAnimation animation = new ResizeAnimation(progressBar, width, 15, 0, 15, 4000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                looseGame();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        trueImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String color = Helper.getRandomNiceColor();
                Log.d("Color: ", color);
                parentLayout.setBackgroundColor(Color.parseColor(color));
                // parentLayout.setBackgroundResource();
                // TransitionDrawable background = (TransitionDrawable) parentLayout.getBackground();
                // You can pass the duration of the animation in milliseconds here
                // background.startTransition( 1500 );
                if (!animation.hasEnded()) {
                    animation.cancel();
                }
                if (resultOfGame) {
                    // reset game
                    progressBar.getLayoutParams().width = width;
                    setGameNumber();
                    animation.start();
                    highScore++;
                    highScoreTxt.setText(highScore + "");
                } else {
                    looseGame();
                }
            }
        });


        falseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!animation.hasEnded()) {
                    animation.cancel();
                }
                if (!resultOfGame) {
                    // reset game
                    progressBar.getLayoutParams().width = width;
                    setGameNumber();
                    animation.start();
                    highScore++;
                    highScoreTxt.setText(highScore + "");
                } else {
                    looseGame();
                }
            }
        });

        progressBar.setAnimation(animation);
    }

    private void setGameNumber() {
        GameObject o = Helper.randomGame();
        resultOfGame = o.isTrue;
        firstImg.setImageDrawable(Helper.getDrawableFromNumber(o.first));
        secondImg.setImageDrawable(Helper.getDrawableFromNumber(o.second));
        resultImg.setImageDrawable(Helper.getDrawableFromNumber(o.res));
    }

    private void looseGame() {
        Toast.makeText(BaseApplication.getAppContext(), "Game Loose", Toast.LENGTH_LONG);
        if (highScore > PrefStore.getMaxScore()) {
            PrefStore.setHighScore(highScore);
        }
    }




}
