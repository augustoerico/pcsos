/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2014-07-22 21:53:01 UTC)
 * on 2014-09-17 at 22:28:50 UTC 
 * Modify at your own risk.
 */

package epusp.pcs.os.model.person.victimendpoint.model;

/**
 * Model definition for FloatArrayAttribute.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the victimendpoint. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class FloatArrayAttribute extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String attributeName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String dataType;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.Float> value;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.Float> valuesAsList;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getAttributeName() {
    return attributeName;
  }

  /**
   * @param attributeName attributeName or {@code null} for none
   */
  public FloatArrayAttribute setAttributeName(java.lang.String attributeName) {
    this.attributeName = attributeName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDataType() {
    return dataType;
  }

  /**
   * @param dataType dataType or {@code null} for none
   */
  public FloatArrayAttribute setDataType(java.lang.String dataType) {
    this.dataType = dataType;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.Float> getValue() {
    return value;
  }

  /**
   * @param value value or {@code null} for none
   */
  public FloatArrayAttribute setValue(java.util.List<java.lang.Float> value) {
    this.value = value;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.Float> getValuesAsList() {
    return valuesAsList;
  }

  /**
   * @param valuesAsList valuesAsList or {@code null} for none
   */
  public FloatArrayAttribute setValuesAsList(java.util.List<java.lang.Float> valuesAsList) {
    this.valuesAsList = valuesAsList;
    return this;
  }

  @Override
  public FloatArrayAttribute set(String fieldName, Object value) {
    return (FloatArrayAttribute) super.set(fieldName, value);
  }

  @Override
  public FloatArrayAttribute clone() {
    return (FloatArrayAttribute) super.clone();
  }

}
