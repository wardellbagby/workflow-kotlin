import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.form.mFormControl
import com.ccfraser.muirwik.components.form.mFormGroup
import com.ccfraser.muirwik.components.input.mInput
import com.ccfraser.muirwik.components.input.mInputLabel
import com.squareup.sample.gameworkflow.NewGameScreen
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import org.w3c.dom.HTMLInputElement
import react.useState

@OptIn(WorkflowUiExperimentalApi::class) val NewGameScreen =
  componentFactory<NewGameScreen> { rendering ->
    val (xName, setXName) = useState(rendering.defaultNameX)
    val (oName, setOName) = useState(rendering.defaultNameO)

    mFormGroup {
      mFormControl {
        mInputLabel("Player X")
        mInput(value = xName, onChange = { setXName((it.target as HTMLInputElement).value) })
      }

      mFormControl {
        mInputLabel("Player O")
        mInput(value = oName, onChange = { setOName((it.target as HTMLInputElement).value) })
      }
      mButton("Let's Play!", onClick = { rendering.onStartGame(xName, oName) })
    }
  }
