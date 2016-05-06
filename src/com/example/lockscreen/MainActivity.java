package com.example.lockscreen;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);// 获取设备策略服务
		mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);// 设备管理组件
		if(mDPM.isAdminActive(mDeviceAdminSample)) {
			lockScreen(new Button(this));
			finish();
		}else {
			activeAdmin(new Button(this));
		}
		// mDPM.lockNow();// 立即锁屏
		// finish();
	}

	// 激活设备管理器, 也可以在设置->安全->设备管理器中手动激活
	public void activeAdmin(View view) {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "哈哈哈, 我们有了超级设备管理器, 好NB!");
		startActivity(intent);
	}

	// 一键锁屏
	public void lockScreen(View view) {
		if (mDPM.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
			mDPM.lockNow();// 立即锁屏
			// mDPM.resetPassword("", 0);
		} else {
			Toast.makeText(this, "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
		}
	}

	public void clearData(View view) {
		if (mDPM.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
			mDPM.wipeData(0);// 清除数据,恢复出厂设置
		} else {
			Toast.makeText(this, "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
		}
	}

	public void unInstall(View view) {
		if(mDPM.isAdminActive(mDeviceAdminSample)) {
			lockScreen(new Button(this));
		}
		mDPM.removeActiveAdmin(mDeviceAdminSample);// 取消激活

		// 卸载程序
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivity(intent);
	}
}
