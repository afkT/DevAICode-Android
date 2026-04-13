package afkt.code.app

import afkt.code.app.basic.BaseActivity
import androidx.databinding.ViewDataBinding
import dev.base.core.interfaces.IDevBase
import dev.base.simple.ActivityVMType
import dev.base.simple.contracts.binding.BindingActivityView

/**
 * detail: Base App Activity
 * @author Ttt
 */
open class AppActivity<VDB : ViewDataBinding, VM : AppViewModel> :
    BaseActivity<VDB, VM> {

    // ==========
    // = 构造函数 =
    // ==========

    constructor(
        bindLayoutId: Int,
        bindViewModelId: Int = IDevBase.NONE,
        vmType: ActivityVMType = ActivityVMType.ACTIVITY,
        simple_Init: ((Any) -> Unit)? = null,
        simple_Start: ((Any) -> Unit)? = null,
        simple_PreLoad: ((Any) -> Unit)? = null,
        simple_Agile: ((Any) -> Unit)? = null
    ) : super(
        bindLayoutId, bindViewModelId, vmType,
        simple_Init, simple_Start, simple_PreLoad, simple_Agile
    )

    constructor(
        bindLayoutView: BindingActivityView?,
        bindViewModelId: Int = IDevBase.NONE,
        vmType: ActivityVMType = ActivityVMType.ACTIVITY,
        simple_Init: ((Any) -> Unit)? = null,
        simple_Start: ((Any) -> Unit)? = null,
        simple_PreLoad: ((Any) -> Unit)? = null,
        simple_Agile: ((Any) -> Unit)? = null
    ) : super(
        bindLayoutView, bindViewModelId, vmType,
        simple_Init, simple_Start, simple_PreLoad, simple_Agile
    )
}