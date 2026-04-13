package afkt.code

import afkt.code.app.AppActivity
import afkt.code.app.AppViewModel
import afkt.code.databinding.MainActivityBinding

class MainActivity : AppActivity<MainActivityBinding, AppViewModel>(
    R.layout.main_activity
)