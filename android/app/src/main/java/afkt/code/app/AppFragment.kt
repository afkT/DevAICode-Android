package afkt.code.app

import afkt.code.R
import afkt.code.app.basic.BaseFragment
import afkt.code.databinding.BaseTitleBarBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.lihang.ShadowLayout
import dev.base.core.fragCommonSystemBarsPadding
import dev.base.core.fragSetSystemBarsPadding
import dev.base.core.interfaces.IDevBase
import dev.base.core.setSystemBarsPadding
import dev.base.simple.FragmentVMType
import dev.base.simple.contracts.binding.BindingFragmentView
import dev.utils.app.ViewUtils
import dev.utils.common.StringUtils

/**
 * detail: Base App Fragment
 * @author Ttt
 */
open class AppFragment<VDB : ViewDataBinding, VM : AppViewModel> :
    BaseFragment<VDB, VM> {

    // ==========
    // = 构造函数 =
    // ==========

    constructor(
        bindLayoutId: Int,
        bindViewModelId: Int = IDevBase.NONE,
        vmType: FragmentVMType = FragmentVMType.FRAGMENT,
        simple_Init: ((Any) -> Unit)? = null,
        simple_Start: ((Any) -> Unit)? = null,
        simple_PreLoad: ((Any) -> Unit)? = null,
        simple_Agile: ((Any) -> Unit)? = null
    ) : super(
        bindLayoutId, bindViewModelId, vmType,
        simple_Init, simple_Start, simple_PreLoad, simple_Agile
    )

    constructor(
        bindLayoutView: BindingFragmentView,
        bindViewModelId: Int = IDevBase.NONE,
        vmType: FragmentVMType = FragmentVMType.FRAGMENT,
        simple_Init: ((Any) -> Unit)? = null,
        simple_Start: ((Any) -> Unit)? = null,
        simple_PreLoad: ((Any) -> Unit)? = null,
        simple_Agile: ((Any) -> Unit)? = null
    ) : super(
        bindLayoutView, bindViewModelId, vmType,
        simple_Init, simple_Start, simple_PreLoad, simple_Agile
    )

    // ============
    // = override =
    // ============

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        viewModel.intentReader(arguments)
        // 添加 TitleBar 则不设置顶部状态栏边距
        if (isAddTitleBar()) {
            fragSetSystemBarsPadding(paddingTop = false)
            // 创建 TitleBar 骨架 View
            createTitleBarSkeletonView(layoutInflater)
        } else {
            if (isApplyWindowInsets()) {
                // 默认设置顶部状态栏、底部导航栏边距
                fragCommonSystemBarsPadding()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    // ==========
    // = 快捷方法 =
    // ==========

    /**
     * 获取 Title View
     */
    fun titleView(): ShadowLayout? {
        val titleView = contentAssist.titleLinear().getChildAt(0)
        if (titleView is ShadowLayout) return titleView
        return null
    }

    /**
     * 获取 TitleBar
     */
    fun titleBar(): TitleBar? {
        return ViewUtils.findViewById(titleView(), R.id.vid_title)
    }

    // =================
    // = Skeleton View =
    // =================

    /**
     * 创建 TitleBar 骨架 View
     */
    private fun createTitleBarSkeletonView(inflater: LayoutInflater) {
        BaseTitleBarBinding.inflate(inflater).apply {
            // 添加状态栏边距
            root.setSystemBarsPadding(paddingBottom = false)
            // 设置标题
            vidTitle.setTitle(viewModel.intentData.getTitle())
                .setOnTitleBarListener(object : OnTitleBarListener {
                    override fun onLeftClick(titleBar: TitleBar) {
                        findNavController().popBackStack()
                    }
                })
            // 添加标题
            contentAssist.addTitleView(root)
        }
    }

    /**
     * 是否添加 TitleBar
     * @return `true` yes, `false` no
     */
    open fun isAddTitleBar(): Boolean {
        // 存在标题才添加 TitleBar
        return StringUtils.isNotEmpty(viewModel.intentData.getTitle())
    }

    // 是否设置顶部状态栏、底部导航栏边距
    open fun isApplyWindowInsets(): Boolean = true
}