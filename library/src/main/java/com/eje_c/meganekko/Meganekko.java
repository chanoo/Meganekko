package com.eje_c.meganekko;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface Meganekko {

    void setSceneFromXML(int xmlRes);

    void setScene(Scene scene);

    Scene getScene();

    void runOnGlThread(Runnable runnable);

    void runOnUiThread(Runnable runnable);

    void recenter();

    void showGazeCursor();

    void hideGazeCursor();

    void animate(@NonNull final Animator anim, @Nullable final Runnable endCallback);

    void cancel(@NonNull final Animator anim, @Nullable final Runnable endCallback);

    Context getContext();
}
