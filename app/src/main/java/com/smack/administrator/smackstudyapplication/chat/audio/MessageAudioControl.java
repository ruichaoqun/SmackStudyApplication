package com.smack.administrator.smackstudyapplication.chat.audio;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smack.administrator.smackstudyapplication.R;
import com.smack.administrator.smackstudyapplication.XmppConnection;
import com.smack.administrator.smackstudyapplication.dao.AudioMsgAttachment;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;
import com.smack.administrator.smackstudyapplication.dao.MsgStatusEnum;
import com.smack.administrator.smackstudyapplication.util.storage.StorageUtil;
import com.smack.administrator.smackstudyapplication.widget.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

import java.io.File;
import java.util.List;

public class MessageAudioControl extends BaseAudioControl<CustomChatMessage> {
    private static MessageAudioControl mMessageAudioControl = null;

    private boolean mIsNeedPlayNext = false;

    private BaseMultiItemFetchLoadAdapter mAdapter;

    private CustomChatMessage mItem = null;

    private MessageAudioControl(Context context) {
        super(context, true);
    }

    public static MessageAudioControl getInstance(Context context) {
        if (mMessageAudioControl == null) {
            synchronized (MessageAudioControl.class) {
                if (mMessageAudioControl == null) {
                    mMessageAudioControl = new MessageAudioControl(XmppConnection.getInstance().getAppContext());
                }
            }
        }

        return mMessageAudioControl;
    }

    @Override
    protected void setOnPlayListener(Playable playingPlayable, AudioControlListener audioControlListener) {
        this.audioControlListener = audioControlListener;

        BasePlayerListener basePlayerListener = new BasePlayerListener(currentAudioPlayer, playingPlayable) {

            @Override
            public void onInterrupt() {
                if (!checkAudioPlayerValid()) {
                    return;
                }

                super.onInterrupt();
                cancelPlayNext();
            }

            @Override
            public void onError(String error) {
                if (!checkAudioPlayerValid()) {
                    return;
                }

                super.onError(error);
                cancelPlayNext();
            }

            @Override
            public void onCompletion() {
                if (!checkAudioPlayerValid()) {
                    return;
                }

                resetAudioController(listenerPlayingPlayable);

                boolean isLoop = false;
                if (mIsNeedPlayNext) {
                    if (mAdapter != null && mItem != null) {
                        isLoop = playNextAudio(mAdapter, mItem);
                    }
                }

                if (!isLoop) {
                    if (audioControlListener != null) {
                        audioControlListener.onEndPlay(currentPlayable);
                    }

                    playSuffix();
                }
            }
        };

        basePlayerListener.setAudioControlListener(audioControlListener);
        currentAudioPlayer.setOnPlayListener(basePlayerListener);
    }

    @Override
    public CustomChatMessage getPlayingAudio() {
        if (isPlayingAudio() && AudioMessagePlayable.class.isInstance(currentPlayable)) {
            return ((AudioMessagePlayable) currentPlayable).getMessage();
        } else {
            return null;
        }
    }

    @Override
    public void startPlayAudioDelay(
            final long delayMillis,
            final CustomChatMessage message,
            final AudioControlListener audioControlListener, final int audioStreamType) {
        // 如果不存在则下载
        AudioMsgAttachment audioAttachment = new Gson().fromJson(message.getMsgAattachment(),AudioMsgAttachment.class);
        File file = new File(audioAttachment.getFilePath());
        if (!file.exists()) {
//            NIMClient.getService(MsgService.class).downloadAttachment(message, false).setCallback(new RequestCallbackWrapper() {
//                @Override
//                public void onResult(int code, Object result, Throwable exception) {
//                    startPlayAudio(message, audioControlListener, audioStreamType, true, delayMillis);
//                }
//            });
            return;
        }
        startPlayAudio(message, audioControlListener, audioStreamType, true, delayMillis);
    }

    //连续播放时不需要resetOrigAudioStreamType
    private void startPlayAudio(
            CustomChatMessage message,
            AudioControlListener audioControlListener,
            int audioStreamType,
            boolean resetOrigAudioStreamType,
            long delayMillis) {
        if (StorageUtil.isExternalStorageExist()) {
            if (startAudio(new AudioMessagePlayable(message), audioControlListener, audioStreamType, resetOrigAudioStreamType, delayMillis)) {
                // 将未读标识去掉,更新数据库
                if (isUnreadAudioMessage(message)) {
                    message.setMsgStatusEnum(MsgStatusEnum.read);
                    XmppConnection.getInstance().getChatDbManager().updateMessage(message);
                }
            }
        } else {
            Toast.makeText(mContext, R.string.sdcard_not_exist_error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean playNextAudio(BaseMultiItemFetchLoadAdapter tAdapter, CustomChatMessage messageItem) {
        final List<?> list = tAdapter.getData();
        int index = 0;
        int nextIndex = -1;
        //找到当前已经播放的
        for (int i = 0; i < list.size(); ++i) {
            CustomChatMessage item = (CustomChatMessage) list.get(i);
            if (item.equals(messageItem)) {
                index = i;
                break;
            }
        }
        //找到下一个将要播放的
        for (int i = index; i < list.size(); ++i) {
            CustomChatMessage item = (CustomChatMessage) list.get(i);
            CustomChatMessage message = item;
            if (isUnreadAudioMessage(message)) {
                nextIndex = i;
                break;
            }
        }

        if (nextIndex == -1) {
            cancelPlayNext();
            return false;
        }
        CustomChatMessage message = (CustomChatMessage) list.get(nextIndex);
        AudioMsgAttachment attach = new Gson().fromJson(message.getMsgAattachment(),AudioMsgAttachment.class);
        if (mMessageAudioControl != null && attach != null) {
            if (attach.getAttachmentStatus() != 2) {
                cancelPlayNext();
                return false;
            }
            if (message.getMsgStatusEnum() != MsgStatusEnum.read) {
                message.setMsgStatusEnum(MsgStatusEnum.read);
                XmppConnection.getInstance().getChatDbManager().updateMessage(message);
            }
            //不是直接通过点击ViewHolder开始的播放，不设置AudioControlListener
            //notifyDataSetChanged会触发ViewHolder刷新，对应的ViewHolder会把AudioControlListener设置上去
            //连续播放 1.继续使用playingAudioStreamType 2.不需要resetOrigAudioStreamType
            mMessageAudioControl.startPlayAudio(message, null, getCurrentAudioStreamType(), false, 0);
            mItem = (CustomChatMessage) list.get(nextIndex);
            tAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    private void cancelPlayNext() {
        setPlayNext(false, null, null);
    }

    public void setPlayNext(boolean isPlayNext, BaseMultiItemFetchLoadAdapter adapter, CustomChatMessage item) {
        mIsNeedPlayNext = isPlayNext;
        mAdapter = adapter;
        mItem = item;
    }

    public void stopAudio() {
        super.stopAudio();
    }

    public boolean isUnreadAudioMessage(CustomChatMessage message) {
        AudioMsgAttachment msgAttachment = new Gson().fromJson(message.getMsgAattachment(),AudioMsgAttachment.class);
        if ((message.getType() == 2)
                && message.getSendUserName() == XmppConnection.getInstance().getCurrentUserName()
                && msgAttachment.getAttachmentStatus() == 2
                && message.getMsgStatusEnum() != MsgStatusEnum.read) {
            return true;
        } else {
            return false;
        }
    }
}
