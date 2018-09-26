package com.memoryladder.taketest.cards;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.memoryladder.taketest.cards.mnemonics.FindingMnemo;
import com.mastersofmemory.memoryladder.R;

import java.util.ArrayList;
import java.util.List;

public class SelectedCardsView extends LinearLayout {

    /* Views */
    private LargeCardView[] myCards;

    private AnimatorSet setFadeOut;
    private AnimatorSet setFadeIn;

    private Context context;

    public SelectedCardsView(Context context) { super(context); this.context = context;    }
    public SelectedCardsView(Context context, @Nullable AttributeSet attrs) { super(context, attrs); this.context = context;    }
    public SelectedCardsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); this.context = context;    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        initViews();
        initAnimations();
    }

    void presentBlankCards() {
        myCards[0].setCard(null);
        myCards[1].setCard(null);
        myCards[2].setCard(null);
    }

    void renderCards(final List<PlayingCard> cards) {
        setFadeOut.removeAllListeners();
        setFadeOut.cancel();
        setFadeIn.cancel();

        setFadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                for (int i=0; i<cards.size(); i++) {
                    setCardDisplayCount(cards.size());
                    myCards[i].setCard(cards.get(i));
                }

                setFadeIn.start();
            }
        });
        setFadeOut.start();
    }

    void setCardDisplayCount(int numCardsToDisplay) {
        for (int cardNum=1; cardNum<=myCards.length; cardNum++) {
            myCards[cardNum-1].setVisibility(numCardsToDisplay < cardNum ? View.GONE : View.VISIBLE);
        }
    }

    void setMnemonicsEnabled(boolean enabled) {
        if (enabled) {
            attachMnemonicTouchListeners();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        myCards = new LargeCardView[3];
        myCards[0] = findViewById(R.id.card_large_one);
        myCards[1] = findViewById(R.id.card_large_two);
        myCards[2] = findViewById(R.id.card_large_three);
    }

    private void initAnimations() {
        List<Animator> animation_fade_in = new ArrayList<>();
        final List<Animator> animation_fade_out = new ArrayList<>();

        final int animationDuration = 100;

        for (LargeCardView myCard : myCards) {
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(myCard, "alpha", 1, 0);

            fadeOut.setDuration(animationDuration);
            animation_fade_out.add(fadeOut);
            setFadeOut = new AnimatorSet();
            setFadeOut.playTogether(animation_fade_out);

            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(myCard, "alpha", 0, 1);
            fadeIn.setDuration(animationDuration);
            animation_fade_in.add(fadeIn);
            setFadeIn = new AnimatorSet();
            setFadeIn.playTogether(animation_fade_in);
        }
    }

    private void attachMnemonicTouchListeners() {
        for (LargeCardView cardView: myCards) {
            cardView.setOnTouchListener((v, event) -> {
                if (cardView.getCard() == null) {
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String text = FindingMnemo.getMnemoFor(cardView.getCard(), context);
                    cardView.showMnemonicText(text);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    cardView.hideMnemonicText();
                }

                return true;
            });
        }
    }
}
