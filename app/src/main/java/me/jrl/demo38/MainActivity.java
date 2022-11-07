package me.jrl.demo38;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Printer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DrawingBoard mDrawingBoard;
    //代表颜色选项
    private ImageView Black;
    private ImageView Gray;
    private ImageView White;
    private ImageView Red;
    private ImageView Orange;
    private ImageView Yellow;
    private ImageView Green;
    private ImageView Cyan;
    private ImageView Blue;
    private ImageView Purple;
    //对画板的操作
    private ImageView mPaper;
    private ImageView mPaint;
    private ImageView mEraser;
    private ImageView mClean;
    private ImageView mLast;
    //记录画笔大小
    private float size;

    //获取像素点
    private int dip2x(float depValue){
        final float density = getResources().getDisplayMetrics().density;
        return (int)(depValue*density+0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //默认画笔大小
       size = dip2x(10);
    }

    private void initView(){
        Black = findViewById(R.id.iv_black);
        Gray = findViewById(R.id.iv_gray);
        White = findViewById(R.id.iv_white);
        Red = findViewById(R.id.iv_red);
        Orange = findViewById(R.id.iv_orange);
        Yellow = findViewById(R.id.iv_yellow);
        Green = findViewById(R.id.iv_green);
        Cyan = findViewById(R.id.iv_cyan);
        Blue = findViewById(R.id.iv_blue);
        Purple = findViewById(R.id.iv_purple);

        mDrawingBoard = findViewById(R.id.draw_board);
        mPaper = findViewById(R.id.iv_paper);
        mPaint = findViewById(R.id.iv_paint);
        mEraser = findViewById(R.id.iv_eraser);
        mClean = findViewById(R.id.iv_clean);
        mLast = findViewById(R.id.iv_last);
    }

    private void initEvent(){
        Black.setOnClickListener(this);
        Gray.setOnClickListener(this);
        White.setOnClickListener(this);
        Red.setOnClickListener(this);
        Orange.setOnClickListener(this);
        Yellow.setOnClickListener(this);
        Green.setOnClickListener(this);
        Cyan.setOnClickListener(this);
        Blue.setOnClickListener(this);
        Purple.setOnClickListener(this);

        mPaper.setOnClickListener(this);
        mPaint.setOnClickListener(this);
        mEraser.setOnClickListener(this);
        mClean.setOnClickListener(this);
        mLast.setOnClickListener(this);
    }

    //设置画板清空对话框
    private void alertDialogClean(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sure you want to clear the drawing board?");
        builder.setPositiveButton("sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDrawingBoard.clean();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        final  AlertDialog dialog = builder.show();
        dialog.show();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_black:
                setColor(ColorConst.black);
                break;
            case R.id.iv_gray:
                setColor(ColorConst.mode_bg);
                break;
            case R.id.iv_white:
                setColor(ColorConst.white);
                break;
            case R.id.iv_red:
                setColor(ColorConst.red);
                break;
            case R.id.iv_orange:
                setColor(ColorConst.orange);
                break;
            case R.id.iv_yellow:
                setColor(ColorConst.yellow);
                break;
            case R.id.iv_green:
                setColor(ColorConst.green);
                break;
            case R.id.iv_cyan:
                setColor(ColorConst.cyan);
                break;
            case R.id.iv_blue:
                setColor(ColorConst.blue);
                break;
            case R.id.iv_purple:
                setColor(ColorConst.purple);
                break;
            case R.id.iv_paper:
                if (mDrawingBoard.getMode() != DrawMode.PaperMode) {
                    mDrawingBoard.setMode(DrawMode.PaperMode);
                }
                mPaper.getDrawable().setLevel(1);
                mPaper.getBackground().setLevel(1);
                mPaint.getDrawable().setLevel(0);
                mPaint.getBackground().setLevel(0);
                mEraser.getDrawable().setLevel(0);
                mEraser.getBackground().setLevel(0);
                mClean.getDrawable().setLevel(0);
                mClean.getBackground().setLevel(0);
                mLast.getDrawable().setLevel(0);
                mLast.getBackground().setLevel(0);
                break;
            case R.id.iv_paint:
                if (mDrawingBoard.getMode() != DrawMode.PaintMode) {
                    mDrawingBoard.setMode(DrawMode.PaintMode);
                }
                mPaper.getDrawable().setLevel(0);
                mPaper.getBackground().setLevel(0);
                mPaint.getDrawable().setLevel(1);
                mPaint.getBackground().setLevel(1);
                mEraser.getDrawable().setLevel(0);
                mEraser.getBackground().setLevel(0);
                mClean.getDrawable().setLevel(0);
                mClean.getBackground().setLevel(0);
                mLast.getDrawable().setLevel(0);
                mLast.getBackground().setLevel(0);
                break;
            case R.id.iv_eraser:
                if (mDrawingBoard.getMode() != DrawMode.EraserMode) {
                    mDrawingBoard.setMode(DrawMode.EraserMode);
                }
                mPaper.getDrawable().setLevel(0);
                mPaper.getBackground().setLevel(0);
                mPaint.getDrawable().setLevel(0);
                mPaint.getBackground().setLevel(0);
                mEraser.getDrawable().setLevel(1);
                mEraser.getBackground().setLevel(1);
                mClean.getDrawable().setLevel(0);
                mClean.getBackground().setLevel(0);
                mLast.getDrawable().setLevel(0);
                mLast.getBackground().setLevel(0);
                break;
            case R.id.iv_clean:
                mPaper.getDrawable().setLevel(0);
                mPaper.getBackground().setLevel(0);
                mPaint.getDrawable().setLevel(0);
                mPaint.getBackground().setLevel(0);
                mEraser.getDrawable().setLevel(0);
                mEraser.getBackground().setLevel(0);
                mClean.getDrawable().setLevel(1);
                mClean.getBackground().setLevel(1);
                mLast.getDrawable().setLevel(0);
                mLast.getBackground().setLevel(0);
                alertDialogClean();
                break;
            case R.id.iv_last:
                mPaper.getDrawable().setLevel(0);
                mPaper.getBackground().setLevel(0);
                mPaint.getDrawable().setLevel(0);
                mPaint.getBackground().setLevel(0);
                mEraser.getDrawable().setLevel(0);
                mEraser.getBackground().setLevel(0);
                mClean.getDrawable().setLevel(0);
                mClean.getBackground().setLevel(0);
                mLast.getDrawable().setLevel(1);
                mLast.getBackground().setLevel(1);
                mDrawingBoard.lastStep();
                break;
        }
    }

    private void setColor(int color) {
        if (mDrawingBoard.getMode() == DrawMode.PaintMode)
            mDrawingBoard.setmPaintColor(color);
        if (mDrawingBoard.getMode() == DrawMode.PaperMode)
            mDrawingBoard.setBitMapBackGroundColor(color);
    }
}
