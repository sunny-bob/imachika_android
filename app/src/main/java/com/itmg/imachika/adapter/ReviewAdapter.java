package com.itmg.imachika.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itmg.imachika.R;
import com.itmg.imachika.model.ReReview;
import com.itmg.imachika.model.Review;
import com.itmg.imachika.ui.EvaluateActivity;
import com.itmg.imachika.ui.LoginActivity;
import com.itmg.imachika.util.Constant;
import com.itmg.imachika.util.Mytoast;
import com.itmg.imachika.util.OkHttpUtils;
import com.itmg.imachika.util.PreferencesUtils;
import com.itmg.imachika.util.RequestCallBack;
import com.itmg.imachika.util.URLInfo;
import com.itmg.imachika.util.Utils;
import com.itmg.imachika.view.MyListView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/10 0010.
 */

public class ReviewAdapter extends RecyclerView.Adapter {
    String TAG = "ReviewAdapter";
    Context context;
    List<Review> list;
    private MyClickListener mListener;
    public ReviewAdapter(Context context, List<Review> list,MyClickListener listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public void setData(List<Review> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_review, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new RecyclerHolder(view);
    }

    boolean itemReviewShow = false;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final RecyclerHolder mHolder = (RecyclerHolder) holder;
        final Review item = list.get(position);
        Log.i(TAG,"onBindViewHolder item.isHint()"+item.isHint());
        if(item.isHint()){//显示、隐藏内容
            Log.i(TAG,"onBindViewHolder position ==== mPosition ");
            mHolder.mainViwe.setVisibility(View.GONE);
            mHolder.hintTv.setVisibility(View.VISIBLE);
            mHolder.moreView.setVisibility(View.GONE);
            mHolder.moreTwo.setText(context.getResources().getString(R.string.show_item));
        }else{
            mHolder.mainViwe.setVisibility(View.VISIBLE);
            mHolder.hintTv.setVisibility(View.GONE);
            mHolder.moreView.setVisibility(View.GONE);
            mHolder.moreTwo.setText(context.getResources().getString(R.string.hint_item));
        }

        float rating = Double.valueOf(item.getRating()).floatValue();
        if(rating >= 4){
            mHolder.ratingBarF.setRating(rating);
            mHolder.ratingBarF.setVisibility(View.VISIBLE);
            mHolder.ratingBar.setVisibility(View.GONE);
        }else{
            mHolder.ratingBarF.setVisibility(View.GONE);
            mHolder.ratingBar.setVisibility(View.VISIBLE);
        }
        mHolder.ratingBar.setRating(rating);
        mHolder.tvContent.setText(item.getContent());
        mHolder.tvUser.setText(item.getUser_name());
        if(1 == item.getIs_liked()){//1，已点赞 0， 未点赞
            mHolder.tvLikeIv.setBackgroundResource(R.drawable.icon_zand);
        } else{
            mHolder.tvLikeIv.setBackgroundResource(R.drawable.icon_zan);
        }

        if(0 != item.getReviewed_count()){
            mHolder.tvReview.setText(context.getResources().getString(R.string.re_review)+" "
                    + item.getReviewed_count());
        }

        mHolder.tvLike.setText(context.getResources().getString(R.string.zan)+(item.getLike_count() != null ? " "+item.getLike_count() :""));
        mHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mHolder.moreView.isShown()){
                    mHolder.moreView.setVisibility(View.GONE);
                }else{
                    mHolder.moreView.setVisibility(View.VISIBLE);
                }
            }
        });

        if(itemReviewShow){
            Log.i(TAG,"onBindViewHolder itemReviewShow === "+itemReviewShow);
            mHolder.reviewView.setVisibility(View.VISIBLE);
            mHolder.reviewEtView.setVisibility(View.GONE);
        }

        mHolder.tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mHolder.reviewView.isShown()){
                    mHolder.reviewView.setVisibility(View.GONE);
                }else{
                    mHolder.reviewView.setVisibility(View.VISIBLE);
                    mHolder.reviewEtView.setVisibility(View.VISIBLE);
                    mHolder.reviewEt.setText("");
                }
            }
        });

        mHolder.sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG," sendTv is clicked reviewContent === "
                            + mHolder.reviewEt.getText().toString().trim());
                if("".equals(mHolder.reviewEt.getText().toString().trim())){
                    return;
                }
                addReviewRequest(item.get_id(),item.getItem_id(),mHolder.reviewEt.getText().toString().trim(),position,mHolder.mainViwe);
//                mHolder.reviewEt.setVisibility(View.GONE);
            }
        });

        mHolder.tvLike.setOnClickListener(mListener);
        mHolder.tvLike.setTag(position);
        mHolder.moreOne.setOnClickListener(mListener);
        mHolder.moreOne.setTag(position);
        mHolder.moreTwo.setOnClickListener(mListener);
        mHolder.moreTwo.setTag(position);
        mHolder.tvUser.setOnClickListener(mListener);
        mHolder.tvUser.setTag(position);
        if(null != item.getRereviews() && item.getRereviews().size() > 0){
            Log.i(TAG,"getView  item.getRereviews().size() === " + item.getRereviews().size());
            ReReviewAdapter adapter = new ReReviewAdapter(context,item.getRereviews());
            mHolder.reReviewLv.setAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static abstract class MyClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }
        public abstract void myOnClick(int position, View v);
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        LinearLayout mainViwe;
        RatingBar ratingBar;
        RatingBar ratingBarF;
        ImageView img;
        LinearLayout moreView;
        TextView moreOne;
        TextView moreTwo;
        TextView tvContent;
        TextView tvUser;
        ImageView tvLikeIv;
        TextView tvLike;
        TextView tvReview;
        LinearLayout reviewView;
        EditText reviewEt;
        RelativeLayout reviewEtView;
        TextView sendTv;
        ListView reReviewLv;
        TextView hintTv;

        private RecyclerHolder(View itemView) {
            super(itemView);
            mainViwe = itemView.findViewById(R.id.item_review_view);
            ratingBar = itemView.findViewById(R.id.item_detail_review_rb);
            ratingBarF = itemView.findViewById(R.id.item_detail_review_rb_f);
            img = itemView.findViewById(R.id.item_detail_review_more);
            moreView = itemView.findViewById(R.id.item_more_view);
            moreOne = itemView.findViewById(R.id.item_more_one);
            moreTwo = itemView.findViewById(R.id.item_more_two);
            tvContent = itemView.findViewById(R.id.item_detail_review_content);
            tvUser = itemView.findViewById(R.id.item_detail_review_user_tv);
            tvLikeIv = itemView.findViewById(R.id.item_detail_review_like_iv);
            tvLike = itemView.findViewById(R.id.item_detail_review_like);
            tvReview = itemView.findViewById(R.id.item_detail_review_re);
            reviewView = itemView.findViewById(R.id.item_detail_review_view);
            reviewEt = itemView.findViewById(R.id.item_detail_review_et);
            reviewEtView = itemView.findViewById(R.id.item_detail_review_et_view);
            sendTv = itemView.findViewById(R.id.item_detail_review_send_tv);
            reReviewLv = itemView.findViewById(R.id.item_review_rereview_lv);
            hintTv = itemView.findViewById(R.id.item_review_hint_tv);

        }
    }

    private void addReviewRequest(String id, String itemId , final String content, final int position, final View view){//回复评论
        String url = URLInfo.addReReview;
        Map<String, String> params = new HashMap<>();
        params.put("reviewed_id", id);
        params.put("item_id", itemId);
        params.put("content", content);
        params.put("t","rereview");
        Log.i(TAG,"addReviewRequest url === " + url+"  params == "+params.toString());
        String userId = PreferencesUtils.getString(context, Constant.SP_USER_ID,"");
        Log.i(TAG,"addReviewRequest userId === " + userId );
        if("".equals(userId)){
            Utils.gotoNewAct(context, LoginActivity.class);
        }
        OkHttpUtils.getInstance().okHttpPost(url,userId , params, new RequestCallBack() {
            @Override
            public void onSuccess(String result) {
                if(null != result){
                    Log.i(TAG,"addReviewRequest  result = "+result);
                    if(result.contains("\"status\": \"ok\"")){//手动加评论内容
                        ReReview review = new ReReview();
                        review.setUser_name(PreferencesUtils.getString(context,Constant.SP_USER_NAME,""));
                        review.setContent(content);
                        if(null != list.get(position).getRereviews()){
                            list.get(position).getRereviews().add(review);
                        }else{
                            ArrayList<ReReview> reReviews = new ArrayList<>();
                            reReviews.add(review);
                            list.get(position).setRereviews(reReviews);
                        }
                        itemReviewShow = true;
                        notifyItemChanged(position);
                        Mytoast.show(context,context.getResources().getString(R.string.toast_publish));
                    }
                }
            }

            @Override
            public void onFail() {
//                mWaitDialog.dismiss();
                Toast.makeText(context,"网络网络请求失败！", Toast.LENGTH_LONG).show();
            }

        });

    }

}
