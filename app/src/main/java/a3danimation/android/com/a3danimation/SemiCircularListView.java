package a3danimation.android.com.a3danimation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SemiCircularListView extends ListView implements AbsListView.OnScrollListener{

    private int mItemHeight = 0;

    private double mRadius = -1;

    public SemiCircularListView(Context context) {
        this(context, null);
    }

    public SemiCircularListView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public SemiCircularListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setOnScrollListener(this);
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }


    public void setRadius(double radius) {
        if (this.mRadius != radius) {
            this.mRadius = radius;
            requestLayout();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        View itemView = this.getChildAt(0);
        if (itemView == null) {
            return;
        }

        int realTotalItemCount = getAdapter().getCount();
        if (realTotalItemCount == 0) {
            return;
        }

        if (mItemHeight == 0) {
            mItemHeight = itemView.getHeight();
        }

        double viewHalfHeight = view.getHeight() / 2.0f;

        double vRadius = view.getHeight();
        double hRadius = view.getWidth();

        double yRadius = (view.getHeight() + mItemHeight) / 2.0f;
        double xRadius = (vRadius < hRadius) ? vRadius : hRadius;
        if (mRadius > 0) {
            xRadius = mRadius;
        }

        for (int i = 0; i < visibleItemCount; i++) {
            itemView = this.getChildAt(i);
            if (itemView != null) {
                double y = Math.abs(viewHalfHeight - (itemView.getTop() + (itemView.getHeight() / 2.0f)));
                y = Math.min(y, yRadius);
                double angle = Math.asin(y / yRadius);
                double x = xRadius * Math.cos(angle);

                x -= xRadius;
                itemView.scrollTo((int) x, 0);
            }
        }

    }

}
