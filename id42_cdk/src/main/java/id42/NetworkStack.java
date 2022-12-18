package id42;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.*;
import software.constructs.Construct;

import java.util.List;

public class NetworkStack extends Stack {
    private final SubnetSelection privateNets;
    private final Vpc vpc;

    public NetworkStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public NetworkStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        var privateSubnetsCfg = SubnetConfiguration.builder()
                .cidrMask(24)
                .name("id42-subnet-pvt")
                .subnetType(SubnetType.PRIVATE_ISOLATED)
                .build();

        var publicSubnetsCfg = SubnetConfiguration.builder()
                .cidrMask(24)
                .name("id42-subnet-pub")
                .subnetType(SubnetType.PUBLIC)
                .build();

        var addr = IpAddresses.cidr("10.0.0.0/16");

        this.vpc = Vpc.Builder.create(this, "id42-vpc")
                .maxAzs(3)
                .subnetConfiguration(List.of(
                        privateSubnetsCfg,
                        publicSubnetsCfg))
                .ipAddresses(addr)
                .defaultInstanceTenancy(DefaultInstanceTenancy.DEFAULT)
                .enableDnsHostnames(true)
                .enableDnsSupport(true)
                .natGateways(0)
                .build();

        var webSG = SecurityGroup.Builder.create(this, "id42-web-sg")
                .vpc(vpc)
                .allowAllOutbound(true)
                .build();

        //TODO: Restrict this traffic to the correct port
        webSG.addIngressRule(Peer.anyIpv4(), Port.allTcp(), "Allow HTTP from the world");

        var privateSubnets = vpc.getPrivateSubnets();

        this.privateNets = SubnetSelection
                .builder()
                .subnetType(SubnetType.PRIVATE_ISOLATED)
                .onePerAz(true)
                .build();

        var outVpcId = CfnOutput.Builder.create(this, "pbnk-out-vpcId")
                .value(vpc.getVpcId())
                .build();

    }

    public SubnetSelection privateSubnets() {
        return privateNets;
    }

    public Vpc vpc() {
        return vpc;
    }
}
