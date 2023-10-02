from aws_lambda_powertools.utilities.parser.compat import disable_pydantic_v2_warning

disable_pydantic_v2_warning()

from .alb import AlbModel, AlbRequestContext, AlbRequestContextData
from .apigw import (
    APIGatewayEventAuthorizer,
    APIGatewayEventIdentity,
    APIGatewayEventRequestContext,
    APIGatewayProxyEventModel,
)
from .apigwv2 import (
    APIGatewayProxyEventV2Model,
    RequestContextV2,
    RequestContextV2Authorizer,
    RequestContextV2AuthorizerIam,
    RequestContextV2AuthorizerIamCognito,
    RequestContextV2AuthorizerJwt,
    RequestContextV2Http,
)
from .cloudformation_custom_resource import (
    CloudFormationCustomResourceBaseModel,
    CloudFormationCustomResourceCreateModel,
    CloudFormationCustomResourceDeleteModel,
    CloudFormationCustomResourceUpdateModel,
)
from .cloudwatch import (
    CloudWatchLogsData,
    CloudWatchLogsDecode,
    CloudWatchLogsLogEvent,
    CloudWatchLogsModel,
)
from .dynamodb import (
    DynamoDBStreamChangedRecordModel,
    DynamoDBStreamModel,
    DynamoDBStreamRecordModel,
)
from .event_bridge import EventBridgeModel
from .kafka import (
    KafkaBaseEventModel,
    KafkaMskEventModel,
    KafkaRecordModel,
    KafkaSelfManagedEventModel,
)
from .kinesis import (
    KinesisDataStreamModel,
    KinesisDataStreamRecord,
    KinesisDataStreamRecordPayload,
)
from .kinesis_firehose import (
    KinesisFirehoseModel,
    KinesisFirehoseRecord,
    KinesisFirehoseRecordMetadata,
)
from .kinesis_firehose_sqs import KinesisFirehoseSqsModel, KinesisFirehoseSqsRecord
from .lambda_function_url import LambdaFunctionUrlModel
from .s3 import (
    S3EventNotificationEventBridgeDetailModel,
    S3EventNotificationEventBridgeModel,
    S3EventNotificationObjectModel,
    S3Model,
    S3RecordModel,
)
from .s3_event_notification import (
    S3SqsEventNotificationModel,
    S3SqsEventNotificationRecordModel,
)
from .s3_object_event import (
    S3ObjectConfiguration,
    S3ObjectContext,
    S3ObjectLambdaEvent,
    S3ObjectSessionAttributes,
    S3ObjectSessionContext,
    S3ObjectSessionIssuer,
    S3ObjectUserIdentity,
    S3ObjectUserRequest,
)
from .ses import (
    SesMail,
    SesMailCommonHeaders,
    SesMailHeaders,
    SesMessage,
    SesModel,
    SesReceipt,
    SesReceiptAction,
    SesReceiptVerdict,
    SesRecordModel,
)
from .sns import SnsModel, SnsNotificationModel, SnsRecordModel
from .sqs import SqsAttributesModel, SqsModel, SqsMsgAttributeModel, SqsRecordModel
from .vpc_lattice import VpcLatticeModel

__all__ = [
    "APIGatewayProxyEventV2Model",
    "RequestContextV2",
    "RequestContextV2Http",
    "RequestContextV2Authorizer",
    "RequestContextV2AuthorizerJwt",
    "RequestContextV2AuthorizerIam",
    "RequestContextV2AuthorizerIamCognito",
    "CloudWatchLogsData",
    "CloudWatchLogsDecode",
    "CloudWatchLogsLogEvent",
    "CloudWatchLogsModel",
    "AlbModel",
    "AlbRequestContext",
    "AlbRequestContextData",
    "DynamoDBStreamModel",
    "EventBridgeModel",
    "DynamoDBStreamChangedRecordModel",
    "DynamoDBStreamRecordModel",
    "DynamoDBStreamChangedRecordModel",
    "KinesisDataStreamModel",
    "KinesisDataStreamRecord",
    "KinesisDataStreamRecordPayload",
    "KinesisFirehoseModel",
    "KinesisFirehoseRecord",
    "KinesisFirehoseRecordMetadata",
    "LambdaFunctionUrlModel",
    "S3Model",
    "S3RecordModel",
    "S3ObjectLambdaEvent",
    "S3ObjectUserIdentity",
    "S3ObjectSessionContext",
    "S3ObjectSessionAttributes",
    "S3ObjectSessionIssuer",
    "S3ObjectUserRequest",
    "S3ObjectConfiguration",
    "S3ObjectContext",
    "S3EventNotificationObjectModel",
    "S3EventNotificationEventBridgeModel",
    "S3EventNotificationEventBridgeDetailModel",
    "SesModel",
    "SesRecordModel",
    "SesMessage",
    "SesMail",
    "SesMailCommonHeaders",
    "SesMailHeaders",
    "SesReceipt",
    "SesReceiptAction",
    "SesReceiptVerdict",
    "SnsModel",
    "SnsNotificationModel",
    "SnsRecordModel",
    "SqsModel",
    "SqsRecordModel",
    "SqsMsgAttributeModel",
    "SqsAttributesModel",
    "S3SqsEventNotificationModel",
    "S3SqsEventNotificationRecordModel",
    "APIGatewayProxyEventModel",
    "APIGatewayEventRequestContext",
    "APIGatewayEventAuthorizer",
    "APIGatewayEventIdentity",
    "KafkaSelfManagedEventModel",
    "KafkaRecordModel",
    "KafkaMskEventModel",
    "KafkaBaseEventModel",
    "KinesisFirehoseSqsModel",
    "KinesisFirehoseSqsRecord",
    "CloudFormationCustomResourceUpdateModel",
    "CloudFormationCustomResourceDeleteModel",
    "CloudFormationCustomResourceCreateModel",
    "CloudFormationCustomResourceBaseModel",
    "VpcLatticeModel",
]
