/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.http.internal.transform;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.transform.TransformationService;

/**
 * The {@link CascadedValueTransformationImpl} implements {@link ValueTransformation for a cascaded set of
 * transformations}
 *
 * @author Jan N. Klug - Initial contribution
 */
@NonNullByDefault
public class CascadedValueTransformationImpl implements ValueTransformation {
    private final List<ValueTransformation> transformations;

    public CascadedValueTransformationImpl(String transformationString,
            Function<String, @Nullable TransformationService> transformationServiceSupplier) {
        transformations = Arrays.stream(transformationString.split("∩")).filter(s -> s != null && !s.isEmpty())
                .map(transformation -> new SingleValueTransformation(transformation, transformationServiceSupplier))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<String> apply(String value) {
        Optional<String> valueOptional = Optional.of(value);

        // process all transformations
        for (ValueTransformation transformation : transformations) {
            valueOptional = valueOptional.flatMap(transformation::apply);
        }

        return valueOptional;
    }
}
