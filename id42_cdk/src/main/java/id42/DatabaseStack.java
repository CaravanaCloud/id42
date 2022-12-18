package id42;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.SecretValue;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Peer;
import software.amazon.awscdk.services.ec2.Port;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.rds.*;
import software.constructs.Construct;

import java.util.List;

public class DatabaseStack extends Stack {
    public DatabaseStack(final Construct scope, final String id) {
        this(scope, id, null, null);
    }

    public DatabaseStack(final Construct scope, final String id, final StackProps props,
                         NetworkStack network) {
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
                .fromPassword("admin", SecretValue.unsafePlainText("Masterkey123"));

        var auroraProps = AuroraMysqlClusterEngineProps
                .builder()
                .version(AuroraMysqlEngineVersion.VER_2_10_3)
                .build();
        var auroraEngine = DatabaseClusterEngine.auroraMysql(auroraProps);

        var db = ServerlessCluster.Builder.create(this, "id42-db")
                .engine(auroraEngine)
                .vpc(vpc)
                .subnetGroup(subnetGroup)
                .securityGroups(List.of(dbSG))
                .credentials(creds)
                .enableDataApi(true)
                .build();


        var outVpcId = CfnOutput.Builder.create(this, "pbnk-out-vpcId")
                .value(vpc.getVpcId())
                .build();

    }

}