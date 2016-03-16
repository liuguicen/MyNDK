package com.example.administrator.myndk;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import  android.app.ActivityManager.RunningServiceInfo;
/**
 * Created by Administrator on 2016/3/16.
 * 里面的wartcher和monitor应当是同一个东西
 * （1）代码中很多属性都是测试时用的，懒得去掉，其实有些都没用到。
 * 只需要关心createAppMonitor这个对外接口就可以了，它要求传入一个当前进程的用户ID，
 * 然后会调用createWatcher本地方法来创建守护进程。
 * （2）还有两个方法connectToMonitor用于创建和监视进程的socket通道，
 * sendMsgToMonitor用于通过socket向子进程发送数据。
 * 由于暂时不需要和子进程 * 进行数据交互，所以这两个方法就没有添加对外的JAVA接口，但是要添加简直是轻而易举的事！
 */
public class Watcher {
    //TODO Fix this according to your service
//    private static final String PACKAGE = "com.example.dameonservice/";
    private static final String PACKGE = "com.example.administrator.myndk";
    //    private String mMonitoredService = "";
    /**
     * 被监视的进程中服务的名字
     */
    private String mMonitoredService = "";
    private volatile boolean bHeartBreak = false;
    private Context mContext;
    private boolean mRunning = true;

    /**
     * 创建一个监听的进程
     *
     * @param userId
     */
    public void createAppMonitor(String userId) {
        if (!createWatcher(userId))//创建监听进程失败
        {
            Log.e("Watcher", "<<Monitor created failed>>");
        }
    }

    //构造函数
    public Watcher(Context context) {
        mContext = context;
    }

    //判断服务是否正在运行
    private int isServiceRunning() {
        ActivityManager am = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo>
                runningService = (ArrayList<RunningServiceInfo>)
                am.getRunningServices(1024);
        for (int i = 0; i < runningService.size(); ++i) {
            if (mMonitoredService.equals(runningService.get(i).
                    service.getClassName().toString())) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Native方法，创建一个监视子进程.
     *
     * @param userId 当前进程的用户ID,子进程重启当前进程时需要用到当前进程的用户ID.
     * @return 如果子进程创建成功返回true，否则返回false
     */
    private native boolean createWatcher(String userId);

    /**
     * Native方法，让当前进程连接到监视进程.
     *
     * @return 连接成功返回true，否则返回false
     */
    private native boolean connectToMonitor();

    /**
     * Native方法，向监视进程发送任意信息
     *
     * @param  msg 发给monitor的信息
     * @return 实际发送的字节
     */
    private native int sendMsgToMonitor(String msg);

    static {
        System.loadLibrary("monitor");
    }
}
