package afkt.code.app.basic

import afkt.code.app.AppContext
import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import dev.DevUtils
import dev.engine.DevEngine

/**
 * detail: Base Application
 * @author Ttt
 */
open class BaseApplication : Application(),
    ViewModelStoreOwner {

    // ==========
    // = 静态方法 =
    // ==========

    companion object {

        private lateinit var application: BaseApplication

        fun application(): BaseApplication {
            return application
        }
    }

    // =======================
    // = ViewModelStoreOwner =
    // =======================

    // ViewModelStore
    private lateinit var mAppViewModelStore: ViewModelStore

    override val viewModelStore: ViewModelStore
        get() = mAppViewModelStore

    // ============
    // = override =
    // ============

    override fun onCreate() {
        super.onCreate()

        // ============
        // = DevUtils =
        // ============

        // 初始化工具类 - 可不调用, 在 DevUtils FileProviderDevApp 中已初始化, 无需主动调用
        DevUtils.init(this)
        // DevUtils Debug 开发配置
        DevUtils.debugDevelop(AppContext.TAG)

        // ============
        // = 初始化操作 =
        // ============

        // 初始化
        initialize()
    }

    // ============
    // = 初始化方法 =
    // ============

    /**
     * 统一初始化方法
     */
    private fun initialize() {
        application = this
        // 全局 ViewModel
        mAppViewModelStore = ViewModelStore()
        // 初始化 Engine
        initEngine()
    }

    /**
     * 初始化 Engine
     */
    private fun initEngine() {
        // DevEngine 完整初始化
        DevEngine.completeInitialize(this)
    }
}