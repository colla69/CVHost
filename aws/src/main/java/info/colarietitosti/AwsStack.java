package info.colarietitosti;

import software.amazon.awscdk.SecretValue;
import software.amazon.awscdk.services.amplify.alpha.*;
import software.amazon.awscdk.services.codebuild.BuildSpec;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AwsStack extends Stack {
    public AwsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AwsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        App amplifyApp = App.Builder.create(this, "CVHost_frontend")
                .sourceCodeProvider(GitHubSourceCodeProvider.Builder.create()
                        .owner("colla69")
                        .repository("CVHost")
                        .oauthToken(new SecretValue("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"))
                        .build())
                .buildSpec(BuildSpec.fromObjectToYaml(Map.of(
                        // Alternatively add a `amplify.yml` to the repo
                        "version", "1.0",
                        "applications", List.of(Map.of(
                                "frontend", Map.of(
                                        "phases", Map.of(
                                                "preBuild", Map.of(
                                                        "commands", List.of(
                                                                "npm install --save cheerio",
                                                                "npm install @vue/cli-service --save-dev",
                                                                "npm ci")),
                                                "build", Map.of(
                                                        "commands", List.of("npm run build"))),
                                        "artifacts", Map.of(
                                                "baseDirectory", "dist",
                                                "files", List.of("**/*")),
                                        "cache", Map.of(
                                                "paths", List.of("node_modules/**/*"))),
                                "appRoot", "frontend")))))
                .environmentVariables(Map.of(
                        "AMPLIFY_MONOREPO_APP_ROOT", "frontend"
                ))
                .autoBranchCreation(AutoBranchCreation.builder() // Automatically connect branches that match a pattern set
                        .patterns(List.of("feature/*", "test/*")).build())
                .autoBranchDeletion(true)
                .build();

        Branch masterBranch = amplifyApp.addBranch("develop");
    }
}
