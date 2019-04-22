/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package io.github.ossgang.properties.fx.util;

import com.google.common.base.Objects;
import io.github.ossgang.properties.core.Property;
import io.github.ossgang.properties.core.Sink;
import io.github.ossgang.properties.core.Sinks;
import io.github.ossgang.properties.core.Source;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


public class JavaFxProperties {
    private JavaFxProperties() {
        throw new UnsupportedOperationException("Static Only!");
    }

    public static <T> Property<T> wrap(ObjectProperty<T> objectProperty) {
        return new JavaFxWrapperProperty<>(objectProperty);
    }

    public static Property<String> wrap(StringProperty stringProperty) {
        return new JavaFxWrapperProperty<>(stringProperty);
    }

    public static Property<Double> wrap(DoubleProperty doubleProperty) {
        return new JavaFxWrapperProperty<>(doubleProperty.asObject());
    }

    public static Property<Integer> wrap(IntegerProperty intProperty) {
        return new JavaFxWrapperProperty<>(intProperty.asObject());
    }

    public static Property<Boolean> wrap(BooleanProperty booleanProperty) {
        return new JavaFxWrapperProperty<>(booleanProperty.asObject());
    }


    public static DoubleProperty doubleProperty(Property<Double> property) {
        System.out.println(property);
        /* just  first try ... might still lead to recursions :-(*/
        SimpleDoubleProperty simpleDoubleProperty = new SimpleDoubleProperty(property.get());
        property.getSource().asStream().subscribe(v -> {
            if (!Objects.equal(v, simpleDoubleProperty.get())) {
                simpleDoubleProperty.set(v);
            }
        });
        simpleDoubleProperty.addListener((prop, oldVal, newVal) -> property.set(newVal.doubleValue()));
        return simpleDoubleProperty;
    }


    private static class JavaFxWrapperProperty<T> implements Property<T> {
        private final Sink<T> updateStream = Sinks.createSink();
        private final javafx.beans.property.Property<T> javaFxProperty;

        public JavaFxWrapperProperty(javafx.beans.property.Property<T> javaFxProperty) {
            this.javaFxProperty = javaFxProperty;
            javaFxProperty.addListener((prop, oldValue, newValue) -> {
                if (!Objects.equal(oldValue, newValue)) {
                    updateStream.push(newValue);
                }
            });
        }

        @Override
        public T get() {
            return javaFxProperty.getValue();
        }

        @Override
        public void set(T value) {
            javaFxProperty.setValue(value);
        }

        @Override
        public Source<T> getSource() {
            return updateStream;
        }

    }

}
