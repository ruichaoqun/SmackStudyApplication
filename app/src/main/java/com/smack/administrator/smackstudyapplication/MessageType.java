package com.smack.administrator.smackstudyapplication;

import android.support.annotation.StringDef;

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
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
@StringDef({
        MessageType.TYPE_IMAGE,
        MessageType.TYPE_TEXT,
        MessageType.TYPE_VOICE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageType {
    String TYPE_TEXT = "text";
    String TYPE_VOICE = "voice";
    String TYPE_IMAGE = "image";
}
