package ir.teamtea.headerlibrary.recycler;

import android.view.View;

import androidx.annotation.NonNull;

public class HeaderViewHolder<T> extends CustomHeaderViewHolder<T> {


    public HeaderViewHolder(@NonNull View itemView) {
        super(itemView);
    }


    public void onAttach() {
        super.onAttach();
        int position = getAdapterPosition();
        if (position == 0) {
            itemView.setVisibility(View.INVISIBLE);
        } else {
            itemView.setVisibility(View.VISIBLE);
        }

    }

    public void changeViewVisibility(boolean shouldBeVisible) {
        if (shouldBeVisible) {
            if (itemView.getVisibility() != View.VISIBLE) {
                itemView.setVisibility(View.VISIBLE);
            }
        } else if (itemView.getVisibility() != View.INVISIBLE) {
            itemView.setVisibility(View.INVISIBLE);
        }
    }
}
