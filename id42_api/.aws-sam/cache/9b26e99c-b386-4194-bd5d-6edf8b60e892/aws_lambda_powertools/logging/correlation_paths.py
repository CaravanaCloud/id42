"""Built-in correlation paths"""

API_GATEWAY_REST = "requestContext.requestId"
API_GATEWAY_HTTP = API_GATEWAY_REST
APPSYNC_AUTHORIZER = "requestContext.requestId"
APPSYNC_RESOLVER = 'request.headers."x-amzn-trace-id"'
APPLICATION_LOAD_BALANCER = 'headers."x-amzn-trace-id"'
EVENT_BRIDGE = "id"
LAMBDA_FUNCTION_URL = API_GATEWAY_REST
S3_OBJECT_LAMBDA = "xAmzRequestId"
VPC_LATTICE = 'headers."x-amzn-trace-id"'
