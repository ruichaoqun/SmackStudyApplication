package com.smack.administrator.smackstudyapplication;

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
 * <td>2018-09-26 16:39</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class CustomMessage {
    private String type;                // 消息类型 1.文字类型 2.语音类型 3.图片类型
    private String text;                // 文本消息
    private String imagePath;           // 图片地址
    private String filePath;            // 语音地址
    private String sendDate;            // 发送日期
    private String recieveDate;         // 接收日期
    private String sendUserId;          // 发送发id
    private String recieveUserId;       // 接收方id

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getRecieveDate() {
        return recieveDate;
    }

    public void setRecieveDate(String recieveDate) {
        this.recieveDate = recieveDate;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getRecieveUserId() {
        return recieveUserId;
    }

    public void setRecieveUserId(String recieveUserId) {
        this.recieveUserId = recieveUserId;
    }
}
