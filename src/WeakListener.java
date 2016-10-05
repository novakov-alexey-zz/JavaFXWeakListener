import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.control.Button;

import java.util.LinkedList;
import java.util.List;

public interface WeakListener {
  default ChangeListener<Object> weak(ChangeListener<Object> changeListener) {
    listenerRefs().add(changeListener);
    return new WeakChangeListener<>(changeListener);
  }

  default EventHandler<Event> weak(EventHandler<Event> eventHandler) {
    listenerRefs().add(eventHandler);
    return new WeakEventHandler<>(eventHandler);
  }

  List<Object> listenerRefs();
}

class BaseView implements WeakListener {
  private List<Object> listenerRefs = new LinkedList<>();

  @Override
  public List<Object> listenerRefs() {
    return listenerRefs;
  }
}

class MyView extends BaseView {
  public void buildUi() {
    ObjectProperty<Boolean> property = new SimpleObjectProperty<>();
    property.addListener(weak((observable, oldValue, newValue) -> {
      System.out.println("property listener call");
    }));

    Button button = new Button("My button");
    button.addEventHandler(EventType.ROOT, weak(event -> {
      System.out.println("event handler call");
    }));
  }
}
