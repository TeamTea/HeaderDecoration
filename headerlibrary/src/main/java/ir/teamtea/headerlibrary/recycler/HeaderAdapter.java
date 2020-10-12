package ir.teamtea.headerlibrary.recycler;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class HeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public abstract boolean checkIsHeader(int position);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof CustomHeaderViewHolder) {
            ((CustomHeaderViewHolder) holder).onAttach();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof CustomHeaderViewHolder) {
            ((CustomHeaderViewHolder) holder).onDetach();
        }
    }

}
