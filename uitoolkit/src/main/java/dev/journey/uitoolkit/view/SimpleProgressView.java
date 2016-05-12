package dev.journey.uitoolkit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mwp on 2015/10/22.
 */
public class SimpleProgressView extends View {

	private int mProgress;
	private int progressColor = 0xffff6d00;

	public SimpleProgressView(Context context) {
		super(context);
	}

	public SimpleProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SimpleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
	}

	public void setProgress(int progress) {
		mProgress = progress;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawProgressColor(canvas);
		super.onDraw(canvas);
	}

	public void setProgressColor(int progressColor) {
		this.progressColor = progressColor;
	}

	private void drawProgressColor(Canvas canvas) {
		canvas.save(Canvas.CLIP_SAVE_FLAG);
		canvas.clipRect(0, 0, getWidth() * mProgress / 100, getHeight());
		canvas.drawColor(progressColor);
		canvas.restore();
	}
}
