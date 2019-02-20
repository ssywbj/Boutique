package ying.jie.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import ying.jie.boutique.R;
import ying.jie.entity.PersonInfo;
import ying.jie.util.ToastUtil;

public class PersonInfoAdapter extends
        BasicAdapter<PersonInfo, PersonInfoAdapter.ViewHolder> {
    private Context mContext;

    public PersonInfoAdapter(Context context, List<PersonInfo> dataList) {
        super(context, dataList);
        mContext = context;
    }

    @Override
    protected int getItemViewLayoutId() {
        return R.layout.person_info_aty_adt;
    }

    @Override
    protected ViewHolder loadViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void bindView(int position, PersonInfo personInfo, ViewHolder vHolder) {
        vHolder.imageView.setImageResource(personInfo.getPhotoImageId());

        String[] photoDesc = mContext.getString(personInfo.getPhotoDesc()).split("\n");
        String strBuilder = photoDesc[0].trim();
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(
                strBuilder);
        int countLen = 0;
        if (strBuilder.contains(" ")) {
            String tempStr[] = strBuilder.split(" ");
            for (int i = 0; i < tempStr.length; i++) {
                if (i > 0) {
                    countLen += tempStr[i - 1].length() + 1;
                }
                /**
                 * setSpan (Object what, int start, int end, int flags)：每执行一次就新建一个what对象，
                 * 因为what不能为同一个对象，否则颜色改变的效果只对最后一个字符子串生效
                 */
                switch (i % 3) {
                    case 0:
                        stringBuilder.setSpan(new ForegroundColorSpan(Color.RED),
                                countLen, countLen + 1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//改变某些文字的颜色
                        break;
                    case 1:
                        stringBuilder.setSpan(new ForegroundColorSpan(Color.BLUE),
                                countLen, countLen + 1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        break;
                    case 2:
                        stringBuilder.setSpan(new ForegroundColorSpan(Color.GREEN),
                                countLen, countLen + 1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        break;
                    default:
                        stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, 1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        } else {
            stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), 0, 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        vHolder.textName.setText(stringBuilder);
        vHolder.textName.getPaint().setFakeBoldText(true);//中文字体加粗；英文字体加粗：android:textStyle="true"

        vHolder.textMail.setText(personInfo.getMail());

        /*
         * 为文本添加超链接，必须要在执行setText方法后调用setFocusable(false)方法，
		 * 否则ListView的Item点击事件失效。注：不能在XML文件中通过设置focusable属性为false
		 * 代替setFocusable(false)方法，否则ListView的Item点击事件同样失效
		 */
        //vHolder.textTel.setAutoLinkMask(Linkify.PHONE_NUMBERS);//在代码添加超链接
        vHolder.textTel.setText(mContext.getString(R.string.lv_person_phone) + personInfo.getTel());
        vHolder.textTel.setFocusable(false);

        vHolder.ratingBar.setRating(personInfo.getRating());
        vHolder.textAds.setText(photoDesc[1]);

        final Button btnDownload = vHolder.btnDownload;
        btnDownload.setText(String.valueOf(position) + "__");
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(btnDownload.getText().toString());
            }
        });

    }

    final class ViewHolder {
        ImageView imageView;
        TextView textName, textMail, textTel, textAds;
        RatingBar ratingBar;
        Button btnDownload;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.main_lView_iView);
            textName = (TextView) view.findViewById(R.id.main_lView_textName);
            textMail = (TextView) view.findViewById(R.id.main_lView_mail_content);
            textTel = (TextView) view.findViewById(R.id.main_lView_textPhone);
            ratingBar = (RatingBar) view.findViewById(R.id.main_lView_ratingBar);
            textAds = (TextView) view.findViewById(R.id.main_lView_textAds);
            textAds.setSelected(true);
            btnDownload = (Button) view.findViewById(R.id.main_lView_btnWorks);
        }
    }

}
