package com.smack.administrator.smackstudyapplication;

import android.app.Application;
import android.view.WindowManager;

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
 * <td>2018-09-25 18:11</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public class DensityUtil {
    public static int dp2px(float dpValue){
        float scale = .getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

}
