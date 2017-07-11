package utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by Aisha on 7/5/2017.
 */

public class AnimationUtil {

    public static void animate(RecyclerView.ViewHolder holder, boolean goesDown) {


        /*
        YoYo.with(Techniques.FadeInLeft)
                .duration(1000)
                .repeat(1)
                .playOn(holder.itemView);
*/


        AnimatorSet animatorSet = new AnimatorSet();

      //  ObjectAnimator animateScaleX = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 0.5f, 0.8f, 1.0f);

        ObjectAnimator animateTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", goesDown == true ? 350 : -350, 0);

//        ObjectAnimator animateTranslateX = ObjectAnimator.ofFloat(holder.itemView, "translationX", 30, -30, 20, -20, 10, -10, 5, -5, 0);

        animatorSet.playTogether(animateTranslateY);
        animatorSet.setDuration(1000);
        animatorSet.start();


    }
}
