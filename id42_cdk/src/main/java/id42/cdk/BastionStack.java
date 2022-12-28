package id42.cdk;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.constructs.Construct;

import java.util.List;

public class BastionStack extends Stack {
    private final Role role;

    public BastionStack(final Construct scope, final String id) {
        this(scope, id, null, null);
    }

    public BastionStack(final Construct scope, final String id, final StackProps props,
                        final NetworkStack network) {
        super(scope, id, props);
        var machineImage = MachineImage.latestAmazonLinux();

        var  bastionSg = SecurityGroup.Builder.create(this, "id42-bastion-sg")
                .vpc(network.vpc())
                .allowAllOutbound(true)
                .build();

        //TODO: Restrict this traffic to the correct port
        bastionSg.addIngressRule(Peer.anyIpv4(), Port.allTcp(), "Allow HTTP from the world");

        var ud = UserData.forLinux();
        ud.addCommands("aws ec2 stop-instances --instance-ids $(curl -s http://169.254.169.254/latest/meta-data/instance-id) --region $(curl -s http://169.254.169.254/latest/meta-data/placement/availability-zone | sed 's/[a-z]$//')");


        //TODO: use lest privilege policy
        var adminPolicy = ManagedPolicy.fromManagedPolicyArn(this,
                "AdministratorAccess",
                "arn:aws:iam::aws:policy/AdministratorAccess");


        this.role = Role.Builder.create(this, "id42-bastion-role")
                .assumedBy(ServicePrincipal.Builder.create("ec2.amazonaws.com").build())
                .managedPolicies(List.of(adminPolicy))
                .build();

        var keyName = "id42-debug-keys";

        var keys = CfnKeyPair.Builder.create(this, "id42-bastion-keys")
                .keyName(keyName)
                .build();

        var bastion = Instance.Builder.create(this, "id42-bastion")
                .instanceType(InstanceType.of(InstanceClass.T3, InstanceSize.NANO))
                .machineImage(machineImage)
                .vpc(network.vpc())
                .vpcSubnets(network.publicSubnets())
                .securityGroup(bastionSg)
                .userData(ud)
                .role(role)
                .keyName(keyName)
                .build();

        /*
        var eniSubnets = network.vpc().selectSubnets(
                network.privateSubnets()
        );
        var eniSubnetId = eniSubnets.getSubnetIds().get(0);

        var eni = CfnNetworkInterface.Builder.create(this, "id42-bastion-eni")
                .subnetId(eniSubnetId)
                .build();



        var eniAttach = CfnNetworkInterfaceAttachment.Builder.create(this, "id42-bastion-eni-attach")
                .instanceId(bastion.getInstanceId())
                .networkInterfaceId(pubEni.getRef())
                .deviceIndex("1")
                .build();
        */

        var eip = CfnEIP.Builder.create(this, "id42-bastion-eip")
                .build();

        var eipAssoc = CfnEIPAssociation.Builder.create(this, "id42-bastion-eip-assoc")
                .allocationId(eip.getAttrAllocationId())
                .instanceId(bastion.getInstanceId())
                .build();

        var eipOut = CfnOutput.Builder.create(this, "id42-bastion-eip-out")
                .value(eip.getRef())
                .build();
    }
}
