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
 * <td>2018-10-12 11:31</td>
 * <td>${User}</td>
 * <td>All</td>
 * <td>Created.</td>
 * </tr>
 * </table>
 */
public abstract class RequestCallbackWrapper<T> implements RequestCallback<T> {
    public RequestCallbackWrapper() {
    }

    public abstract void onResult(int var1, T var2, Throwable var3);

    public void onSuccess(T var1) {
        this.onResult(200, var1, (Throwable)null);
    }

    public void onFailed(int var1) {
        this.onResult(var1, (T) null, (Throwable)null);
    }

    public void onException(Throwable var1) {
        this.onResult(1000, (T) null, var1);
    }
}

