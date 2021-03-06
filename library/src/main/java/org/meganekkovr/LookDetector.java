package org.meganekkovr;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joml.Vector3f;

public class LookDetector {

    private static LookDetector instance;
    private final long appPtr;

    private LookDetector(long appPtr) {
        this.appPtr = appPtr;
    }

    public synchronized static void init(long appPtr) {
        if (instance != null)
            throw new IllegalStateException("init was called twice!");

        instance = new LookDetector(appPtr);
    }

    public static LookDetector getInstance() {
        return instance;
    }

    private static native boolean isLookingAt(long appPtr, long entityPtr, long geometryComponentPtr, float[] first, float[] second, boolean axisInWorld);

    public boolean isLookingAt(@NonNull Entity entity) {
        return isLookingAt(entity, null, null, false);
    }

    public boolean isLookingAt(@NonNull Entity entity, @Nullable float[] firstIntersect, @Nullable float[] secondIntersect, boolean axisInWorld) {

        if (!entity.isShown() || !entity.hasComponent(GeometryComponent.class)) {
            return false;
        }

        GeometryComponent geometry = entity.getComponent(GeometryComponent.class);
        return isLookingAt(appPtr, entity.getNativePointer(), geometry.getNativePointer(), firstIntersect, secondIntersect, axisInWorld);
    }

    @Nullable
    public Vector3f getLookingPoint(@NonNull Entity entity) {
        return getLookingPoint(entity, false);
    }

    @Nullable
    public Vector3f getLookingPoint(@NonNull Entity entity, boolean axisInWorkd) {
        float[] first = new float[3];
        boolean looking = isLookingAt(entity, first, null, axisInWorkd);
        if (looking) {
            return new Vector3f(first[0], first[1], first[2]);
        } else {
            return null;
        }
    }
}
