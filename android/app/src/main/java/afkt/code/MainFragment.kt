package afkt.code

import afkt.code.app.AppFragment
import afkt.code.app.AppViewModel
import afkt.code.databinding.MainFragmentBinding

class MainFragment : AppFragment<MainFragmentBinding, MainViewModel>(
    R.layout.main_fragment, BR.viewModel
)

/**
 * detail: Main Fragment ViewModel
 * @author Ttt
 * 示例项目，避免目录 ViewModel 文件过多，统一将对应的 ViewModel 放到对应 Fragment.kt 中
 */
class MainViewModel : AppViewModel()