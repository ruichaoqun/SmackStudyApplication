package com.smack.administrator.smackstudyapplication.dao;

import com.lzy.imagepicker.bean.ImageItem;

/**
 * <p>图片消息附件</p>
 *
 * <b>Maintenance History</b>:
 * <table>
 * 		<tr>
 * 			<th>Date</th>
 * 			<th>Developer</th>
 * 			<th>Target</th>
 * 			<th>Content</th>
 * 		</tr>
 * 		<tr>
 * 			<td>2018-10-24 10:15</td>
 * 			<td>Rui Chaoqun</td>
 * 			<td>All</td>
 *			<td>Created.</td>
 * 		</tr>
 * </table>
 */
public class ImageMsgAttachment {
    public String name;       //图片的名字
    public String path;       //图片的路径
    private String url;       //图片url（用于接收者展示图片）
    public long size;         //图片的大小
    public int width;         //图片的宽度
    public int height;        //图片的高度
    public String mimeType;   //图片的类型
    public String extension;  //图片类型

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public static ImageMsgAttachment createImageMsgAttachment(ImageItem item){
        ImageMsgAttachment attachment = new ImageMsgAttachment();
        attachment.setPath(item.path);
        attachment.setWidth(item.width);
        attachment.setHeight(item.height);
        attachment.setSize(item.size);
        attachment.setName(item.name);
        attachment.setMimeType(item.mimeType);
        String[] strings = item.name.split(".");
        if(strings != null && strings.length > 1){
            attachment.setExtension(strings[1]);
        }
        return attachment;
    }
}
