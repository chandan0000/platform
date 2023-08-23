package com.example.platindepend

// import io.flutter.embedding.android.FlutterActivity
// import io.flutter.embedding.engine.FlutterEngine
// import io.flutter.plugin.common.MethodChannel

// import androidx.annotation.NonNull
// import android.content.Context
// import android.content.ContextWrapper
// import android.content.Intent
// import android.content.IntentFilter
// import android.os.BatteryManager
// import android.os.Build.VERSION
// import android.os.Build.VERSION_CODES
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.TelephonyManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel


class MainActivity: FlutterActivity() {

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val callReceiver = object : EventChannel.StreamHandler, BroadcastReceiver() {
            var eventSink: EventChannel.EventSink? = null
            
            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                eventSink = events
            }

            override fun onCancel(arguments: Any?) {
                eventSink = null
            }

            override fun onReceive(p0: Context?, p1: Intent?) {
                val state: String? = p1?.getStringExtra(TelephonyManager.EXTRA_STATE)
                val incomingNumber: String? =
                    p1?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

                if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                    // if call 
                    eventSink?.success("$incomingNumber")
                }
            }
        }

        registerReceiver(callReceiver, IntentFilter("android.intent.action.PHONE_STATE"))
        EventChannel(flutterEngine.dartExecutor.binaryMessenger, "br.com.app/callStream")
            .setStreamHandler(callReceiver)
    }


    // private val CHANNEL = "samples.flutter.dev/battery"

    // override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
    //     super.configureFlutterEngine(flutterEngine)
    //     MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
    //         call, result ->
    //         if (call.method == "getBatteryLevel") {
    //         val batteryLevel = getBatteryLevel()

    //         if (batteryLevel != -1) {
    //             result.success(batteryLevel)
    //         } else {
    //             result.error("UNAVAILABLE", "Battery level not available.", null)
    //         }
    //     } else {
    //         result.notImplemented()
    //     }

    //     }
    // }
    // private fun getBatteryLevel(): Int {
    //     val batteryLevel: Int
    //     if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
    //         val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    //         batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    //     } else {
    //         val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    //         batteryLevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
    //     }

    //     return batteryLevel
    // }


}
