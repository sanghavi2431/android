package in.woloo.www.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public interface ScrollViewListener {
   public void onScrolled(RecyclerView recyclerView, int dx, int dy);
   public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState);
}
