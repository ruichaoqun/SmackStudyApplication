package com.smack.administrator.smackstudyapplication.chat;

import com.netease.nimlib.sdk.media.record.RecordType;

import java.io.File;

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
 * <td>2018-10-12 14:39</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public interface IAudioRecordCallback {
    void onRecordReady();

    void onRecordStart(File var1, RecordType var2);

    void onRecordSuccess(File var1, long var2, RecordType var4);

    void onRecordFail();

    void onRecordCancel();

    void onRecordReachedMaxTime(int var1);
}
