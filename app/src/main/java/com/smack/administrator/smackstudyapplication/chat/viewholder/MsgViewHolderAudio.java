package com.smack.administrator.smackstudyapplication.chat.viewholder;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.XmppConnection;
import com.smack.administrator.smackstudyapplication.chat.audio.MessageAudioControl;
import com.smack.administrator.smackstudyapplication.chat.audio.Playable;
import com.smack.administrator.smackstudyapplication.dao.AudioMsgAttachment;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;
import com.smack.administrator.smackstudyapplication.dao.ImageMsgAttachment;
import com.smack.administrator.smackstudyapplication.dao.MsgStatusEnum;
import com.smack.administrator.smackstudyapplication.util.sys.ScreenUtil;
import com.smack.administrator.smackstudyapplication.util.sys.TimeUtil;
import com.smack.administrator.smackstudyapplication.widget.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * 语音viewholder
 */
public class MsgViewHolderAudio extends MsgViewHolderBase {

    public MsgViewHolderAudio(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    public static final int CLICK_TO_PLAY_AUDIO_DELAY = 500;

    private TextView durationLabel;
    private View containerView;
    private View unreadIndicator;
    private ImageView animationView;

    private MessageAudioControl audioControl;

    @Override
    protected int getContentResId() {
        return R.layout.xmpp_message_item_audio;
    }

    @Override
    protected void inflateContentView() {
        durationLabel = findViewById(R.id.message_item_audio_duration);
        containerView = findViewById(R.id.message_item_audio_container);
        unreadIndicator = findViewById(R.id.message_item_audio_unread_indicator);
        animationView = findViewById(R.id.message_item_audio_playing_animation);
        animationView.setBackgroundResource(0);
        audioControl = MessageAudioControl.getInstance(context);
    }

    @Override
    protected void bindContentView() {
        layoutByDirection();

        refreshStatus();

        controlPlaying();
    }

    @Override
    protected void onItemClick() {
        if (audioControl != null) {
            AudioMsgAttachment msgAttachment = gson.fromJson(message.getMsgAattachment(),AudioMsgAttachment.class);
            if(isReceivedMessage() && TextUtils.isEmpty(msgAttachment.getFilePath())){
                //TODO 调用文件下载
                return;
            }


            if (message.getMsgStatusEnum() != MsgStatusEnum.read) {
                // 将未读标识去掉,更新数据库
                unreadIndicator.setVisibility(View.GONE);
            }

            initPlayAnim(); // 设置语音播放动画

            audioControl.startPlayAudioDelay(CLICK_TO_PLAY_AUDIO_DELAY, message, onPlayListener);

            audioControl.setPlayNext(true, adapter, message);
        }
    }

    private void layoutByDirection() {
        if (isReceivedMessage()) {
            setGravity(animationView, Gravity.LEFT | Gravity.CENTER_VERTICAL);
            setGravity(durationLabel, Gravity.RIGHT | Gravity.CENTER_VERTICAL);

            containerView.setBackgroundResource(R.drawable.nim_message_item_left_selector);
            containerView.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(8), ScreenUtil.dip2px(10), ScreenUtil.dip2px(8));
            animationView.setBackgroundResource(R.drawable.nim_audio_animation_list_left);
            durationLabel.setTextColor(Color.BLACK);

        } else {
            setGravity(animationView, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            setGravity(durationLabel, Gravity.LEFT | Gravity.CENTER_VERTICAL);
            unreadIndicator.setVisibility(View.GONE);

            containerView.setBackgroundResource(R.drawable.nim_message_item_right_selector);
            containerView.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(8), ScreenUtil.dip2px(15), ScreenUtil.dip2px(8));
            animationView.setBackgroundResource(R.drawable.nim_audio_animation_list_right);
            durationLabel.setTextColor(Color.WHITE);
        }
    }

    private void refreshStatus() {// 消息状态
        AudioMsgAttachment attachment = gson.fromJson(message.getMsgAattachment(),AudioMsgAttachment.class);
        MsgStatusEnum status = message.getMsgStatusEnum();
        int attachStatus = attachment.getAttachmentStatus();

        // alert button
        if (TextUtils.isEmpty(attachment.getFilePath())) {
            if (attachStatus == 3 || status == MsgStatusEnum.fail) {
                alertButton.setVisibility(View.VISIBLE);
            } else {
                alertButton.setVisibility(View.GONE);
            }
        }

        // progress bar indicator
        if (status == MsgStatusEnum.sending || attachStatus == 1) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

        // unread indicator
        if (isReceivedMessage()
                && attachStatus == 2
                && status != MsgStatusEnum.read) {
            unreadIndicator.setVisibility(View.VISIBLE);
        } else {
            unreadIndicator.setVisibility(View.GONE);
        }
    }

    private void controlPlaying() {
        final AudioMsgAttachment msgAttachment = gson.fromJson(message.getMsgAattachment(),AudioMsgAttachment.class);
        long duration = msgAttachment.getDuration();
        setAudioBubbleWidth(duration);

        durationLabel.setTag(message.getUuid());

        if (!isMessagePlaying(audioControl, message)) {
            if (audioControl.getAudioControlListener() != null &&
                    audioControl.getAudioControlListener().equals(onPlayListener)) {
                audioControl.changeAudioControlListener(null);
            }

            updateTime(duration);
            stop();
        } else {
            audioControl.changeAudioControlListener(onPlayListener);
            play();
        }
    }

    public static int getAudioMaxEdge() {
        return (int) (0.6 * ScreenUtil.screenMin);
    }

    public static int getAudioMinEdge() {
        return (int) (0.1875 * ScreenUtil.screenMin);
    }

    private void setAudioBubbleWidth(long milliseconds) {
        long seconds = TimeUtil.getSecondsByMilliseconds(milliseconds);

        int currentBubbleWidth = calculateBubbleWidth(seconds, 120);
        ViewGroup.LayoutParams layoutParams = containerView.getLayoutParams();
        layoutParams.width = currentBubbleWidth;
        containerView.setLayoutParams(layoutParams);
    }

    private int calculateBubbleWidth(long seconds, int MAX_TIME) {
        int maxAudioBubbleWidth = getAudioMaxEdge();
        int minAudioBubbleWidth = getAudioMinEdge();

        int currentBubbleWidth;
        if (seconds <= 0) {
            currentBubbleWidth = minAudioBubbleWidth;
        } else if (seconds > 0 && seconds <= MAX_TIME) {
            currentBubbleWidth = (int) ((maxAudioBubbleWidth - minAudioBubbleWidth) * (2.0 / Math.PI)
                    * Math.atan(seconds / 10.0) + minAudioBubbleWidth);
        } else {
            currentBubbleWidth = maxAudioBubbleWidth;
        }

        if (currentBubbleWidth < minAudioBubbleWidth) {
            currentBubbleWidth = minAudioBubbleWidth;
        } else if (currentBubbleWidth > maxAudioBubbleWidth) {
            currentBubbleWidth = maxAudioBubbleWidth;
        }

        return currentBubbleWidth;
    }

    private void updateTime(long milliseconds) {
        long seconds = TimeUtil.getSecondsByMilliseconds(milliseconds);

        if (seconds >= 0) {
            durationLabel.setText(seconds + "\"");
        } else {
            durationLabel.setText("");
        }
    }

    protected boolean isMessagePlaying(MessageAudioControl audioControl, CustomChatMessage message) {
        if (audioControl.getPlayingAudio() != null && audioControl.getPlayingAudio().isTheSame(message)) {
            return true;
        } else {
            return false;
        }
    }

    private MessageAudioControl.AudioControlListener onPlayListener = new MessageAudioControl.AudioControlListener() {

        @Override
        public void updatePlayingProgress(Playable playable, long curPosition) {
            if (!isTheSame(message.getUuid())) {
                return;
            }

            if (curPosition > playable.getDuration()) {
                return;
            }
            updateTime(curPosition);
        }

        @Override
        public void onAudioControllerReady(Playable playable) {
            if (!isTheSame(message.getUuid())) {
                return;
            }

            play();
        }

        @Override
        public void onEndPlay(Playable playable) {
            if (!isTheSame(message.getUuid())) {
                return;
            }

            updateTime(playable.getDuration());

            stop();
        }


    };

    private void play() {
        if (animationView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) animationView.getBackground();
            animation.start();
        }
    }

    private void stop() {
        if (animationView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) animationView.getBackground();
            animation.stop();

            endPlayAnim();
        }
    }

    private void initPlayAnim() {
        if (isReceivedMessage()) {
            animationView.setBackgroundResource(R.drawable.nim_audio_animation_list_left);
        } else {
            animationView.setBackgroundResource(R.drawable.nim_audio_animation_list_right);
        }
    }

    private void endPlayAnim() {
        if (isReceivedMessage()) {
            animationView.setBackgroundResource(R.mipmap.nim_audio_animation_list_left_3);
        } else {
            animationView.setBackgroundResource(R.mipmap.nim_audio_animation_list_right_3);
        }
    }

    private boolean isTheSame(String uuid) {
        String current = durationLabel.getTag().toString();
        return !TextUtils.isEmpty(uuid) && uuid.equals(current);
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }
}
