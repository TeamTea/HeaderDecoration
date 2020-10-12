package ir.teamtea.headerlibrary;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ir.teamtea.headerlibrary.recycler.HeaderAdapter;
import ir.teamtea.headerlibrary.recycler.HeaderRecyclerData;
import ir.teamtea.headerlibrary.recycler.HeaderViewHolder;
import kotlin.Pair;

public class HeaderItemDecoration extends RecyclerView.ItemDecoration {
    private RecyclerView parent;
    private Boolean shouldFadeOutHeader = false;
    private Pair<Integer, RecyclerView.ViewHolder> currentHeader = null;

    public HeaderItemDecoration(RecyclerView parent, Boolean shouldFadeOutHeader) {
        this.parent = parent;
        this.shouldFadeOutHeader = shouldFadeOutHeader;
        initDecoration();
    }

    private void initDecoration() {
        parent.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                currentHeader = null;
            }
        });
        parent.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6,
                                       int i7) {
                currentHeader = null;
            }
        });
        parent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View childAt = parent.getChildAt(i);
                    RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(childAt);
                    if (viewHolder instanceof HeaderViewHolder) {
                        if (childAt.getTop() > 0) {
                            ((HeaderViewHolder) viewHolder).changeViewVisibility(true);
                        }
                        break;
                    }

                }
            }
        });
        // handle click on sticky header
        parent.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    if (currentHeader != null && currentHeader.getSecond() != null) {
                        return e.getY() <= currentHeader.getSecond().itemView.getBottom();
                    } else {
                        return e.getY() <= 0;
                    }
                } else {
                    return false;
                }
            }
        });
    }


    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent,
                           @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int topChildPosition = -1;
        int contactPoint = -1;
        //val topChild = parent.getChildAt(0) ?: return
        View topChild = parent.findChildViewUnder(
                parent.getPaddingLeft(),
                parent.getPaddingTop() /*+ (currentHeader?.second?.itemView?.height ?: 0 )*/
        );
        if (topChild != null) {
            topChildPosition = parent.getChildAdapterPosition(topChild);
            if (topChildPosition == RecyclerView.NO_POSITION) {
                return;
            }

            View headerView = getHeaderViewForItem(topChildPosition, parent);

            if (headerView != null) {
                contactPoint = headerView.getBottom() + parent.getPaddingTop();
            } else {
                return;
            }
            View childInContact = getChildInContact(parent, contactPoint);
            int childPosition = parent.getChildAdapterPosition(childInContact);
            if (isHeader(childPosition)) {
                moveHeader(c, headerView, childInContact, parent.getPaddingTop());
                return;
            }
            View childAt = parent.getChildAt(0);
            RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(childAt);
            if (viewHolder instanceof HeaderViewHolder && childAt.getTop() <= 0) {
                ((HeaderViewHolder) viewHolder).changeViewVisibility(false);
            }
            drawHeader(c, headerView, parent.getPaddingTop());
        }

    }

    private View getHeaderViewForItem(int itemPosition, RecyclerView parent) {
        if (parent.getAdapter() == null) {
            return null;
        }
        int headerPosition = getHeaderPositionForItem(itemPosition);
        if (headerPosition == RecyclerView.NO_POSITION) return null;

        int headerType = parent.getAdapter().getItemViewType(headerPosition);

        // if match reuse viewHolder
        if (currentHeader != null && currentHeader.getSecond() != null &&
                currentHeader.getFirst() == headerPosition &&
                currentHeader.getSecond().getItemViewType() == headerType) {
            return currentHeader.getSecond().itemView;
        }

        RecyclerView.ViewHolder headerHolder = parent.getAdapter().createViewHolder(parent, headerType);
        if (headerHolder != null) {
            parent.getAdapter().onBindViewHolder(headerHolder, headerPosition);
            fixLayoutSize(parent, headerHolder.itemView);
            // save for next draw
            currentHeader = new Pair<>(headerPosition, headerHolder);
        }
        return headerHolder.itemView;
    }

    private void drawHeader(Canvas c, View header, int paddingTop) {
        c.save();
        c.translate(0f, paddingTop);
        header.draw(c);
        c.restore();
    }

    private void moveHeader(Canvas c, View currentHeader, View nextHeader, int paddingTop) {
        c.save();
        if (!shouldFadeOutHeader) {
            c.clipRect(0, paddingTop, c.getWidth(), paddingTop + currentHeader.getHeight());
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                c.saveLayerAlpha(
                        new RectF(0f, 0f, c.getWidth(), c.getHeight()),
                        (((nextHeader.getTop() - paddingTop) / nextHeader.getHeight()) * 255)
                );
            } else {
                c.saveLayerAlpha(
                        0f, 0f, c.getWidth(), c.getHeight(),
                        (((nextHeader.getTop() - paddingTop) / nextHeader.getHeight()) * 255),
                        Canvas.ALL_SAVE_FLAG
                );
            }

        }
        c.translate(0f, (nextHeader.getTop() - currentHeader.getHeight()) /*+ paddingTop*/);

        currentHeader.draw(c);
        if (shouldFadeOutHeader) {
            c.restore();
        }
        c.restore();
    }

    private View getChildInContact(RecyclerView parent, int contactPoint) {
        View childInContact = null;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            Rect mBounds = new Rect();
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            if (mBounds.bottom > contactPoint) {
                if (mBounds.top <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child;
                    break;
                }
            }
        }
        return childInContact;
    }

    private void fixLayoutSize(ViewGroup parrent, View view) {

        // Specs for parent (RecyclerView)
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec =
                View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

        // Specs for children (headers)
        int childWidthSpec = ViewGroup.getChildMeasureSpec(
                widthSpec,
                parent.getPaddingLeft() + parent.getPaddingRight(),
                view.getLayoutParams().width
        );
        int childHeightSpec = ViewGroup.getChildMeasureSpec(
                heightSpec,
                parent.getPaddingTop() + parent.getPaddingBottom(),
                view.getLayoutParams().height
        );

        view.measure(childWidthSpec, childHeightSpec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    private int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = RecyclerView.NO_POSITION;
        int currentPosition = itemPosition;
        do {
            if (isHeader(currentPosition)) {
                headerPosition = currentPosition;
                break;
            }
            currentPosition -= 1;
        } while (currentPosition >= 0);
        return headerPosition;
    }

    private boolean isHeader(int position) {
        if (!(parent.getAdapter() instanceof HeaderAdapter)) {
            return false;
        } else {
            HeaderAdapter headerAdapter = (HeaderAdapter) parent.getAdapter();
            return headerAdapter.checkIsHeader(position);
        }
    }

}
