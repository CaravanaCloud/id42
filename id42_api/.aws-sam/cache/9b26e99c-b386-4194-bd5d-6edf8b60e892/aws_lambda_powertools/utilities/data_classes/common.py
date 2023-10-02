import base64
import json
from collections.abc import Mapping
from typing import Any, Callable, Dict, Iterator, List, Optional, overload

from aws_lambda_powertools.shared.headers_serializer import BaseHeadersSerializer
from aws_lambda_powertools.utilities.data_classes.shared_functions import (
    get_header_value,
    get_query_string_value,
)


class DictWrapper(Mapping):
    """Provides a single read only access to a wrapper dict"""

    def __init__(self, data: Dict[str, Any], json_deserializer: Optional[Callable] = None):
        """
        Parameters
        ----------
        data : Dict[str, Any]
            Lambda Event Source Event payload
        json_deserializer : Callable, optional
            function to deserialize `str`, `bytes`, `bytearray` containing a JSON document to a Python `obj`,
            by default json.loads
        """
        self._data = data
        self._json_data: Optional[Any] = None
        self._json_deserializer = json_deserializer or json.loads

    def __getitem__(self, key: str) -> Any:
        return self._data[key]

    def __eq__(self, other: object) -> bool:
        if not isinstance(other, DictWrapper):
            return False

        return self._data == other._data

    def __iter__(self) -> Iterator:
        return iter(self._data)

    def __len__(self) -> int:
        return len(self._data)

    def __str__(self) -> str:
        return str(self._str_helper())

    def _str_helper(self) -> Dict[str, Any]:
        """
        Recursively get a Dictionary of DictWrapper properties primarily
        for use by __str__ for debugging purposes.

        Will remove "raw_event" properties, and any defined by the Data Class
        `_sensitive_properties` list field.
        This should be used in case where secrets, such as access keys, are
        stored in the Data Class but should not be logged out.
        """
        properties = self._properties()
        sensitive_properties = ["raw_event"]
        if hasattr(self, "_sensitive_properties"):
            sensitive_properties.extend(self._sensitive_properties)  # pyright: ignore

        result: Dict[str, Any] = {}
        for property_key in properties:
            if property_key in sensitive_properties:
                result[property_key] = "[SENSITIVE]"
            else:
                try:
                    property_value = getattr(self, property_key)
                    result[property_key] = property_value

                    # Checks whether the class is a subclass of the parent class to perform a recursive operation.
                    if issubclass(property_value.__class__, DictWrapper):
                        result[property_key] = property_value._str_helper()
                    # Checks if the key is a list and if it is a subclass of the parent class
                    elif isinstance(property_value, list):
                        for seq, item in enumerate(property_value):
                            if issubclass(item.__class__, DictWrapper):
                                result[property_key][seq] = item._str_helper()
                except Exception:
                    result[property_key] = "[Cannot be deserialized]"

        return result

    def _properties(self) -> List[str]:
        return [p for p in dir(self.__class__) if isinstance(getattr(self.__class__, p), property)]

    def get(self, key: str, default: Optional[Any] = None) -> Optional[Any]:
        return self._data.get(key, default)

    @property
    def raw_event(self) -> Dict[str, Any]:
        """The original raw event dict"""
        return self._data


class BaseProxyEvent(DictWrapper):
    @property
    def headers(self) -> Dict[str, str]:
        return self.get("headers") or {}

    @property
    def query_string_parameters(self) -> Optional[Dict[str, str]]:
        return self.get("queryStringParameters")

    @property
    def is_base64_encoded(self) -> Optional[bool]:
        return self.get("isBase64Encoded")

    @property
    def body(self) -> Optional[str]:
        """Submitted body of the request as a string"""
        return self.get("body")

    @property
    def json_body(self) -> Any:
        """Parses the submitted body as json"""
        if self._json_data is None:
            self._json_data = self._json_deserializer(self.decoded_body)
        return self._json_data

    @property
    def decoded_body(self) -> str:
        """Dynamically base64 decode body as a str"""
        body: str = self["body"]
        if self.is_base64_encoded:
            return base64.b64decode(body.encode()).decode()
        return body

    @property
    def path(self) -> str:
        return self["path"]

    @property
    def http_method(self) -> str:
        """The HTTP method used. Valid values include: DELETE, GET, HEAD, OPTIONS, PATCH, POST, and PUT."""
        return self["httpMethod"]

    def get_query_string_value(self, name: str, default_value: Optional[str] = None) -> Optional[str]:
        """Get query string value by name

        Parameters
        ----------
        name: str
            Query string parameter name
        default_value: str, optional
            Default value if no value was found by name
        Returns
        -------
        str, optional
            Query string parameter value
        """
        return get_query_string_value(
            query_string_parameters=self.query_string_parameters,
            name=name,
            default_value=default_value,
        )

    @overload
    def get_header_value(
        self,
        name: str,
        default_value: str,
        case_sensitive: Optional[bool] = False,
    ) -> str:
        ...

    @overload
    def get_header_value(
        self,
        name: str,
        default_value: Optional[str] = None,
        case_sensitive: Optional[bool] = False,
    ) -> Optional[str]:
        ...

    def get_header_value(
        self,
        name: str,
        default_value: Optional[str] = None,
        case_sensitive: Optional[bool] = False,
    ) -> Optional[str]:
        """Get header value by name

        Parameters
        ----------
        name: str
            Header name
        default_value: str, optional
            Default value if no value was found by name
        case_sensitive: bool
            Whether to use a case-sensitive look up
        Returns
        -------
        str, optional
            Header value
        """
        return get_header_value(
            headers=self.headers,
            name=name,
            default_value=default_value,
            case_sensitive=case_sensitive,
        )

    def header_serializer(self) -> BaseHeadersSerializer:
        raise NotImplementedError()


class RequestContextClientCert(DictWrapper):
    @property
    def client_cert_pem(self) -> str:
        """Client certificate pem"""
        return self["clientCertPem"]

    @property
    def issuer_dn(self) -> str:
        """Issuer Distinguished Name"""
        return self["issuerDN"]

    @property
    def serial_number(self) -> str:
        """Unique serial number for client cert"""
        return self["serialNumber"]

    @property
    def subject_dn(self) -> str:
        """Subject Distinguished Name"""
        return self["subjectDN"]

    @property
    def validity_not_after(self) -> str:
        """Date when the cert is no longer valid

        eg: Aug  5 00:28:21 2120 GMT"""
        return self["validity"]["notAfter"]

    @property
    def validity_not_before(self) -> str:
        """Cert is not valid before this date

        eg: Aug 29 00:28:21 2020 GMT"""
        return self["validity"]["notBefore"]


class APIGatewayEventIdentity(DictWrapper):
    @property
    def access_key(self) -> Optional[str]:
        return self["requestContext"]["identity"].get("accessKey")

    @property
    def account_id(self) -> Optional[str]:
        """The AWS account ID associated with the request."""
        return self["requestContext"]["identity"].get("accountId")

    @property
    def api_key(self) -> Optional[str]:
        """For API methods that require an API key, this variable is the API key associated with the method request.
        For methods that don't require an API key, this variable is null."""
        return self["requestContext"]["identity"].get("apiKey")

    @property
    def api_key_id(self) -> Optional[str]:
        """The API key ID associated with an API request that requires an API key."""
        return self["requestContext"]["identity"].get("apiKeyId")

    @property
    def caller(self) -> Optional[str]:
        """The principal identifier of the caller making the request."""
        return self["requestContext"]["identity"].get("caller")

    @property
    def cognito_authentication_provider(self) -> Optional[str]:
        """A comma-separated list of the Amazon Cognito authentication providers used by the caller
        making the request. Available only if the request was signed with Amazon Cognito credentials."""
        return self["requestContext"]["identity"].get("cognitoAuthenticationProvider")

    @property
    def cognito_authentication_type(self) -> Optional[str]:
        """The Amazon Cognito authentication type of the caller making the request.
        Available only if the request was signed with Amazon Cognito credentials."""
        return self["requestContext"]["identity"].get("cognitoAuthenticationType")

    @property
    def cognito_identity_id(self) -> Optional[str]:
        """The Amazon Cognito identity ID of the caller making the request.
        Available only if the request was signed with Amazon Cognito credentials."""
        return self["requestContext"]["identity"].get("cognitoIdentityId")

    @property
    def cognito_identity_pool_id(self) -> Optional[str]:
        """The Amazon Cognito identity pool ID of the caller making the request.
        Available only if the request was signed with Amazon Cognito credentials."""
        return self["requestContext"]["identity"].get("cognitoIdentityPoolId")

    @property
    def principal_org_id(self) -> Optional[str]:
        """The AWS organization ID."""
        return self["requestContext"]["identity"].get("principalOrgId")

    @property
    def source_ip(self) -> str:
        """The source IP address of the TCP connection making the request to API Gateway."""
        return self["requestContext"]["identity"]["sourceIp"]

    @property
    def user(self) -> Optional[str]:
        """The principal identifier of the user making the request."""
        return self["requestContext"]["identity"].get("user")

    @property
    def user_agent(self) -> Optional[str]:
        """The User Agent of the API caller."""
        return self["requestContext"]["identity"].get("userAgent")

    @property
    def user_arn(self) -> Optional[str]:
        """The Amazon Resource Name (ARN) of the effective user identified after authentication."""
        return self["requestContext"]["identity"].get("userArn")

    @property
    def client_cert(self) -> Optional[RequestContextClientCert]:
        client_cert = self["requestContext"]["identity"].get("clientCert")
        return None if client_cert is None else RequestContextClientCert(client_cert)


class BaseRequestContext(DictWrapper):
    @property
    def account_id(self) -> str:
        """The AWS account ID associated with the request."""
        return self["requestContext"]["accountId"]

    @property
    def api_id(self) -> str:
        """The identifier API Gateway assigns to your API."""
        return self["requestContext"]["apiId"]

    @property
    def domain_name(self) -> Optional[str]:
        """A domain name"""
        return self["requestContext"].get("domainName")

    @property
    def domain_prefix(self) -> Optional[str]:
        return self["requestContext"].get("domainPrefix")

    @property
    def extended_request_id(self) -> Optional[str]:
        """An automatically generated ID for the API call, which contains more useful information
        for debugging/troubleshooting."""
        return self["requestContext"].get("extendedRequestId")

    @property
    def protocol(self) -> str:
        """The request protocol, for example, HTTP/1.1."""
        return self["requestContext"]["protocol"]

    @property
    def http_method(self) -> str:
        """The HTTP method used. Valid values include: DELETE, GET, HEAD, OPTIONS, PATCH, POST, and PUT."""
        return self["requestContext"]["httpMethod"]

    @property
    def identity(self) -> APIGatewayEventIdentity:
        return APIGatewayEventIdentity(self._data)

    @property
    def path(self) -> str:
        return self["requestContext"]["path"]

    @property
    def stage(self) -> str:
        """The deployment stage of the API request"""
        return self["requestContext"]["stage"]

    @property
    def request_id(self) -> str:
        """The ID that API Gateway assigns to the API request."""
        return self["requestContext"]["requestId"]

    @property
    def request_time(self) -> Optional[str]:
        """The CLF-formatted request time (dd/MMM/yyyy:HH:mm:ss +-hhmm)"""
        return self["requestContext"].get("requestTime")

    @property
    def request_time_epoch(self) -> int:
        """The Epoch-formatted request time."""
        return self["requestContext"]["requestTimeEpoch"]

    @property
    def resource_id(self) -> str:
        return self["requestContext"]["resourceId"]

    @property
    def resource_path(self) -> str:
        return self["requestContext"]["resourcePath"]


class RequestContextV2Http(DictWrapper):
    @property
    def method(self) -> str:
        return self["requestContext"]["http"]["method"]

    @property
    def path(self) -> str:
        return self["requestContext"]["http"]["path"]

    @property
    def protocol(self) -> str:
        """The request protocol, for example, HTTP/1.1."""
        return self["requestContext"]["http"]["protocol"]

    @property
    def source_ip(self) -> str:
        """The source IP address of the TCP connection making the request to API Gateway."""
        return self["requestContext"]["http"]["sourceIp"]

    @property
    def user_agent(self) -> str:
        """The User Agent of the API caller."""
        return self["requestContext"]["http"]["userAgent"]


class BaseRequestContextV2(DictWrapper):
    @property
    def account_id(self) -> str:
        """The AWS account ID associated with the request."""
        return self["requestContext"]["accountId"]

    @property
    def api_id(self) -> str:
        """The identifier API Gateway assigns to your API."""
        return self["requestContext"]["apiId"]

    @property
    def domain_name(self) -> str:
        """A domain name"""
        return self["requestContext"]["domainName"]

    @property
    def domain_prefix(self) -> str:
        return self["requestContext"]["domainPrefix"]

    @property
    def http(self) -> RequestContextV2Http:
        return RequestContextV2Http(self._data)

    @property
    def request_id(self) -> str:
        """The ID that API Gateway assigns to the API request."""
        return self["requestContext"]["requestId"]

    @property
    def route_key(self) -> str:
        """The selected route key."""
        return self["requestContext"]["routeKey"]

    @property
    def stage(self) -> str:
        """The deployment stage of the API request"""
        return self["requestContext"]["stage"]

    @property
    def time(self) -> str:
        """The CLF-formatted request time (dd/MMM/yyyy:HH:mm:ss +-hhmm)."""
        return self["requestContext"]["time"]

    @property
    def time_epoch(self) -> int:
        """The Epoch-formatted request time."""
        return self["requestContext"]["timeEpoch"]

    @property
    def authentication(self) -> Optional[RequestContextClientCert]:
        """Optional when using mutual TLS authentication"""
        # FunctionURL might have NONE as AuthZ
        authentication = self["requestContext"].get("authentication") or {}
        client_cert = authentication.get("clientCert")
        return None if client_cert is None else RequestContextClientCert(client_cert)
