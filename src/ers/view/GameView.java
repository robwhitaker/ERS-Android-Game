package ers.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
	private Paint redPaint;
	private int circleX;
	private int circleY;
	private float radius;
	
	public GameView(Context context) {
		super(context);
		redPaint = new Paint();
		redPaint.setAntiAlias(true);
		redPaint.setColor(Color.RED);
		circleX = 100;
		circleY = 100;
		radius = 30;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(circleX, circleY, radius, redPaint);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		int eventAction = event.getAction();
		int x = (int)event.getX();
		int y = (int)event.getY();
		
		switch(eventAction) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			circleX = x;
			circleY = y;
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		
		invalidate();
		return true;
	}
}
