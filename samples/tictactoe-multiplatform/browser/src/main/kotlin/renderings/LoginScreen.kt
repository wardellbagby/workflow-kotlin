import com.ccfraser.muirwik.components.MGridAlignItems.center
import com.ccfraser.muirwik.components.MGridDirection.column
import com.ccfraser.muirwik.components.MGridJustify
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.direction
import com.ccfraser.muirwik.components.input.mInput
import com.ccfraser.muirwik.components.mGridContainer
import com.squareup.sample.authworkflow.LoginScreen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import org.w3c.dom.HTMLInputElement
import react.dom.div
import react.useState

@OptIn(WorkflowUiExperimentalApi::class) val LoginScreen =
  componentFactory<LoginScreen> { rendering ->
    val (name, setName) = useState("")
    val (password, setPassword) = useState("")

    mGridContainer(alignItems = center, justify = MGridJustify.center) {
      attrs {
        direction = column
      }

      div {
        +rendering.errorMessage
      }
      mInput(
        value = name,
        placeholder = "Username",
        onChange = { setName((it.target as HTMLInputElement).value) })
      mInput(
        value = password,
        placeholder = "Password",
        onChange = { setPassword((it.target as HTMLInputElement).value) })


      mButton("Login", onClick = {
        rendering.onLogin(name, password)
      })
    }
  }

