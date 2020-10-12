package ir.teamtea.headerlibrary.recycler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public abstract class HeaderListAdapter<T, VH extends RecyclerView.ViewHolder> extends ListAdapter<T, VH> {

    public abstract boolean checkIsHeader(int position);

    protected HeaderListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    protected HeaderListAdapter(@NonNull AsyncDifferConfig<T> config) {
        super(config);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof CustomHeaderViewHolder) {
            ((CustomHeaderViewHolder) holder).onAttach();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VH holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof CustomHeaderViewHolder) {
            ((CustomHeaderViewHolder) holder).onDetach();
        }
    }
}
