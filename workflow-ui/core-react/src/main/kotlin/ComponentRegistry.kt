import react.RBuilder
import react.ReactElement
import react.buildElement
import kotlin.reflect.KClass

interface ComponentFactory {
  fun canHandleRendering(rendering: Any): Boolean
  fun createComponent(rendering: Any): ReactElement
}

abstract class TypedComponentFactory<T : Any>(
  private val clazz: KClass<T>
) : ComponentFactory {
  final override fun canHandleRendering(rendering: Any): Boolean {
    // TODO is this even safe in JS?
    return rendering::class == clazz
  }

  abstract fun RBuilder.createComponent(rendering: T)

  override fun createComponent(rendering: Any): ReactElement {
    return buildElement(RBuilder().apply { createComponent(rendering.unsafeCast<T>()) }) {}
  }
}

inline fun <reified RenderingT : Any> componentFactory(
  crossinline creator: RBuilder.(rendering: RenderingT) -> Unit
): ComponentFactory {
  return object : TypedComponentFactory<RenderingT>(RenderingT::class) {
    override fun RBuilder.createComponent(rendering: RenderingT) {
      creator(rendering)
    }
  }
}

class ComponentRegistry(
  private val factories: List<ComponentFactory> = listOf()
) {
  fun getComponent(rendering: Any): ReactElement {
    console.log("Attempting to find component factory for rendering ${rendering::class.simpleName}")
    return factories.firstOrNull { factory: ComponentFactory ->
      factory.canHandleRendering(rendering)
    }?.createComponent(rendering)
      ?: error(
        "No registry found for rendering ${rendering::class.simpleName}. Have you registered your component registry?"
      )
  }
}
