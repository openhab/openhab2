package org.openhab.binding.dbquery.internal;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

class Value2StateConverterTest {
    private Value2StateConverter instance;

    @BeforeEach
    void setUp() {
        instance = new Value2StateConverter();
    }

    @AfterEach
    void tearDown() {
        instance = null;
    }

    @ParameterizedTest
    @ValueSource(classes = { StringType.class, DecimalType.class, DateTimeType.class, OpenClosedType.class,
            OnOffType.class })
    void given_null_value_return_undef(Class<State> classe) {
        assertThat(instance.convertValue(null, classe), is(UnDefType.NULL));
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "stringValue" })
    void given_string_value_and_string_target_return_stringtype(String value) {
        var converted = instance.convertValue(value, StringType.class);
        assertThat(converted.toFullString(), is(value));
    }

    @ParameterizedTest
    @MethodSource("provideValuesOfAllSupportedResultRowTypesExceptBytes")
    void given_valid_object_types_and_string_target_return_stringtype_with_string(Object value) {
        var converted = instance.convertValue(value, StringType.class);
        assertThat(converted.toFullString(), is(value.toString()));
    }

    @Test
    void given_byte_array_and_string_target_return_encoded_base64() {
        var someBytes = "Hello world".getBytes(Charset.defaultCharset());
        var someBytesB64 = Base64.getEncoder().encodeToString(someBytes);
        var converted = instance.convertValue(someBytes, StringType.class);
        assertThat(converted.toFullString(), is(someBytesB64));
    }

    @ParameterizedTest
    @MethodSource("provideNumericTypes")
    void given_numeric_type_and_decimal_target_return_decimaltype(Number value) {
        var converted = instance.convertValue(value, DecimalType.class);
        assertThat(converted, instanceOf(DecimalType.class));
        assertThat(((DecimalType) converted).doubleValue(), closeTo(value.doubleValue(), 0.01d));
    }

    @ParameterizedTest
    @MethodSource("provideNumericTypes")
    void given_numeric_string_and_decimal_target_return_decimaltype(Number value) {
        var numberString = value.toString();
        var converted = instance.convertValue(numberString, DecimalType.class);
        assertThat(converted, instanceOf(DecimalType.class));
        assertThat(((DecimalType) converted).doubleValue(), closeTo(value.doubleValue(), 0.01d));
    }

    @Test
    void given_duration_and_decimal_target_return_decimaltype_with_milliseconds() {
        var duration = Duration.ofDays(1);
        var converted = instance.convertValue(duration, DecimalType.class);
        assertThat(converted, instanceOf(DecimalType.class));
        assertThat(((DecimalType) converted).longValue(), is(duration.toMillis()));
    }

    @Test
    void given_instant_and_datetime_target_return_datetype() {
        var instant = Instant.now();
        var converted = instance.convertValue(instant, DateTimeType.class);
        assertThat(converted, instanceOf(DateTimeType.class));
        assertThat(((DateTimeType) converted).getZonedDateTime(),
                is(ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()).withFixedOffsetZone()));
    }

    @Test
    void given_date_and_datetime_target_return_datetype() {
        var date = new Date();
        var converted = instance.convertValue(date, DateTimeType.class);
        assertThat(converted, instanceOf(DateTimeType.class));
        assertThat(((DateTimeType) converted).getZonedDateTime(),
                is(ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).withFixedOffsetZone()));
    }

    @ParameterizedTest
    @ValueSource(strings = { "2019-10-12T07:20:50.52Z", "2019-10-12" })
    void given_valid_string_date_and_datetime_target_return_datetype(String date) {
        var converted = instance.convertValue(date, DateTimeType.class);
        assertThat(converted, instanceOf(DateTimeType.class));
        var convertedDateTime = ((DateTimeType) converted).getZonedDateTime();
        assertThat(convertedDateTime.getYear(), is(2019));
        assertThat(convertedDateTime.getMonthValue(), is(10));
        assertThat(convertedDateTime.getDayOfMonth(), is(12));
        assertThat(convertedDateTime.getHour(), anyOf(is(7), is(0)));
    }

    @ParameterizedTest
    @MethodSource("trueValues")
    void given_values_considerated_true_and_on_off_target_return_on(Object value) {
        var converted = instance.convertValue(value, OnOffType.class);
        assertThat(converted, instanceOf(OnOffType.class));
        assertThat(converted, is(OnOffType.ON));
    }

    @ParameterizedTest
    @MethodSource("falseValues")
    void given_values_considerated_false_and_on_off_target_return_off(Object value) {
        var converted = instance.convertValue(value, OnOffType.class);
        assertThat(converted, instanceOf(OnOffType.class));
        assertThat(converted, is(OnOffType.OFF));
    }

    @ParameterizedTest
    @MethodSource("trueValues")
    void given_values_considerated_true_and_open_closed_target_return_open(Object value) {
        var converted = instance.convertValue(value, OpenClosedType.class);
        assertThat(converted, instanceOf(OpenClosedType.class));
        assertThat(converted, is(OpenClosedType.OPEN));
    }

    @ParameterizedTest
    @MethodSource("falseValues")
    void given_values_considerated_false_and_open_closed_target_return_closed(Object value) {
        var converted = instance.convertValue(value, OpenClosedType.class);
        assertThat(converted, instanceOf(OpenClosedType.class));
        assertThat(converted, is(OpenClosedType.CLOSED));
    }

    private static Stream<Object> trueValues() {
        return Stream.of("true", "True", 1, 2, "On", "on", -1, 0.3);
    }

    private static Stream<Object> falseValues() {
        return Stream.of("false", "False", 0, 0.0d, "off", "Off", "", "a value");
    }

    private static Stream<Number> provideNumericTypes() {
        return Stream.of(1L, 1.2, 1.2f, -1, 0, new BigDecimal("212321213123123123123123"));
    }

    private static Stream<Object> provideValuesOfAllSupportedResultRowTypes() {
        return Stream.of("", "String", Boolean.TRUE, 1L, 1.2, 1.2f, new BigDecimal("212321213123123123123123"),
                "bytes".getBytes(Charset.defaultCharset()), Instant.now(), new Date(), Duration.ofDays(1));
    }

    private static Stream<Object> provideValuesOfAllSupportedResultRowTypesExceptBytes() {
        return provideValuesOfAllSupportedResultRowTypes().filter(o -> !(o instanceof byte[]));
    }
}
