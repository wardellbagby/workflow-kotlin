import com.squareup.sample.helloworkflow.HelloRendering
import com.squareup.sample.helloworkflow.HelloWorkflow
import kotlinx.browser.document
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.ReactElement
import react.dom.h1
import react.dom.render

fun main() {
  render(document.getElementById("app")) {
    app.invoke {
      attrs {
        workflowProps = Unit
        workflow = HelloWorkflow(::HelloJSRendering)
      }
    }
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

val app = workflow<Unit, Nothing, HelloJSRendering>("app")


