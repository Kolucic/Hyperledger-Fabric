package it.unicam.cs.hyperledgerfabricwizard.utils.docker

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import it.unicam.cs.hyperledgerfabricwizard.utils.Builder

/**
 * La classe identifica i services da inserire in un docker-compose.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class Service private constructor(
        val image: String?,
        val environment: List<String>?,
        val ports: List<String>?,
        val command: String?,
        @JsonProperty("container_name") val containerName: String?,
        val networks: List<String>?,
        @JsonProperty("working_dir") val workingDir: String?,
        val volumes: List<String>?,
        val network_mode: String?
) {
    open class ServiceBuilder : Builder<Service> {
        private var image: String? = null
        private var command: String? = null
        private var containerName: String? = null
        private var workingDir: String? = null
        private var environment: MutableList<String>? = null
        private var ports: MutableList<String>? = null
        private var networks: MutableList<String>? = null
        private var volumes: MutableList<String>? = null
        private var network_mode: String? = null

        fun image(image: String) = apply {
            this.image = image
        }

        fun command(command: String) = apply {
            this.command = command
        }

        fun workingDir(workingDir: String) = apply {
            this.workingDir = workingDir
        }

        fun containerName(containerName: String) = apply {
            this.containerName = containerName
        }

        fun addEnvironment(name: String, value: String) = apply {
            this.environment = (this.environment ?: mutableListOf()).apply {
                this += "$name=$value"
            }
        }

        fun addPort(localPort: Int, containerPort: Int) = apply {
            this.ports = (this.ports ?: mutableListOf()).apply {
                this += "$localPort:$containerPort"
            }
        }

        fun addVolume(localPath: String, containerPath: String) = apply {
            this.volumes = (this.volumes ?: mutableListOf()).apply {
                this += "$localPath:$containerPath"
            }
        }


        fun networkMode(mode: NetworkMode) {
            this.network_mode = mode.value
        }

        override fun build(): Service {
            return Service(
                    this.image,
                    this.environment,
                    this.ports,
                    this.command,
                    this.containerName,
                    this.networks,
                    this.workingDir,
                    this.volumes,
                    this.network_mode
            )
        }
    }

    companion object {
        fun builder() = ServiceBuilder()
    }

    enum class NetworkMode(val value: String) {
        HOST("host"), BRIDGE("bridge")
    }
}
