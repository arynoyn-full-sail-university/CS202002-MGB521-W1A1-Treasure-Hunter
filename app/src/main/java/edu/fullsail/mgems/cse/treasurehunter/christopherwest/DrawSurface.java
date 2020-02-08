package edu.fullsail.mgems.cse.treasurehunter.christopherwest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class DrawSurface extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private static final float RADIUS = 50.0f;
    private ArrayList<Item> mItems;
    private ArrayList<Item> mInventory;
    private ArrayList<PointF> mDugHoles;
    private Bitmap mBMPField;
    private Bitmap mBMPHole;
    private Rect mFieldDim;
    private Context context;


    public DrawSurface(Context context) {
        super(context);
        initialize(context);
    }

    public DrawSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public DrawSurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        this.context = context;
        setWillNotDraw(false);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        mItems = new ArrayList<>();
        mInventory = new ArrayList<>();
        mDugHoles = new ArrayList<>();
        mBMPField = BitmapFactory.decodeResource(getResources(), R.drawable.field);
        mBMPHole = BitmapFactory.decodeResource(getResources(), R.drawable.hole);
        mFieldDim = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(mBMPField, null, mFieldDim,null);
        float holeBMPVertOffset = mBMPHole.getHeight() / 2;
        float holeBMPHorizOffset = mBMPHole.getWidth() / 2;

        for (PointF hole : mDugHoles) {
            canvas.drawBitmap(mBMPHole, hole.x - holeBMPHorizOffset, hole.y - holeBMPVertOffset, null);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas c = holder.lockCanvas();
        if (c != null) {
            mFieldDim.set(0,0, c.getWidth(), c.getHeight());
        }
        holder.unlockCanvasAndPost(c);
        invalidate();

        for (int i=0; i < mItems.size(); i++) {
            mItems.get(i).x = (int)(Math.random() * (float)mFieldDim.width());
            mItems.get(i).y = (int)(Math.random() * (float)mFieldDim.height());
        }

        for (Item item : mItems) {
            item.x = (int)(Math.random() * (float)mFieldDim.width());
            item.y = (int)(Math.random() * (float)mFieldDim.height());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mDugHoles.add(new PointF(event.getX(),event.getY()));

        ArrayList<Item> foundItems = new ArrayList<>();
        for (int i = mItems.size()-1; i >= 0; i--) {
            Item item = mItems.get(i);
            if (item.isCollision(event.getX(),event.getY(), RADIUS)) {
                foundItems.add(item);
            }
        }

        if (foundItems.size() > 0) {
            mInventory.addAll(foundItems);
            mItems.removeAll(foundItems);
            Toast.makeText(getContext(), displayItemsWithCounts(foundItems), Toast.LENGTH_SHORT).show();
            foundItems.clear();
        }
        if (mItems.size() == 0) {
            Toast.makeText(getContext(), "Congratulations!\nYou found all the items!", Toast.LENGTH_LONG).show();
        }


        invalidate();
        return super.onTouchEvent(event);
    }

    public void setItems(ArrayList<Item> items) {
        this.mItems = items;
    }

    public ArrayList<Item> getInventory() {
        return mInventory;
    }

    private String displayItemsWithCounts(ArrayList<Item> inventory) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> itemNames = new ArrayList<>();
        for (Item inventoryItem : inventory){
            itemNames.add(inventoryItem.name);
        }
        Set<String> uniqueNames = new HashSet<>(itemNames);
        for (String itemName : uniqueNames) {
            if (itemName.equals("Gold")) {
                stringBuilder.insert(0, Collections.frequency(itemNames, itemName) + "g\n" );
            } else {
                stringBuilder.append(itemName + " (x" + Collections.frequency(itemNames, itemName) + ")\n");
            }
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());

        return stringBuilder.toString();
    }

}
