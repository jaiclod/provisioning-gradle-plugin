package gradle.plugins.provisioning

import gradle.plugins.provisioning.aws.AwsDeployConfiguration
import gradle.plugins.provisioning.internal.dependencies.Packages
import gradle.plugins.provisioning.internal.disk.Partitioning
import gradle.plugins.provisioning.internal.network.Networking
import gradle.plugins.provisioning.virtualbox.VirtualBoxMachineDetails
import org.gradle.api.Action
import org.gradle.api.Project

class ProvisioningProject {
    String rootpw
    String installImage
    String lang = "en_US.UTF-8"
    String keyboard = "us"
    String timezone = "Etc/GMT"
    String postInstall
    Boolean poweroff = Boolean.FALSE

    Partitioning partitioning
    Networking networking
    Packages packages

    Project project

    VirtualBoxMachineDetails serverDetails
    AwsDeployConfiguration awsDetails

    ProvisioningProject(Project project) {
        this.project = project
    }

    void partitioning(Action<Partitioning> action) {
        partitioning = new Partitioning()
        action.execute partitioning
    }

    void network(Action<Networking> action) {
        this.networking = new Networking()
        action.execute networking
    }

    void packages(Action<Packages> action) {
        packages = new Packages()
        action.execute packages
    }

    void vbox(Action<VirtualBoxMachineDetails> action) {
        serverDetails = new VirtualBoxMachineDetails()
        action.execute serverDetails
    }

    void aws(Closure clos) {
        def builder = new AwsDeployConfiguration.Builder()
        clos.delegate = builder
        clos.resolveStrategy = Closure.DELEGATE_FIRST
        clos.call()
        this.awsDetails = builder.build()
    }

    void postInstall(Closure clos) {
        this.postInstall = clos.call()
    }
}
