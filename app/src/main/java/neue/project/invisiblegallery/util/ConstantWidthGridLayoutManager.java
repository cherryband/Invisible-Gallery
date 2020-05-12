package neue.project.invisiblegallery.util;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class ConstantWidthGridLayoutManager extends GridLayoutManager {
    private Context context;
    private int minItemLengthDp;

    public ConstantWidthGridLayoutManager (Context context, int minItemLengthDp) {
        super(context, 1);
        this.context = context;
        this.minItemLengthDp = minItemLengthDp;
    }

    @Override
    public void onLayoutChildren (RecyclerView.Recycler recycler, RecyclerView.State state) {
        updateSpanCount();
        super.onLayoutChildren(recycler, state);
    }

    private void updateSpanCount () {
        float lengthDp = getParentLengthDp();
        int spancount = (int) (lengthDp / minItemLengthDp);

        if (spancount < 1) spancount = 1;

        setSpanCount(spancount);
    }

    private float getParentLengthDp (){
        float length = getWidth();
        return length / context.getResources().getDisplayMetrics().density;
    }

}
