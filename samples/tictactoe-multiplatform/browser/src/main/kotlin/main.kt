import com.ccfraser.muirwik.components.mCssBaseline
import com.squareup.sample.authworkflow.RealAuthService
import com.squareup.sample.authworkflow.RealAuthWorkflow
import com.squareup.sample.gameworkflow.RealGameLog
import com.squareup.sample.gameworkflow.RealRunGameWorkflow
import com.squareup.sample.gameworkflow.RealTakeTurnsWorkflow
import com.squareup.sample.mainworkflow.TicTacToeWorkflow
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import react.PropsWithChildren
import react.dom.render
import react.fc

fun main() {
  render(document.getElementById("app")) {
    child(app)
  }
}

//TODO what should coroutines scope look like here?
val app = fc<PropsWithChildren> {
  mCssBaseline()
  Workflow(
    workflow = TicTacToeWorkflow(
      RealAuthWorkflow(RealAuthService(GlobalScope)),
      RealRunGameWorkflow(RealTakeTurnsWorkflow(), RealGameLog())
    ),
    props = Unit,
    componentRegistry = ComponentRegistry(
      listOf(
        AlertContainerScreen,
        PanelContainerScreen,
        BackStackScreen,
        ScrimContainerScreen,
        LoginScreen,
        GamePlayScreen,
        AuthorizingScreen,
        NewGameScreen,
        GameOverScreen
      )
    )
  )
}


