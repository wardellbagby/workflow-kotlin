package com.squareup.workflow1

import okio.Buffer
import okio.ByteString
import okio.EOFException
import kotlin.reflect.KClass
import kotlin.reflect.KType

public actual class WorkflowIdentifier internal constructor(
  private val typeName: String,
  private val isKClass: Boolean,
  private val proxiedIdentifier: WorkflowIdentifier? = null,
  private val description: (() -> String?)? = null
) {

  private val proxiedIdentifiers = generateSequence(this) { it.proxiedIdentifier }
  public actual companion object {
    private const val NO_PROXY_IDENTIFIER_TAG = 0.toByte()
    private const val PROXY_IDENTIFIER_TAG = 1.toByte()

    actual fun parse(bytes: ByteString): WorkflowIdentifier = Buffer().let { source ->
      source.write(bytes)

      try {
        val typeString = source.readUtf8WithLength()
        val proxiedIdentifier = when (source.readByte()) {
          NO_PROXY_IDENTIFIER_TAG -> null
          PROXY_IDENTIFIER_TAG -> parse(source.readByteString())
          else -> throw IllegalArgumentException("Invalid WorkflowIdentifier")
        }
        val isKClass: Boolean = source.readBooleanFromInt()

        return WorkflowIdentifier(typeString, isKClass, proxiedIdentifier)
      } catch (e: EOFException) {
        throw IllegalArgumentException("Invalid WorkflowIdentifier")
      }
    }
  }

  public actual fun toByteStringOrNull(): ByteString? {
    if (!isKClass) {
      return null
    }

    val proxiedBytes = proxiedIdentifier?.let {
      // If we have a proxied identifier but it's not serializable, then we can't be serializable
      // either.
      it.toByteStringOrNull() ?: return null
    }

    return Buffer().let { sink ->
      sink.writeUtf8WithLength(typeName)
      if (proxiedBytes != null) {
        sink.writeByte(PROXY_IDENTIFIER_TAG.toInt())
        sink.write(proxiedBytes)
      } else {
        sink.writeByte(NO_PROXY_IDENTIFIER_TAG.toInt())
      }
      sink.readByteString()
    }
  }

  override fun toString(): String =
    description?.invoke()
      ?: proxiedIdentifiers
        .joinToString { it.typeName }
        .let { "WorkflowIdentifier($it)" }

  override fun equals(other: Any?): Boolean = when {
    this === other -> true
    other !is WorkflowIdentifier -> false
    else -> typeName == other.typeName && proxiedIdentifier == other.proxiedIdentifier
  }

  override fun hashCode(): Int {
    var result = typeName.hashCode()
    result = 31 * result + (proxiedIdentifier?.hashCode() ?: 0)
    return result
  }
}

public actual val Workflow<*, *, *>.identifier: WorkflowIdentifier
  get() {
    val maybeImpostor = this as? ImpostorWorkflow
    return WorkflowIdentifier(
      typeName = this::class.toString().removePrefix("class "),
      isKClass = false,
      proxiedIdentifier = maybeImpostor?.realIdentifier,
      description = maybeImpostor?.let { it::describeRealIdentifier }
    )
  }

public actual fun unsnapshottableIdentifier(type: KType): WorkflowIdentifier =
  WorkflowIdentifier(type.toString(), false)
