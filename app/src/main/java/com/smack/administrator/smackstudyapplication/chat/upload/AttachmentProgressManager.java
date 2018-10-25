package com.smack.administrator.smackstudyapplication.chat.upload;

import com.google.gson.Gson;
import com.smack.administrator.smackstudyapplication.dao.CustomChatMessage;
import com.smack.administrator.smackstudyapplication.dao.ImageMsgAttachment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <p>文件上传管理器</p>
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
 * 			<td>2018-10-25 09:47</td>
 * 			<td>Rui Chaoqun</td>
 * 			<td>All</td>
 *			<td>Created.</td>
 * 		</tr>
 * </table>
 */
public class AttachmentProgressManager {
    private AttachmentProgressUpdateListener listener;

    public AttachmentProgressManager(AttachmentProgressUpdateListener listener) {
        this.listener = listener;
    }

    public void uploadAttachment(CustomChatMessage chatMessage,final String uuid){
        //TODO 模拟文件上传
        final int count = 0;
        Observable.intervalRange(1,100,0,200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    private Disposable mDisposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if(listener != null ){
                            AttachmentProgress progress = new AttachmentProgress(uuid,aLong,100);
                            listener.onEvent(progress);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(!mDisposable.isDisposed()){
                            mDisposable.dispose();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
