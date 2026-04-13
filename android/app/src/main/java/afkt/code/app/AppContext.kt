package afkt.code.app

import afkt.code.MainActivity
import afkt.code.app.basic.BaseApplication
import android.content.Context
import android.content.Intent
import android.os.Process
import dev.DevUtils
import dev.base.utils.ViewModelUtils
import dev.utils.app.CrashUtils
import dev.utils.app.CrashUtils.CrashCatchListener
import kotlin.system.exitProcess

/**
 * detail: App Application
 * @author Ttt
 */
class AppContext : BaseApplication() {

    companion object {

        // 日志 TAG
        val TAG = "DevAICode_Log"

        private val viewModel: AppViewModel by lazy {
            ViewModelUtils.getAppViewModel(
                application(),
                AppViewModel::class.java
            )!!
        }

        fun viewModel(): AppViewModel {
            return viewModel
        }

        fun context(): Context {
            return DevUtils.getContext()
        }

        fun context(context: Context?): Context {
            return DevUtils.getContext(context)
        }
    }

    // ===========
    // = override =
    // ===========

    override fun onCreate() {
        super.onCreate()
        // 初始化异常捕获处理
        initCrash()
    }

    /**
     * 初始化异常捕获处理
     */
    private fun initCrash() {
        // 捕获异常处理 => 在 BaseApplication 中调用
        CrashUtils.getInstance().initialize(applicationContext, object : CrashCatchListener {
            override fun handleException(ex: Throwable) {}

            override fun uncaughtException(
                context: Context,
                thread: Thread,
                ex: Throwable
            ) {
                // 重启应用【跳转到首页】
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(intent)
                // 关闭进程
                Process.killProcess(Process.myPid())
                exitProcess(1)
            }
        })
    }
}

/**
 * 获取全局 ViewModel
 * @return Application ViewModel
 */
fun appViewModel(): AppViewModel {
    return AppContext.viewModel()
}