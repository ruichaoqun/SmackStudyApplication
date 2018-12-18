package com.smack.administrator.smackstudyapplication.chat.data;

/**
 * <p>Description.</p>
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
 * 			<td>2018-11-06 16:10</td>
 * 			<td>Rui Chaoqun</td>
 * 			<td>All</td>
 *			<td>Created.</td>
 * 		</tr>
 * </table>
 */
public interface AttachmentStatus {
    //1.文字类型 2.语音类型 3.图片类型 4.位置 5.视频 6.文件
    int draf = 1;
    int TYPE_VOICE = 2;
    int TYPE_IMAGE = 3;
}
