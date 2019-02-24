package jie.example.boutique.receiver;

import java.util.List;

import jie.example.boutique.BoutiqueActivity;
import jie.example.manager.ActivityCollector;
import jie.example.utils.LogUtil;
import jie.example.utils.ToastUtil;
import android.content.Context;
import android.content.Intent;

import com.baidu.android.pushservice.PushMessageReceiver;

public class BoutiquePushReceiver extends PushMessageReceiver {
	private static final String TAG = BoutiquePushReceiver.class
			.getSimpleName();

	/**
	 * 调用PushManager.startWork后，sdk将异步对push server发起绑定请求，绑定请求的结果通过onBind返回。
	 * 如果需要用单播推送，需要把这里获取的channel id和user id上传到应用server中，再调用server接口用channel
	 * id和user id给单个手机或者用户推送。
	 * 
	 * errorCode：绑定接口返回值，0表示绑定成功；appid： 应用id，errorCode非0时为null；userId：应用 user
	 * id，errorCode非0时为null；channelId：应用channel
	 * id，errorCode非0时为null；requestId：向服务端发起的请求id，在追查问题时有用
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
	}

	/**
	 * 接收通知到达的函数。title：推送的通知的标题；description：推送的通知的描述；customContent：自定义内容，
	 * 为空或者json字符串
	 */
	@Override
	public void onNotificationArrived(Context context, String title,
			String description, String customContent) {
		LogUtil.i(TAG, "onNotificationArrived(" + title + ", " + description
				+ ", " + customContent + ")");
	}

	/**
	 * 接收通知点击的函数。title：推送的通知的标题；description：推送的通知的描述；customContent：自定义内容，
	 * 为空或者json字符串
	 */
	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContent) {
		ToastUtil.showToast(title);
		ActivityCollector.finishAllActivity();// 启动登录界面前，先销毁所有的Activity
		Intent intent = new Intent(context, BoutiqueActivity.class);
		// 因为是在广播接收者启动Activity，所以要加入FLAG_ACTIVITY_NEW_TASK，不然会报错
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 接收透传消息。 message：推送的消息 ；customContent：自定义内容，为空或者json字符串
	 */
	@Override
	public void onMessage(Context context, String message, String customContent) {
		LogUtil.i(TAG, "onMessage(" + message + ", " + customContent + ")");
	}

	/**
	 * setTags()的回调函数。errorCode：错误码，0表示某些tag已经设置成功，非0表示所有tag的设置均失败；successTags：
	 * 设置成功的tag；failTags：设置失败的tag；requestId：分配给对云推送的请求的id
	 */
	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> successTags, List<String> failTags, String requestId) {
	}

	/**
	 * delTags()的回调函数。errorCode：错误码，0表示某些tag已经删除成功，非0表示所有的tag均删除失败；successTags：
	 * 成功删除的tag；failTags：删除失败的tag；requestId：分配给对云推送的请求的id
	 */
	@Override
	public void onDelTags(Context arg0, int errorCode,
			List<String> successTags, List<String> failTags, String requestId) {
	}

	/**
	 * listTags()的回调函数。errorCode：错误码，0表示列举tag成功，非0表示失败；tags：
	 * 当前应用所有的tag；requestId：分配给对云推送的请求的id
	 */
	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId) {
	}

	/**
	 * PushManager.stopWork()的回调函数。errorCode：错误码，0表示从云推送解绑成功，非0表示失败；requestId：
	 * 分配给对云推送的请求的id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
	}

}
