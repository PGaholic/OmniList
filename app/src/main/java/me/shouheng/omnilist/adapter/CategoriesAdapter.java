package me.shouheng.omnilist.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Collections;
import java.util.List;

import me.shouheng.omnilist.R;
import me.shouheng.omnilist.model.Category;
import me.shouheng.omnilist.utils.ColorUtils;
import me.shouheng.omnilist.widget.CircleImageView;
import me.shouheng.omnilist.widget.tools.IItemTouchHelperAdapter;


/**
 * Created by Wang Shouheng on 2018/2/14. */
public class CategoriesAdapter extends BaseQuickAdapter<Category, BaseViewHolder> implements
        IItemTouchHelperAdapter {

    private Category mJustDeletedToDoItem;
    private int mIndexOfDeletedToDoItem;
    private boolean isPositionChanged;

    private Context context;
    private boolean isDarkTheme;
    private int accentColor;

    private OnItemRemovedListener onItemRemovedListener;

    public CategoriesAdapter(Context context, @Nullable List<Category> data) {
        super(R.layout.item_category, data);

        this.context = context;

        this.accentColor = ColorUtils.accentColor();
        this.isDarkTheme = ColorUtils.isDarkTheme();
    }

    @Override
    protected void convert(BaseViewHolder helper, Category category) {
        final int categoryColor = category.getColor();

        if (isDarkTheme) helper.itemView.setBackgroundResource(R.color.dark_theme_background);

        helper.setText(R.id.tv_title, category.getName());
        helper.setText(R.id.tv_sub_title, context.getResources().getQuantityString(R.plurals.assignments_number, category.getCount(), category.getCount()));

        ((CircleImageView) helper.getView(R.id.iv_folder_background)).setFillingCircleColor(categoryColor);

        helper.addOnClickListener(R.id.iv_more);
        helper.setImageResource(R.id.iv_folder_portrait, category.getPortrait().iconRes);
    }

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        isPositionChanged = true;
        if(fromPosition < toPosition){
            for(int i=fromPosition; i<toPosition; i++){
                Collections.swap(getData(), i, i+1);
            }
        } else{
            for(int i=fromPosition; i > toPosition; i--){
                Collections.swap(getData(), i, i-1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemRemoved(int position, int direction) {
        isPositionChanged = true;
        mJustDeletedToDoItem = getData().remove(position);
        mIndexOfDeletedToDoItem = position;
        notifyItemRemoved(position);
        if (onItemRemovedListener != null){
            onItemRemovedListener.onItemRemoved(mJustDeletedToDoItem, mIndexOfDeletedToDoItem);
        }
    }

    @Override
    public void afterMoved() {
        notifyDataSetChanged();
    }

    public boolean isPositionChanged() {
        return isPositionChanged;
    }

    public void setOnItemRemovedListener(OnItemRemovedListener onItemRemovedListener) {
        this.onItemRemovedListener = onItemRemovedListener;
    }

    public interface OnItemRemovedListener {
        void onItemRemoved(Category item, int position);
    }
}
