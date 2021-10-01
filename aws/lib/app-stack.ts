import * as cdk from '@aws-cdk/core';
import * as s3 from '@aws-cdk/aws-s3';
import { IVpc, Vpc } from '@aws-cdk/aws-ec2';
import path from "path";
import { Duration } from '@aws-cdk/core';
import * as ec2 from '@aws-cdk/aws-ec2';

export class AppStack extends cdk.Stack {

  private readonly vpc: IVpc;

  constructor(scope: cdk.Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    this.vpc = new Vpc(this, '/CV-VPC', { maxAzs: 1 });

    const websiteBucket = new s3.Bucket(this, 'WebsiteBucket', {
      publicReadAccess: false
    });

    // new s3deploy.BucketDeployment(this, 'DeployWebsite', {
    //   sources: [s3deploy.Source.asset('./website-dist')],
    //   destinationBucket: websiteBucket,
    //   destinationKeyPrefix: 'web/static' // optional prefix in destination bucket
    // });

  }
}
