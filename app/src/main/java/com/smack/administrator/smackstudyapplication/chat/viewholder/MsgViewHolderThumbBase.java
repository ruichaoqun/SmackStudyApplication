package com.smack.administrator.smackstudyapplication.chat.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.smack.administrator.smackstudyapplication.chat.data.MessageType;
import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.dao.ImageMsgAttachment;
import com.smack.administrator.smackstudyapplication.dao.MsgStatusEnum;
import com.smack.administrator.smackstudyapplication.util.BitmapDecoder;
import com.smack.administrator.smackstudyapplication.util.ImageUtil;
import com.smack.administrator.smackstudyapplication.util.StringUtil;
import com.smack.administrator.smackstudyapplication.util.sys.ScreenUtil;
import com.smack.administrator.smackstudyapplication.widget.imageview.MsgThumbImageView;
import com.smack.administrator.smackstudyapplication.widget.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

import java.io.File;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public abstract class MsgViewHolderThumbBase extends MsgViewHolderBase {

    public MsgViewHolderThumbBase(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    protected MsgThumbImageView thumbnail;
    protected View progressCover;
    protected TextView progressLabel;
    protected ImageMsgAttachment msgAttachment;

    @Override
    protected void inflateContentView() {
        thumbnail = findViewById(R.id.message_item_thumb_thumbnail);
        progressBar = findViewById(R.id.message_item_thumb_progress_bar); // 覆盖掉
        progressCover = findViewById(R.id.message_item_thumb_progress_cover);
        progressLabel = findViewById(R.id.message_item_thumb_progress_text);
    }

    @Override
    protected void bindContentView() {
        msgAttachment =  gson.fromJson(message.getMsgAattachment(),ImageMsgAttachment.class);
        String path = msgAttachment.getPath();
        String url = msgAttachment.getUrl();
        if(msgAttachment != null){
            setImageSize(path);
            if(!isReceivedMessage()){
                if (path != null) {
                    //thumbnail.loadAsPath(thumbPath, getImageMaxEdge(), getImageMaxEdge(), maskBg());
                    thumbnail.loadAsPath(path, getImageMaxEdge(), getImageMaxEdge(), maskBg(), msgAttachment.getExtension());
                } else {
                    thumbnail.loadAsResource(R.mipmap.xmpp_image_default, maskBg());
                }
            }else{
                if (url != null) {
                    thumbnail.loadAdUrl(path, getImageMaxEdge(), getImageMaxEdge(), maskBg(), msgAttachment.getExtension());
                } else {
                    thumbnail.loadAsResource(R.mipmap.xmpp_image_default, maskBg());
                }
            }
        }
        refreshStatus();
    }

    private void refreshStatus() {
        if (TextUtils.isEmpty(msgAttachment.getPath())) {
            if (message.getMsgStatusEnum() == MsgStatusEnum.fail ) {
                alertButton.setVisibility(View.VISIBLE);
            } else {
                alertButton.setVisibility(View.GONE);
            }
        }

        if (message.getMsgStatusEnum() == MsgStatusEnum.sending) {
            progressCover.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressLabel.setVisibility(View.VISIBLE);
            progressLabel.setText(StringUtil.getPercentString(getMsgAdapter().getProgress(message)));
        } else {
            progressCover.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            progressLabel.setVisibility(View.GONE);
        }
    }

    private void setImageSize(String thumbPath) {
        int[] bounds = null;
        if (thumbPath != null) {
            bounds = BitmapDecoder.decodeBound(new File(thumbPath));
        }
        if (bounds == null) {
            if (message.getType() == MessageType.TYPE_IMAGE) {
                bounds = new int[]{msgAttachment.getWidth(), msgAttachment.getHeight()};
            }
        }

        if (bounds != null) {
            ImageUtil.ImageSize imageSize = ImageUtil.getThumbnailDisplaySize(bounds[0], bounds[1], getImageMaxEdge(), getImageMinEdge());
            setLayoutParams(imageSize.width, imageSize.height, thumbnail);
        }
    }

    private int maskBg() {
        return R.drawable.nim_message_item_round_bg;
    }

    public static int getImageMaxEdge() {
        return (int) (165.0 / 320.0 * ScreenUtil.screenWidth);
    }

    public static int getImageMinEdge() {
        return (int) (76.0 / 320.0 * ScreenUtil.screenWidth);
    }

    protected abstract String thumbFromSourceFile(String path);
}
