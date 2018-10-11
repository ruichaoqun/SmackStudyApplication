package com.smack.administrator.smackstudyapplication.chat.viewholder;

import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 消息项展示ViewHolder工厂类。
 */
public class MsgViewHolderFactory {

//    private static HashMap<Class<? extends MsgAttachment>, Class<? extends MsgViewHolderBase>> viewHolders = new HashMap<>();

    private static Class<? extends MsgViewHolderBase> tipMsgViewHolder;

    static {
        // built in
//        register(ImageAttachment.class, MsgViewHolderPicture.class);
//        register(AudioAttachment.class, MsgViewHolderAudio.class);
//        register(VideoAttachment.class, MsgViewHolderVideo.class);
//        register(LocationAttachment.class, MsgViewHolderLocation.class);
//        register(NotificationAttachment.class, MsgViewHolderNotification.class);
//        register(RobotAttachment.class, MsgViewHolderRobot.class);
    }

//    public static void register(Class<? extends MsgAttachment> attach, Class<? extends MsgViewHolderBase> viewHolder) {
//        viewHolders.put(attach, viewHolder);
//    }

    public static void registerTipMsgViewHolder(Class<? extends MsgViewHolderBase> viewHolder) {
        tipMsgViewHolder = viewHolder;
    }

    public static Class<? extends MsgViewHolderBase> getViewHolderByType(CustomChatMessage message) {
        switch (message.getType()){
            case "1:":
                return MsgViewHolderText.class;
            case "2":
            case "3":
                default:
        }
//        if (message.getType() == 1) {
//            return MsgViewHolderText.class;
//        } else if (message.getMsgType() == MsgTypeEnum.tip) {
//            return tipMsgViewHolder == null ? MsgViewHolderUnknown.class : tipMsgViewHolder;
//        } else {
//            Class<? extends MsgViewHolderBase> viewHolder = null;
//            if (message.getAttachment() != null) {
//                Class<? extends MsgAttachment> clazz = message.getAttachment().getClass();
//                while (viewHolder == null && clazz != null) {
//                    viewHolder = viewHolders.get(clazz);
//                    if (viewHolder == null) {
//                        clazz = getSuperClass(clazz);
//                    }
//                }
//            }
//            return viewHolder == null ? MsgViewHolderUnknown.class : viewHolder;
//        }
        return null;
    }

//    private static Class<? extends MsgAttachment> getSuperClass(Class<? extends MsgAttachment> derived) {
//        Class sup = derived.getSuperclass();
//        if (sup != null && MsgAttachment.class.isAssignableFrom(sup)) {
//            return sup;
//        } else {
//            for (Class itf : derived.getInterfaces()) {
//                if (MsgAttachment.class.isAssignableFrom(itf)) {
//                    return itf;
//                }
//            }
//        }
//        return null;
//    }

    public static List<Class<? extends MsgViewHolderBase>> getAllViewHolders() {
        List<Class<? extends MsgViewHolderBase>> list = new ArrayList<>();
//        list.addAll(viewHolders.values());
        if (tipMsgViewHolder != null) {
            list.add(tipMsgViewHolder);
        }
        list.add(MsgViewHolderUnknown.class);
        list.add(MsgViewHolderText.class);

        return list;
    }
}
