package com.smack.administrator.smackstudyapplication.dao;

/**
 * <p>语音消息附件</p>
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
 * <td>2018-10-24 10:03</td>
 * <td>Rui Chaoqun</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class AudioMsgAttachment {
    private String filePath;        // 本地语音地址
    private String url;             // 服务器语音地址
    private long duration;          // 语音长度

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
