/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appacitive.core.apjson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Note: this class was written without inspecting the non-free org.apjson sourcecode.

/**
 * A dense indexed sequence of values. Values may be any mix of
 * {@link APJSONObject JSONObjects}, other {@link APJSONArray JSONArrays}, Strings,
 * Booleans, Integers, Longs, Doubles, {@code null} or {@link APJSONObject#NULL}.
 * Values may not be {@link Double#isNaN() NaNs}, {@link Double#isInfinite()
 * infinities}, or of any type not listed here.
 * <p/>
 * <p>{@code APJSONArray} has the same type coercion behavior and
 * optional/mandatory accessors as {@link APJSONObject}. See that class'
 * documentation for details.
 * <p/>
 * <p><strong>Warning:</strong> this class represents null in two incompatible
 * ways: the standard Java {@code null} reference, and the sentinel value {@link
 * APJSONObject#NULL}. In particular, {@code get} fails if the requested index
 * holds the null reference, but succeeds if it holds {@code APJSONObject.NULL}.
 * <p/>
 * <p>Instances of this class are not thread safe. Although this class is
 * nonfinal, it was not designed for inheritance and should not be subclassed.
 * In particular, self-use by overridable methods is not specified. See
 * <i>Effective Java</i> Item 17, "Design and Document or inheritance or else
 * prohibit it" for further information.
 */
public class APJSONArray {

    public final List<Object> values;

    /**
     * Creates a {@code APJSONArray} with no values.
     */
    public APJSONArray() {
        values = new ArrayList<Object>();
    }

    /**
     * Creates a new {@code APJSONArray} by copying all values from the given
     * collection.
     *
     * @param copyFrom a collection whose values are of supported types.
     *                 Unsupported values are not permitted and will yield an array in an
     *                 inconsistent state.
     */
    /* Accept a raw type for API compatibility */
    public APJSONArray(Collection copyFrom) {
        this();
        Collection<?> copyFromTyped = (Collection<?>) copyFrom;
        values.addAll(copyFromTyped);
    }

    /**
     * Creates a new {@code APJSONArray} with values from the next array in the
     * tokener.
     *
     * @param readFrom a tokener whose nextValue() method will yield a
     *                 {@code APJSONArray}.
     * @throws APJSONException if the parse fails or doesn't yield a
     *                       {@code APJSONArray}.
     */
    public APJSONArray(APJSONTokener readFrom) throws APJSONException {
        /*
         * Getting the parser to populate this could get tricky. Instead, just
         * parse to temporary APJSONArray and then steal the data from that.
         */
        Object object = readFrom.nextValue();
        if (object instanceof APJSONArray) {
            values = ((APJSONArray) object).values;
        } else {
            throw APJSON.typeMismatch(object, "APJSONArray");
        }
    }

    /**
     * Creates a new {@code APJSONArray} with values from the APJSON string.
     *
     * @param json a APJSON-encoded string containing an array.
     * @throws APJSONException if the parse fails or doesn't yield a {@code
     *                       APJSONArray}.
     */
    public APJSONArray(String json) throws APJSONException {
        this(new APJSONTokener(json));
    }

    /**
     * Returns the number of values in this array.
     */
    public int length() {
        return values.size();
    }

    /**
     * Appends {@code value} to the end of this array.
     *
     * @return this array.
     */
    public APJSONArray put(boolean value) {
        values.add(value);
        return this;
    }

    /**
     * Appends {@code value} to the end of this array.
     *
     * @param value a finite value. May not be {@link Double#isNaN() NaNs} or
     *              {@link Double#isInfinite() infinities}.
     * @return this array.
     */
    public APJSONArray put(double value) throws APJSONException {
        values.add(APJSON.checkDouble(value));
        return this;
    }

    /**
     * Appends {@code value} to the end of this array.
     *
     * @return this array.
     */
    public APJSONArray put(int value) {
        values.add(value);
        return this;
    }

    /**
     * Appends {@code value} to the end of this array.
     *
     * @return this array.
     */
    public APJSONArray put(long value) {
        values.add(value);
        return this;
    }

    /**
     * Appends {@code value} to the end of this array.
     *
     * @param value a {@link APJSONObject}, {@link APJSONArray}, String, Boolean,
     *              Integer, Long, Double, {@link APJSONObject#NULL}, or {@code null}. May
     *              not be {@link Double#isNaN() NaNs} or {@link Double#isInfinite()
     *              infinities}. Unsupported values are not permitted and will cause the
     *              array to be in an inconsistent state.
     * @return this array.
     */
    public APJSONArray put(Object value) {
        values.add(value);
        return this;
    }

    /**
     * Sets the value at {@code index} to {@code value}, null padding this array
     * to the required length if necessary. If a value already exists at {@code
     * index}, it will be replaced.
     *
     * @return this array.
     */
    public APJSONArray put(int index, boolean value) throws APJSONException {
        return put(index, (Boolean) value);
    }

    /**
     * Sets the value at {@code index} to {@code value}, null padding this array
     * to the required length if necessary. If a value already exists at {@code
     * index}, it will be replaced.
     *
     * @param value a finite value. May not be {@link Double#isNaN() NaNs} or
     *              {@link Double#isInfinite() infinities}.
     * @return this array.
     */
    public APJSONArray put(int index, double value) throws APJSONException {
        return put(index, (Double) value);
    }

    /**
     * Sets the value at {@code index} to {@code value}, null padding this array
     * to the required length if necessary. If a value already exists at {@code
     * index}, it will be replaced.
     *
     * @return this array.
     */
    public APJSONArray put(int index, int value) throws APJSONException {
        return put(index, (Integer) value);
    }

    /**
     * Sets the value at {@code index} to {@code value}, null padding this array
     * to the required length if necessary. If a value already exists at {@code
     * index}, it will be replaced.
     *
     * @return this array.
     */
    public APJSONArray put(int index, long value) throws APJSONException {
        return put(index, (Long) value);
    }

    /**
     * Sets the value at {@code index} to {@code value}, null padding this array
     * to the required length if necessary. If a value already exists at {@code
     * index}, it will be replaced.
     *
     * @param value a {@link APJSONObject}, {@link APJSONArray}, String, Boolean,
     *              Integer, Long, Double, {@link APJSONObject#NULL}, or {@code null}. May
     *              not be {@link Double#isNaN() NaNs} or {@link Double#isInfinite()
     *              infinities}.
     * @return this array.
     */
    public APJSONArray put(int index, Object value) throws APJSONException {
        if (value instanceof Number) {
            // deviate from the original by checking all Numbers, not just floats & doubles
            APJSON.checkDouble(((Number) value).doubleValue());
        }
        while (values.size() <= index) {
            values.add(null);
        }
        values.set(index, value);
        return this;
    }

    /**
     * Returns true if this array has no value at {@code index}, or if its value
     * is the {@code null} reference or {@link APJSONObject#NULL}.
     */
    public boolean isNull(int index) {
        Object value = opt(index);
        return value == null || value == APJSONObject.NULL;
    }

    /**
     * Returns the value at {@code index}.
     *
     * @throws APJSONException if this array has no value at {@code index}, or if
     *                       that value is the {@code null} reference. This method returns
     *                       normally if the value is {@code APJSONObject#NULL}.
     */
    public Object get(int index) throws APJSONException {
        try {
            Object value = values.get(index);
            if (value == null) {
                throw new APJSONException("Value at " + index + " is null.");
            }
            return value;
        } catch (IndexOutOfBoundsException e) {
            throw new APJSONException("Index " + index + " out of range [0.." + values.size() + ")");
        }
    }

    /**
     * Returns the value at {@code index}, or null if the array has no value
     * at {@code index}.
     */
    public Object opt(int index) {
        if (index < 0 || index >= values.size()) {
            return null;
        }
        return values.get(index);
    }

    /**
     * Returns the value at {@code index} if it exists and is a boolean or can
     * be coerced to a boolean.
     *
     * @throws APJSONException if the value at {@code index} doesn't exist or
     *                       cannot be coerced to a boolean.
     */
    public boolean getBoolean(int index) throws APJSONException {
        Object object = get(index);
        Boolean result = APJSON.toBoolean(object);
        if (result == null) {
            throw APJSON.typeMismatch(index, object, "boolean");
        }
        return result;
    }

    /**
     * Returns the value at {@code index} if it exists and is a boolean or can
     * be coerced to a boolean. Returns false otherwise.
     */
    public boolean optBoolean(int index) {
        return optBoolean(index, false);
    }

    /**
     * Returns the value at {@code index} if it exists and is a boolean or can
     * be coerced to a boolean. Returns {@code fallback} otherwise.
     */
    public boolean optBoolean(int index, boolean fallback) {
        Object object = opt(index);
        Boolean result = APJSON.toBoolean(object);
        return result != null ? result : fallback;
    }

    /**
     * Returns the value at {@code index} if it exists and is a double or can
     * be coerced to a double.
     *
     * @throws APJSONException if the value at {@code index} doesn't exist or
     *                       cannot be coerced to a double.
     */
    public double getDouble(int index) throws APJSONException {
        Object object = get(index);
        Double result = APJSON.toDouble(object);
        if (result == null) {
            throw APJSON.typeMismatch(index, object, "double");
        }
        return result;
    }

    /**
     * Returns the value at {@code index} if it exists and is a double or can
     * be coerced to a double. Returns {@code NaN} otherwise.
     */
    public double optDouble(int index) {
        return optDouble(index, Double.NaN);
    }

    /**
     * Returns the value at {@code index} if it exists and is a double or can
     * be coerced to a double. Returns {@code fallback} otherwise.
     */
    public double optDouble(int index, double fallback) {
        Object object = opt(index);
        Double result = APJSON.toDouble(object);
        return result != null ? result : fallback;
    }

    /**
     * Returns the value at {@code index} if it exists and is an int or
     * can be coerced to an int.
     *
     * @throws APJSONException if the value at {@code index} doesn't exist or
     *                       cannot be coerced to a int.
     */
    public int getInt(int index) throws APJSONException {
        Object object = get(index);
        Integer result = APJSON.toInteger(object);
        if (result == null) {
            throw APJSON.typeMismatch(index, object, "int");
        }
        return result;
    }

    /**
     * Returns the value at {@code index} if it exists and is an int or
     * can be coerced to an int. Returns 0 otherwise.
     */
    public int optInt(int index) {
        return optInt(index, 0);
    }

    /**
     * Returns the value at {@code index} if it exists and is an int or
     * can be coerced to an int. Returns {@code fallback} otherwise.
     */
    public int optInt(int index, int fallback) {
        Object object = opt(index);
        Integer result = APJSON.toInteger(object);
        return result != null ? result : fallback;
    }

    /**
     * Returns the value at {@code index} if it exists and is a long or
     * can be coerced to a long.
     *
     * @throws APJSONException if the value at {@code index} doesn't exist or
     *                       cannot be coerced to a long.
     */
    public long getLong(int index) throws APJSONException {
        Object object = get(index);
        Long result = APJSON.toLong(object);
        if (result == null) {
            throw APJSON.typeMismatch(index, object, "long");
        }
        return result;
    }

    /**
     * Returns the value at {@code index} if it exists and is a long or
     * can be coerced to a long. Returns 0 otherwise.
     */
    public long optLong(int index) {
        return optLong(index, 0L);
    }

    /**
     * Returns the value at {@code index} if it exists and is a long or
     * can be coerced to a long. Returns {@code fallback} otherwise.
     */
    public long optLong(int index, long fallback) {
        Object object = opt(index);
        Long result = APJSON.toLong(object);
        return result != null ? result : fallback;
    }

    /**
     * Returns the value at {@code index} if it exists, coercing it if
     * necessary.
     *
     * @throws APJSONException if no such value exists.
     */
    public String getString(int index) throws APJSONException {
        Object object = get(index);
        String result = APJSON.toString(object);
        if (result == null) {
            throw APJSON.typeMismatch(index, object, "String");
        }
        return result;
    }

    /**
     * Returns the value at {@code index} if it exists, coercing it if
     * necessary. Returns the empty string if no such value exists.
     */
    public String optString(int index) {
        return optString(index, "");
    }

    /**
     * Returns the value at {@code index} if it exists, coercing it if
     * necessary. Returns {@code fallback} if no such value exists.
     */
    public String optString(int index, String fallback) {
        Object object = opt(index);
        String result = APJSON.toString(object);
        return result != null ? result : fallback;
    }

    /**
     * Returns the value at {@code index} if it exists and is a {@code
     * APJSONArray}.
     *
     * @throws APJSONException if the value doesn't exist or is not a {@code
     *                       APJSONArray}.
     */
    public APJSONArray getJSONArray(int index) throws APJSONException {
        Object object = get(index);
        if (object instanceof APJSONArray) {
            return (APJSONArray) object;
        } else {
            throw APJSON.typeMismatch(index, object, "APJSONArray");
        }
    }

    /**
     * Returns the value at {@code index} if it exists and is a {@code
     * APJSONArray}. Returns null otherwise.
     */
    public APJSONArray optJSONArray(int index) {
        Object object = opt(index);
        return object instanceof APJSONArray ? (APJSONArray) object : null;
    }

    /**
     * Returns the value at {@code index} if it exists and is a {@code
     * APJSONObject}.
     *
     * @throws APJSONException if the value doesn't exist or is not a {@code
     *                       APJSONObject}.
     */
    public APJSONObject getJSONObject(int index) throws APJSONException {
        Object object = get(index);
        if (object instanceof APJSONObject) {
            return (APJSONObject) object;
        } else {
            throw APJSON.typeMismatch(index, object, "APJSONObject");
        }
    }

    /**
     * Returns the value at {@code index} if it exists and is a {@code
     * APJSONObject}. Returns null otherwise.
     */
    public APJSONObject optJSONObject(int index) {
        Object object = opt(index);
        return object instanceof APJSONObject ? (APJSONObject) object : null;
    }

    /**
     * Returns a new object whose values are the values in this array, and whose
     * names are the values in {@code names}. Names and values are paired up by
     * index from 0 through to the shorter array's length. Names that are not
     * strings will be coerced to strings. This method returns null if either
     * array is empty.
     */
    public APJSONObject toJSONObject(APJSONArray names) throws APJSONException {
        APJSONObject result = new APJSONObject();
        int length = Math.min(names.length(), values.size());
        if (length == 0) {
            return null;
        }
        for (int i = 0; i < length; i++) {
            String name = APJSON.toString(names.opt(i));
            result.put(name, opt(i));
        }
        return result;
    }

    /**
     * Returns a new string by alternating this array's values with {@code
     * separator}. This array's string values are quoted and have their special
     * characters escaped. For example, the array containing the strings '12"
     * pizza', 'taco' and 'soda' joined on '+' returns this:
     * <pre>"12\" pizza"+"taco"+"soda"</pre>
     */
    public String join(String separator) throws APJSONException {
        APJSONStringer stringer = new APJSONStringer();
        stringer.open(APJSONStringer.Scope.NULL, "");
        for (int i = 0, size = values.size(); i < size; i++) {
            if (i > 0) {
                stringer.out.append(separator);
            }
            stringer.value(values.get(i));
        }
        stringer.close(APJSONStringer.Scope.NULL, APJSONStringer.Scope.NULL, "");
        return stringer.out.toString();
    }

    /**
     * Encodes this array as a compact APJSON string, such as:
     * <pre>[94043,90210]</pre>
     */
    @Override
    public String toString() {
        try {
            APJSONStringer stringer = new APJSONStringer();
            writeTo(stringer);
            return stringer.toString();
        } catch (APJSONException e) {
            return null;
        }
    }

    /**
     * Encodes this array as a human readable APJSON string for debugging, such
     * as:
     * <pre>
     * [
     *     94043,
     *     90210
     * ]</pre>
     *
     * @param indentSpaces the number of spaces to indent for each level of
     *                     nesting.
     */
    public String toString(int indentSpaces) throws APJSONException {
        APJSONStringer stringer = new APJSONStringer(indentSpaces);
        writeTo(stringer);
        return stringer.toString();
    }

    void writeTo(APJSONStringer stringer) throws APJSONException {
        stringer.array();
        for (Object value : values) {
            stringer.value(value);
        }
        stringer.endArray();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof APJSONArray && ((APJSONArray) o).values.equals(values);
    }

    @Override
    public int hashCode() {
        // diverge from the original, which doesn't implement hashCode
        return values.hashCode();
    }
}
