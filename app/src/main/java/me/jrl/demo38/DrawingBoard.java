package me.jrl.demo38;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawingBoard extends View {

    private Canvas canvas;

    //当前控件的宽高
    private int mWidth;
    private int mHeight;

    //画笔
    private Paint mPaint;
    //画笔颜色，默认黑色
    private int mPaintColor = Color.BLACK;
    //画布颜色，默认白色
    public static int mCanvasColor = Color.WHITE;

    public int getmCanvasColor() {
        return mCanvasColor;
    }

    //画笔宽度,默认10个像素点
    private int mPaintSize = dip2x(5);
    //橡皮擦宽度
    private int mEraseSize = dip2x(20);
    //缓冲的位图
    private Bitmap mBufferBitmap;
    //缓冲的画布
    private Canvas mBufferCanvas;

    public Canvas getmBufferCanvas() {
        return mBufferCanvas;
    }

    //上次的位置
    private float mLastX;
    private float mLastY;
    //路径
    private Path mPath;
    //设置图形混合模式为清除，橡皮擦的功能
    private PorterDuffXfermode mEraserMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    //保存的路径
    private List<DrawPathList> savePaths;
    //当前的路径
    private List<DrawPathList> currPaths;
    //最多保存20条路径
    private int MAX = 20;

    private DrawMode mDrawMode = DrawMode.PaintMode;

    public Canvas getCanvas() {
        return canvas;
    }

    public Bitmap getmBufferBitmap() {
        return mBufferBitmap;
    }

    //设置画笔颜色
    public void setmPaintColor(int mPaintColor) {
        this.mPaintColor = mPaintColor;
        // 2130968606
        // 2130968656
//        mPaint.setColor(Color.RED);
        mPaint.setColor(mPaintColor);
    }

    public DrawMode getMode() {
        return mDrawMode;
    }
    //设置画笔模式
    @SuppressLint("ResourceType")
    public void setMode(DrawMode mode){
        if (mode != mDrawMode){
            if (mode == DrawMode.EraserMode) {
                //橡皮擦模式
                mPaint.setStrokeWidth(mEraseSize);
                mPaint.setXfermode(mEraserMode);
                mPaint.setColor(mCanvasColor);
            }
            else if (mode == DrawMode.PaintMode){
                //画笔模式
                mPaint.setStrokeWidth(mPaintSize);
                mPaint.setXfermode(null);
                mPaint.setColor(mPaintColor);
            }else {
                canvas.drawColor(mCanvasColor);
            }
        }
        mDrawMode = mode;
   }

    public DrawingBoard(Context context) {
        this(context,null);
    }

    public DrawingBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initPath();
    }
    //获取像素点
    private int dip2x(float depValue){
        final float density = getResources().getDisplayMetrics().density;
        return (int)(depValue*density+0.5f);
    }

    private void initPath(){
        mPath = new Path();
        savePaths = new ArrayList<>();
        currPaths = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth,mHeight);
        initCanvas();
    }

    private void initCanvas(){
        //创建一个BITMAP，BITMAP就是Canvas绘制的图片
        mBufferBitmap = Bitmap.createBitmap(mWidth,mHeight,Bitmap.Config.ARGB_8888);
        mBufferCanvas= new Canvas(mBufferBitmap);
        mBufferCanvas.drawColor(ColorConst.red);
    }

    private void initPaint(){
        //设置画笔抗锯齿和抖动
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        //设置画笔填充方式为只描边
        mPaint.setStyle(Paint.Style.STROKE);
        //设置画笔颜色
        mPaint.setColor(mPaintColor);
        //设置画笔宽度
        mPaint.setStrokeWidth(mPaintSize);
        //设置圆形线帽
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //设置线段连接处为圆角
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mCanvasColor);
        this.canvas = canvas;
        canvas.drawBitmap(mBufferBitmap,0,0,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x =event.getX();
        float y =event.getY();
        switch (event.getAction())

        {
            case MotionEvent.ACTION_DOWN:

                mLastX = x;
                mLastY = y;
                mPath.moveTo(mLastX,mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                //绘制画出的图形
                mPath.quadTo(mLastX,mLastY,(mLastX+x)/2,(mLastY+y)/2);
                mBufferCanvas.drawPath(mPath,mPaint);
                invalidate();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                //保存路径
                savePath();
                mPath.reset();
                break;

        }

        return true;
    }
    private void savePath(){
        if (savePaths.size() == MAX){
            savePaths.remove(0);
        }
        savePaths.clear();
        savePaths.addAll(currPaths);
        Path path = new Path(mPath);
        Paint paint = new Paint(mPaint);
        savePaths.add(new DrawPathList(paint,path));
        currPaths.add(new DrawPathList(paint,path));
    }

    public void clean(){
        savePaths.clear();
        currPaths.clear();
        //将位图变为透明的
        mBufferBitmap.eraseColor(mCanvasColor);
        invalidate();
    }

    public int getmPaintSize() {
        return mPaintSize;
    }

    public void setmPaintSize(int mPaintSize) {
        this.mPaintSize = mPaintSize;
        mPaint.setStrokeWidth(mPaintSize);
    }

    //撤销
    public void lastStep() {
        if (currPaths.size() > 0) {
            currPaths.remove(currPaths.size() - 1);
            reDrawBitmap();
        }
    }

    //重绘位图
    private void reDrawBitmap(){
        mBufferBitmap.eraseColor(mCanvasColor);
        for (int i=0;i<currPaths.size();i++){
            DrawPathList path = currPaths.get(i);
            mBufferCanvas.drawPath(path.getPath(),path.getPaint());
        }
        invalidate();
    }

    public void setBitMapBackGroundColor(int color) {
        mCanvasColor = color;
        mBufferBitmap.eraseColor(color);
        reDrawBitmap();
    }
}
