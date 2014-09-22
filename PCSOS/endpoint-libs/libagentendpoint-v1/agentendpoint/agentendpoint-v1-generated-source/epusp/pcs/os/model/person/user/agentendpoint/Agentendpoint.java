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
 * on 2014-09-17 at 22:29:03 UTC 
 * Modify at your own risk.
 */

package epusp.pcs.os.model.person.user.agentendpoint;

/**
 * Service definition for Agentendpoint (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link AgentendpointRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Agentendpoint extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.18.0-rc of the agentendpoint library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://epusp-pcsos.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "agentendpoint/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Agentendpoint(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Agentendpoint(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getAgent".
   *
   * This request holds the parameters needed by the agentendpoint server.  After setting any optional
   * parameters, call the {@link GetAgent#execute()} method to invoke the remote operation.
   *
   * @param email
   * @return the request
   */
  public GetAgent getAgent(java.lang.String email) throws java.io.IOException {
    GetAgent result = new GetAgent(email);
    initialize(result);
    return result;
  }

  public class GetAgent extends AgentendpointRequest<epusp.pcs.os.model.person.user.agentendpoint.model.Agent> {

    private static final String REST_PATH = "agent/{email}";

    /**
     * Create a request for the method "getAgent".
     *
     * This request holds the parameters needed by the the agentendpoint server.  After setting any
     * optional parameters, call the {@link GetAgent#execute()} method to invoke the remote operation.
     * <p> {@link
     * GetAgent#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param email
     * @since 1.13
     */
    protected GetAgent(java.lang.String email) {
      super(Agentendpoint.this, "GET", REST_PATH, null, epusp.pcs.os.model.person.user.agentendpoint.model.Agent.class);
      this.email = com.google.api.client.util.Preconditions.checkNotNull(email, "Required parameter email must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetAgent setAlt(java.lang.String alt) {
      return (GetAgent) super.setAlt(alt);
    }

    @Override
    public GetAgent setFields(java.lang.String fields) {
      return (GetAgent) super.setFields(fields);
    }

    @Override
    public GetAgent setKey(java.lang.String key) {
      return (GetAgent) super.setKey(key);
    }

    @Override
    public GetAgent setOauthToken(java.lang.String oauthToken) {
      return (GetAgent) super.setOauthToken(oauthToken);
    }

    @Override
    public GetAgent setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetAgent) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetAgent setQuotaUser(java.lang.String quotaUser) {
      return (GetAgent) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetAgent setUserIp(java.lang.String userIp) {
      return (GetAgent) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String email;

    /**

     */
    public java.lang.String getEmail() {
      return email;
    }

    public GetAgent setEmail(java.lang.String email) {
      this.email = email;
      return this;
    }

    @Override
    public GetAgent set(String parameterName, Object value) {
      return (GetAgent) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "insertAgent".
   *
   * This request holds the parameters needed by the agentendpoint server.  After setting any optional
   * parameters, call the {@link InsertAgent#execute()} method to invoke the remote operation.
   *
   * @param content the {@link epusp.pcs.os.model.person.user.agentendpoint.model.Agent}
   * @return the request
   */
  public InsertAgent insertAgent(epusp.pcs.os.model.person.user.agentendpoint.model.Agent content) throws java.io.IOException {
    InsertAgent result = new InsertAgent(content);
    initialize(result);
    return result;
  }

  public class InsertAgent extends AgentendpointRequest<epusp.pcs.os.model.person.user.agentendpoint.model.Agent> {

    private static final String REST_PATH = "agent";

    /**
     * Create a request for the method "insertAgent".
     *
     * This request holds the parameters needed by the the agentendpoint server.  After setting any
     * optional parameters, call the {@link InsertAgent#execute()} method to invoke the remote
     * operation. <p> {@link
     * InsertAgent#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param content the {@link epusp.pcs.os.model.person.user.agentendpoint.model.Agent}
     * @since 1.13
     */
    protected InsertAgent(epusp.pcs.os.model.person.user.agentendpoint.model.Agent content) {
      super(Agentendpoint.this, "POST", REST_PATH, content, epusp.pcs.os.model.person.user.agentendpoint.model.Agent.class);
    }

    @Override
    public InsertAgent setAlt(java.lang.String alt) {
      return (InsertAgent) super.setAlt(alt);
    }

    @Override
    public InsertAgent setFields(java.lang.String fields) {
      return (InsertAgent) super.setFields(fields);
    }

    @Override
    public InsertAgent setKey(java.lang.String key) {
      return (InsertAgent) super.setKey(key);
    }

    @Override
    public InsertAgent setOauthToken(java.lang.String oauthToken) {
      return (InsertAgent) super.setOauthToken(oauthToken);
    }

    @Override
    public InsertAgent setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (InsertAgent) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public InsertAgent setQuotaUser(java.lang.String quotaUser) {
      return (InsertAgent) super.setQuotaUser(quotaUser);
    }

    @Override
    public InsertAgent setUserIp(java.lang.String userIp) {
      return (InsertAgent) super.setUserIp(userIp);
    }

    @Override
    public InsertAgent set(String parameterName, Object value) {
      return (InsertAgent) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "listAgent".
   *
   * This request holds the parameters needed by the agentendpoint server.  After setting any optional
   * parameters, call the {@link ListAgent#execute()} method to invoke the remote operation.
   *
   * @return the request
   */
  public ListAgent listAgent() throws java.io.IOException {
    ListAgent result = new ListAgent();
    initialize(result);
    return result;
  }

  public class ListAgent extends AgentendpointRequest<epusp.pcs.os.model.person.user.agentendpoint.model.CollectionResponseAgent> {

    private static final String REST_PATH = "agent";

    /**
     * Create a request for the method "listAgent".
     *
     * This request holds the parameters needed by the the agentendpoint server.  After setting any
     * optional parameters, call the {@link ListAgent#execute()} method to invoke the remote
     * operation. <p> {@link
     * ListAgent#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @since 1.13
     */
    protected ListAgent() {
      super(Agentendpoint.this, "GET", REST_PATH, null, epusp.pcs.os.model.person.user.agentendpoint.model.CollectionResponseAgent.class);
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public ListAgent setAlt(java.lang.String alt) {
      return (ListAgent) super.setAlt(alt);
    }

    @Override
    public ListAgent setFields(java.lang.String fields) {
      return (ListAgent) super.setFields(fields);
    }

    @Override
    public ListAgent setKey(java.lang.String key) {
      return (ListAgent) super.setKey(key);
    }

    @Override
    public ListAgent setOauthToken(java.lang.String oauthToken) {
      return (ListAgent) super.setOauthToken(oauthToken);
    }

    @Override
    public ListAgent setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (ListAgent) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public ListAgent setQuotaUser(java.lang.String quotaUser) {
      return (ListAgent) super.setQuotaUser(quotaUser);
    }

    @Override
    public ListAgent setUserIp(java.lang.String userIp) {
      return (ListAgent) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String cursor;

    /**

     */
    public java.lang.String getCursor() {
      return cursor;
    }

    public ListAgent setCursor(java.lang.String cursor) {
      this.cursor = cursor;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Integer limit;

    /**

     */
    public java.lang.Integer getLimit() {
      return limit;
    }

    public ListAgent setLimit(java.lang.Integer limit) {
      this.limit = limit;
      return this;
    }

    @Override
    public ListAgent set(String parameterName, Object value) {
      return (ListAgent) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "removeAgent".
   *
   * This request holds the parameters needed by the agentendpoint server.  After setting any optional
   * parameters, call the {@link RemoveAgent#execute()} method to invoke the remote operation.
   *
   * @param email
   * @return the request
   */
  public RemoveAgent removeAgent(java.lang.String email) throws java.io.IOException {
    RemoveAgent result = new RemoveAgent(email);
    initialize(result);
    return result;
  }

  public class RemoveAgent extends AgentendpointRequest<Void> {

    private static final String REST_PATH = "agent/{email}";

    /**
     * Create a request for the method "removeAgent".
     *
     * This request holds the parameters needed by the the agentendpoint server.  After setting any
     * optional parameters, call the {@link RemoveAgent#execute()} method to invoke the remote
     * operation. <p> {@link
     * RemoveAgent#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param email
     * @since 1.13
     */
    protected RemoveAgent(java.lang.String email) {
      super(Agentendpoint.this, "DELETE", REST_PATH, null, Void.class);
      this.email = com.google.api.client.util.Preconditions.checkNotNull(email, "Required parameter email must be specified.");
    }

    @Override
    public RemoveAgent setAlt(java.lang.String alt) {
      return (RemoveAgent) super.setAlt(alt);
    }

    @Override
    public RemoveAgent setFields(java.lang.String fields) {
      return (RemoveAgent) super.setFields(fields);
    }

    @Override
    public RemoveAgent setKey(java.lang.String key) {
      return (RemoveAgent) super.setKey(key);
    }

    @Override
    public RemoveAgent setOauthToken(java.lang.String oauthToken) {
      return (RemoveAgent) super.setOauthToken(oauthToken);
    }

    @Override
    public RemoveAgent setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (RemoveAgent) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public RemoveAgent setQuotaUser(java.lang.String quotaUser) {
      return (RemoveAgent) super.setQuotaUser(quotaUser);
    }

    @Override
    public RemoveAgent setUserIp(java.lang.String userIp) {
      return (RemoveAgent) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String email;

    /**

     */
    public java.lang.String getEmail() {
      return email;
    }

    public RemoveAgent setEmail(java.lang.String email) {
      this.email = email;
      return this;
    }

    @Override
    public RemoveAgent set(String parameterName, Object value) {
      return (RemoveAgent) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "updateAgent".
   *
   * This request holds the parameters needed by the agentendpoint server.  After setting any optional
   * parameters, call the {@link UpdateAgent#execute()} method to invoke the remote operation.
   *
   * @param content the {@link epusp.pcs.os.model.person.user.agentendpoint.model.Agent}
   * @return the request
   */
  public UpdateAgent updateAgent(epusp.pcs.os.model.person.user.agentendpoint.model.Agent content) throws java.io.IOException {
    UpdateAgent result = new UpdateAgent(content);
    initialize(result);
    return result;
  }

  public class UpdateAgent extends AgentendpointRequest<epusp.pcs.os.model.person.user.agentendpoint.model.Agent> {

    private static final String REST_PATH = "agent";

    /**
     * Create a request for the method "updateAgent".
     *
     * This request holds the parameters needed by the the agentendpoint server.  After setting any
     * optional parameters, call the {@link UpdateAgent#execute()} method to invoke the remote
     * operation. <p> {@link
     * UpdateAgent#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param content the {@link epusp.pcs.os.model.person.user.agentendpoint.model.Agent}
     * @since 1.13
     */
    protected UpdateAgent(epusp.pcs.os.model.person.user.agentendpoint.model.Agent content) {
      super(Agentendpoint.this, "PUT", REST_PATH, content, epusp.pcs.os.model.person.user.agentendpoint.model.Agent.class);
    }

    @Override
    public UpdateAgent setAlt(java.lang.String alt) {
      return (UpdateAgent) super.setAlt(alt);
    }

    @Override
    public UpdateAgent setFields(java.lang.String fields) {
      return (UpdateAgent) super.setFields(fields);
    }

    @Override
    public UpdateAgent setKey(java.lang.String key) {
      return (UpdateAgent) super.setKey(key);
    }

    @Override
    public UpdateAgent setOauthToken(java.lang.String oauthToken) {
      return (UpdateAgent) super.setOauthToken(oauthToken);
    }

    @Override
    public UpdateAgent setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (UpdateAgent) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public UpdateAgent setQuotaUser(java.lang.String quotaUser) {
      return (UpdateAgent) super.setQuotaUser(quotaUser);
    }

    @Override
    public UpdateAgent setUserIp(java.lang.String userIp) {
      return (UpdateAgent) super.setUserIp(userIp);
    }

    @Override
    public UpdateAgent set(String parameterName, Object value) {
      return (UpdateAgent) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link Agentendpoint}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Agentendpoint}. */
    @Override
    public Agentendpoint build() {
      return new Agentendpoint(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link AgentendpointRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setAgentendpointRequestInitializer(
        AgentendpointRequestInitializer agentendpointRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(agentendpointRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
