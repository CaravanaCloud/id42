package id42;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.Peer;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.rds.*;
import software.constructs.Construct;

import java.util.List;

public class DatabaseStack extends Stack {
    private final String jdbcUrl;
    private final ServerlessCluster cluster;

    public DatabaseStack(final Construct scope, final String id) {
        this(scope, id, null, null);
    }

    public DatabaseStack(final Construct scope,
                         final String id,
                         final StackProps props,
                         final NetworkStack network) {
        super(scope, id, props);
        var vpc = network.vpc();
        var privateSubnets = network.privateSubnets();

        var subnetGroup = SubnetGroup.Builder.create(this, "id42-subnet-group")
                .vpc(vpc)
                .description("Subnet group for id42")
                .vpcSubnets(privateSubnets)
                .build();


        var dbSG = SecurityGroup.Builder.create(this, "id42-db-sg")
                .vpc(vpc)
                .allowAllOutbound(true)
                .build();

        dbSG.addIngressRule(Peer.anyIpv4(), Port.tcp(3306), "Allow MySQL IN");

        //TODO: Use parameter store
        var creds = Credentials
                .fromPassword(getRootUsername(),
                        SecretValue.unsafePlainText(getRootPassword()));

        var auroraProps = AuroraMysqlClusterEngineProps
                .builder()
                //.version(AuroraMysqlEngineVersion.VER_2_10_3)
                .version(AuroraMysqlEngineVersion.VER_2_08_3)
                .build();
        var auroraEngine = DatabaseClusterEngine.auroraMysql(auroraProps);

        var scaling = ServerlessScalingOptions.builder()
                .minCapacity(AuroraCapacityUnit.ACU_1)
                .minCapacity(AuroraCapacityUnit.ACU_2)
                .build();

        var dbName=StackConfig.db_name.getString();
        this.cluster = ServerlessCluster.Builder.create(this, "id42-db")
                .engine(auroraEngine)
                .vpc(vpc)
                .defaultDatabaseName(dbName)
                .subnetGroup(subnetGroup)
                .securityGroups(List.of(dbSG))
                .credentials(creds)
                .enableDataApi(true)
                .scaling(scaling)
                .build();

        var outDbArn = CfnOutput.Builder.create(this, "id42-rds-cluster-arn")
                .value(this.cluster.getClusterArn())
                .build();

        var instanceEndpointAddress = this.cluster.getClusterEndpoint();
        var instanceEndpointPort = "3306";
        var databaseOpts="?useSSL=false";
        this.jdbcUrl = "jdbc:mysql://" + instanceEndpointAddress
                + ":"
                + instanceEndpointPort
                + "/"
                + dbName
                + databaseOpts;

        CfnOutput.Builder.create(this, "id42-rds-jdbc-url")
                .value(this.jdbcUrl)
                .build();
    }

    public String jdbcUrl() {
        return jdbcUrl;
    }

    public String getRootUsername(){
        return StackConfig.db_root_username.getString();
    }

    public String getRootPassword(){
        return StackConfig.db_root_password.getString();
    }

    public ServerlessCluster cluster() {
        return this.cluster;
    }
}
