import com.squareup.sample.helloworkflow.HelloRendering
import com.squareup.sample.helloworkflow.HelloWorkflow
import kotlinx.browser.document
import kotlinx.html.js.onClickFunction
import react.Props
import react.RBuilder
import react.dom.attrs
import react.dom.h1
import react.dom.render
import react.fc

fun main() {
  render(document.getElementById("app")) {
    child(app)
  }
}

class HelloJSRendering(
  override val message: String,
  override val onClick: () -> Unit
) : ReactComponentRendering, HelloRendering {
  override val componentFactory: RBuilder.() -> Unit = {
    h1 {
      +message
      attrs {
        onClickFunction = {
          console.log("clicking!")
          onClick()
        }
      }
    }
  }
}

//todo debug builds seem to be broken but normal builds work fine
val app = fc<Props> {
  Workflow(
    workflow = HelloWorkflow(renderingFactory = { message, onClick ->
      HelloJSRendering(
        message,
        onClick
      )
    }),
    props = Unit
  )
}
