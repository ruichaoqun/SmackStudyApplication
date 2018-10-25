package com.smack.administrator.smackstudyapplication.chat.upload;

/**
 * <p>文件、图片上传监听返回</p>
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
 * 			<td>2018-10-25 09:50</td>
 * 			<td>Rui Chaoqun</td>
 * 			<td>All</td>
 *			<td>Created.</td>
 * 		</tr>
 * </table>
 */
public class AttachmentProgress {
    private final String uuid;              //对应消息uuid
    private final long transferred;         //已上传大小
    private final long total;               //文件总大小

    public AttachmentProgress(String var1, long var2, long var4) {
        this.uuid = var1;
        this.transferred = var2;
        this.total = var4;
    }

    public String getUuid() {
        return this.uuid;
    }

    public long getTransferred() {
        return this.transferred;
    }

    public long getTotal() {
        return this.total;
    }
}
