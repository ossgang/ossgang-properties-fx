/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package io.github.ossgang.properties.fx.util;

import com.google.common.base.Objects;
import io.github.ossgang.properties.core.Property;
import io.github.ossgang.properties.core.Sink;
import io.github.ossgang.properties.core.Sinks;
import io.github.ossgang.properties.core.Source;
import javafx.beans.property.*;


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
