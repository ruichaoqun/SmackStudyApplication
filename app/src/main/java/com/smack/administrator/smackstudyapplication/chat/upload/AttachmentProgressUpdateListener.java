package com.smack.administrator.smackstudyapplication.chat.upload;


/**
 * <p>上传进度监听</p>
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
 * 			<td>2018-10-25 10:16</td>
 * 			<td>Rui Chaoqun</td>
 * 			<td>All</td>
 *			<td>Created.</td>
 * 		</tr>
 * </table>
 */
public interface AttachmentProgressUpdateListener {
    public void onEvent(AttachmentProgress progress);
}
