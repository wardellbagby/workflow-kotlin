package android.app;

import android.annotation.SuppressLint;
import android.content.DialogInterface.OnShowListener;
import android.os.Message;
import androidx.annotation.Nullable;
import java.lang.reflect.Field;

public final class Dialogs {
  /**
   * Missing getter counterpart to {@link Dialog#setOnShowListener}, so that
   * we can use that hook w/o stomping client code. Uses Java reflection to
   * avoid pulling in heavy weight Kotlin reflection.
   */
  public static @Nullable OnShowListener getOnShowListener(Dialog dialog) {
    try {
      @SuppressWarnings("JavaReflectionMemberAccess")
      @SuppressLint("DiscouragedPrivateApi")
      Field field = Dialog.class.getDeclaredField("mShowMessage");
      field.setAccessible(true);
      Message message = (Message) field.get(dialog);
      if (message == null) return null;
      return (OnShowListener) message.obj;
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
