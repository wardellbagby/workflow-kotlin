import com.squareup.sample.helloworkflow.HelloRendering
import com.squareup.sample.helloworkflow.HelloWorkflow
import kotlinx.browser.document
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.ReactElement
import react.child
import react.dom.h1
import react.dom.render
import react.functionalComponent

fun main() {
  render(document.getElementById("app")) {
    child(app)
  }
}

class HelloJSRendering(
  override val message: String,
  override val onClick: () -> Unit
) : ReactComponentRendering, HelloRendering {
  override val componentFactory: RBuilder.() -> ReactElement = {
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

val app = functionalComponent<RProps> {
  Workflow(
    workflow = HelloWorkflow(renderingFactory = ::HelloJSRendering),
    props = Unit
  )
}


