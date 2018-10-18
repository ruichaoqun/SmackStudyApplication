package com.smack.administrator.smackstudyapplication.chat.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>Description.</p>
 * <p>
 * <b>Maintenance History</b>:
 * <table>
 * <tr>
 * <th>Date</th>
 * <th>Developer</th>
 * <th>Target</th>
 * <th>Content</th>
 * </tr>
 * <tr>
 * <td>2018-09-27 17:22</td>
 * <td>rui chaoqun</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
@IntDef({
        MessageType.TYPE_IMAGE,
        MessageType.TYPE_TEXT,
        MessageType.TYPE_VOICE,
        MessageType.TYPE_LOCATION,
        MessageType.TYPE_VEDIO,
        MessageType.TYPE_FILE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageType {
     //1.文字类型 2.语音类型 3.图片类型 4.位置 5.视频 6.文件
    int TYPE_TEXT = 1;
    int TYPE_VOICE = 2;
    int TYPE_IMAGE = 3;
    int TYPE_LOCATION = 4;
    int TYPE_VEDIO = 5;
    int TYPE_FILE = 6;
}
